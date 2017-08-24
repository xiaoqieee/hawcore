package com.banzhiyan.security.db;

import com.banzhiyan.core.util.ResourceUtils;
import com.banzhiyan.util.Charsets;
import com.banzhiyan.util.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Created by xn025665 on 2017/8/24.
 */

class PasswordUtils {
    private static final String KSP = "2d7a63a9e34608a216aae84ab3ae0616798e72095e1b";
    private static final String PKP = "2d7a63a9e34608a216aae84ab3ae06167996631f";
    private static final String SUFFIX = ".RSA.512.DB";
    private static final byte[] KEYS = new byte[]{-4, 102, -32, 109, -107, 36, 31, -8, -59, -26, -8, -120, -91, -92, 37, -85};
    private static final byte[] IVS = new byte[]{-118, -89, 92, 91, 55, 20, -90, -97, -22, -105, 6, 13, -96, 92, 53, -36};
    private static final KeyStore KEYSTORE;

    PasswordUtils() {
    }

    static String decrypt(String data) {
        try {
            Cipher cipher = getCipher();
            String plainText = new String(cipher.doFinal(Hex.decodeHexString(data)), Charsets.UTF_8);
            if(plainText != null && plainText.endsWith(".RSA.512.DB")) {
                return plainText.substring(0, plainText.lastIndexOf(".RSA.512.DB"));
            }
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }

        throw new RuntimeException("Decryption failed.");
    }

    private static Cipher getCipher() throws Exception {
        PrivateKey privateKey = (PrivateKey)KEYSTORE.getKey("SensitiveKey", fromAES("2d7a63a9e34608a216aae84ab3ae06167996631f"));
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(2, privateKey);
        return cipher;
    }

    private static char[] fromAES(String cipherText) {
        SecretKeySpec key = new SecretKeySpec(KEYS, "AES");
        IvParameterSpec iv = new IvParameterSpec(IVS);

        try {
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(2, key, iv, new SecureRandom());
            return (new String(cipher.doFinal(Hex.decodeHexString(cipherText)), Charsets.UTF_8)).toCharArray();
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    static String toAES(String plainText) {
        SecretKeySpec key = new SecretKeySpec(KEYS, "AES");
        IvParameterSpec iv = new IvParameterSpec(IVS);

        try {
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(1, key, iv, new SecureRandom());
            return Hex.encodeHexString(cipher.doFinal(plainText.getBytes("UTF-8")));
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }

    static {
        try {
            KEYSTORE = KeyStore.getInstance("JCEKS");
            InputStream is = ResourceUtils.getResourceAsStream("classpath:ooh/bravo/security/db/db.sec");
            Throwable var1 = null;

            try {
                KEYSTORE.load(is, fromAES("2d7a63a9e34608a216aae84ab3ae0616798e72095e1b"));
            } catch (Throwable var11) {
                var1 = var11;
                throw var11;
            } finally {
                if(is != null) {
                    if(var1 != null) {
                        try {
                            is.close();
                        } catch (Throwable var10) {
                            var1.addSuppressed(var10);
                        }
                    } else {
                        is.close();
                    }
                }

            }

        } catch (NoSuchAlgorithmException | CertificateException | IOException | KeyStoreException var13) {
            throw new RuntimeException(var13);
        }
    }
}
