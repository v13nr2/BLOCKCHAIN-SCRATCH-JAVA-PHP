package net.borneolink.libs;

import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.encoders.Hex;

// Teknik Enkripsi
public class EnDec {
    // Parameter Salt Untuk PBE
    private static String salt = "&&&jangandigangguyamaafssshhhhhhhhhhh!!!!";

    public String encryptCBCIV(byte[] iv, String key, String strToEncrypt) throws Exception {
        // Parameter Enkripsi
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        // Key Generator Menggunakan PBE
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
        System.out.println("Kunci yang Digunakan : " +Hex.toHexString(secretKey.getEncoded()));

        // Algoritma, Mode Operasi dan Enkripsi
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
        return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
    }

    public String encryptCBC(String strToEncrypt, String key) throws Exception {
        // Parameter Enkripsi
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //128 Bit
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        // Key Generator Menggunakan PBE
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
        System.out.println("Kunci yang Digunakan : " +Hex.toHexString(secretKey.getEncoded()));

        // Algoritma, Mode Operasi dan Enkripsi
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
        return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
    }

    // Teknik Dekripsi
    public String decryptCBC(String strToDecrypt, String key) throws Exception {
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        // Key Generator Menggunakan PBE
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(key.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
        //System.out.println("Kunci yang Digunakan : " +Hex.toHexString(secretKey.getEncoded()));

        // Algoritma, Mode Operasi dan Dekripsi
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
        return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
    }
}
