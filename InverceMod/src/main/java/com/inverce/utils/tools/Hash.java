package com.inverce.utils.tools;

import com.inverce.utils.logging.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SuppressWarnings("unused")
public class Hash {

    public static String md5(String s) {
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(s.getBytes("UTF-8"));

            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.exs("UTILS", "No such algorithm", e);
        } catch (UnsupportedEncodingException e) {
            Log.exs("UTILS", "Unsupported encoding exception", e);
        }

        return "";
    }
}
