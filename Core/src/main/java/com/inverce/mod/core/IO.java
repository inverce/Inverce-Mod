package com.inverce.mod.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@SuppressWarnings("unused")
public class IO {
    public static void closeSilently(@Nullable Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
                /* ignored by user request */
            }
        }
    }

    public static void copyFile(@NonNull File in, @NonNull File out) throws IOException, FileNotFoundException {
        copyStream(new FileInputStream(in), new FileOutputStream(out));
    }

    public static long copyStream(@NonNull InputStream is, @NonNull OutputStream out) throws IOException {
        return copyStream(is, out, true);
    }

    public static long copyStream(@NonNull InputStream in, @NonNull OutputStream out, boolean closeStreams) throws IOException {
        long total = 0;
        try {
            byte[] buf = new byte[8096];
            while (true) {
                int r = in.read(buf);
                if (r == -1) {
                    break;
                }
                out.write(buf, 0, r);
                total += r;
            }
        } finally {
            if (closeStreams) {
                closeSilently(in);
                closeSilently(out);
            }
        }
        return total;
    }
}
