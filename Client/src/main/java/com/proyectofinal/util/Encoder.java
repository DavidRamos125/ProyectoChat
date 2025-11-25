package com.proyectofinal.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Encoder {
    private static final String HASH_ALG = "SHA-256";
    public static String encode(String plainPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALG);
            byte[] digest = md.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
