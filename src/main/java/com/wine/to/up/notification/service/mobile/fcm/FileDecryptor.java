package com.wine.to.up.notification.service.mobile.fcm;

import java.io.*;
import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class FileDecryptor {
    private static final byte[] SALT = {
            (byte) 0x43, (byte) 0x76, (byte) 0x95, (byte) 0xc7,
            (byte) 0x5b, (byte) 0xd7, (byte) 0x45, (byte) 0x17
    };

    private static Cipher makeCipher(String pass) throws GeneralSecurityException{
        PBEKeySpec keySpec = new PBEKeySpec(pass.toCharArray());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(keySpec);

        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(SALT, 42);

        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");

        cipher.init(Cipher.DECRYPT_MODE, key, pbeParamSpec);

        return cipher;
    }


    public static String decryptFile(String fileName, String password)
            throws GeneralSecurityException, IOException{
        byte[] encData;
        byte[] decData;
        File inFile = new File(fileName);
        String decodedFilepath = fileName + ".decrypted";

        Cipher cipher = FileDecryptor.makeCipher(password);

        FileInputStream stream = new FileInputStream(inFile);
        encData = new byte[(int)inFile.length()];
        stream.read(encData);
        stream.close();
        decData = cipher.doFinal(encData);

        FileOutputStream target = new FileOutputStream(new File(decodedFilepath));
        target.write(decData);
        target.close();

        return decodedFilepath;
    }
}


