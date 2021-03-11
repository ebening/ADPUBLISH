package com.adinfi.formateador.util;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;

public class StringEncrypter {

    public StringEncrypter() {

    }

    public static String encryptAES(String value) {
        String s = null;
        try {
            s = new String(Base64.encodeBase64(value.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(StringEncrypter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }

    public static String decryptAES(byte[] encrypted) {
        return new String(Base64.decodeBase64(encrypted));
    }

}
