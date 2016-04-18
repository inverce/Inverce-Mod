package com.inverce.utils.tools;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Tools {
    private static Context context;

    public static void init(Context context) {
        Tools.context = context;
    }

    public static class Util {
        public static String getAppVersion() {
            try {
                PackageManager m = context.getPackageManager();
                return m.getPackageInfo(context.getPackageName(), 0).versionName;
            }
            catch (Exception ignored) {
                return "1.0";
            }
        }

        public static void copy(File src, File dst) throws IOException {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new FileInputStream(src);
                out = new FileOutputStream(dst);
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                assert in != null && out != null;
                in.close();
                out.close();
            }
        }

        public static String deAccent(String str) {
            String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(nfdNormalizedString).replaceAll("");
        }

        public static <T> T defaultVal(T val, @NonNull T onNull) {
            return val != null ? val : onNull;
        }
        public static boolean checkRange(int pos, java.util.List<?> list) {
            return list != null && pos >= 0 && pos < list.size();
        }

        public static String asLineArray(List<?> list) {
            if (list == null) {
                return "";
            }
            StringBuilder out = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                Object o = list.get(i);
                out.append("\t").append(i).append(". ").append(o != null ? o.toString() : "[null]").append("\n");
            }
            return out.toString();
        }

        public static boolean nullOrEmpty(String txt) {
            return txt == null || txt.length() < 1;
        }

        public static <T> boolean nullOrEmpty(ArrayList<T> list) {
            return list == null || list.size() == 0;
        }
    }


    //    public static class Collection {
//        public static <T> List<T> remove(@NonNull List<T> collection, @NonNull Selector<T> selector) {
//            ArrayList<T> toRemove = new ArrayList<>();
//            for (T el: collection) {
//                if (selector.select(el)) {
//                    toRemove.add(el);
//                }
//            }
//            collection.removeAll(toRemove);
//            return collection;
//        }
//
//        public static <T> List<T> select(@NonNull List<T> collection, @NonNull Selector<T> selector) {
//            ArrayList<T> selected = new ArrayList<>();
//            for (T el: collection) {
//                if (selector.select(el)) {
//                    selected.add(el);
//                }
//            }
//            return selected;
//        }
//    }


}