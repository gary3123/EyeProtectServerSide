package com.example.eyeprotext.utensil;

import java.util.Base64;
import java.util.UUID;

public class ShortIDGenerator {
    public static String generateShortID() {
        UUID uuid = UUID.randomUUID();
        byte[] bytes = uuid.toString().getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(bytes);
        String encodedStr = new String(encodedBytes);
        return encodedStr.substring(0, 8); // 返回前8个字符作为短ID
    }
}
