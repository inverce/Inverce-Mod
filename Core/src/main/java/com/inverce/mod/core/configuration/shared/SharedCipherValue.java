//package com.inverce.mod.core.configuration.shared;
//
//import java.security.SecureRandom;
//
//import javax.crypto.Cipher;
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//
//import static com.inverce.mod.core.MathEx.fromBase64Bytes;
//import static com.inverce.mod.core.MathEx.toBase64;
//
//@SuppressWarnings({"unused", "WeakerAccess"})
//public class SharedCipherValue extends SharedValue<String> {
//    private static String ENCRYPTION = "AES/CFB/PKCS5Padding";
//    private final String cipher;
//
//    public SharedCipherValue(String key, String cipher) {
//        this(key, cipher, "im_shared", null);
//    }
//
//    public SharedCipherValue(String key, String cipher, String defaultValue) {
//        this(key, cipher, "im_shared", defaultValue);
//    }
//
//    public SharedCipherValue(String key, String cipher, String storeFile, String defaultValue) {
//        super(String.class, key, storeFile, defaultValue);
//        this.cipher = cipher;
//    }
//
//    private SecretKey generateKey() throws Exception {
//        byte[] keyStart = cipher.getBytes();
//        KeyGenerator kgen = KeyGenerator.getInstance("AES");
//        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
//        sr.setSeed(keyStart);
//        kgen.init(128, sr); // 192 and 256 bits may not be available
//        SecretKey skey = kgen.generateKey();
//        return new SecretKeySpec(skey.getEncoded(), "AES");
//    }
//
//    private byte[] encryptMsg(String message) throws Exception {
//        Cipher cipher = Cipher.getInstance(ENCRYPTION);
//        cipher.init(Cipher.ENCRYPT_MODE, generateKey());
//        return cipher.doFinal(message.getBytes("UTF-8"));
//    }
//
//    private String decryptMsg(byte[] cipherText) throws Exception {
//        Cipher cipher = Cipher.getInstance(ENCRYPTION);
//        cipher.init(Cipher.DECRYPT_MODE, generateKey());
//        return new String(cipher.doFinal(cipherText), "UTF-8");
//    }
//
//    @Override
//    public String get() {
//        try {
//            return decryptMsg(fromBase64Bytes(super.get()));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public boolean set(String e) {
//        try {
//            return super.set(toBase64(encryptMsg(e)));
//        } catch (Exception e1) {
//            throw new RuntimeException(e1);
//        }
//    }
//}
