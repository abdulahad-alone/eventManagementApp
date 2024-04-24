package com.example.eventapp.utils;

import java.util.Random;

public class OrderNumberGenerator {

    public static String generateOrderNumber() {
        // Get current timestamp
        long timestamp = System.currentTimeMillis();

        // Generate random string (you can adjust the length as needed)
        String randomString = generateRandomString(6);

        // Combine timestamp and random string
        return "ORD-" + timestamp + "-" + randomString;
    }

    private static String generateRandomString(int length) {
        // Define characters to use in the random string
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        // Create StringBuilder to store the random string
        StringBuilder stringBuilder = new StringBuilder();

        // Create random object
        Random random = new Random();

        // Generate random characters
        for (int i = 0; i < length; i++) {
            // Get random index from characters
            int index = random.nextInt(characters.length());

            // Append random character to string builder
            stringBuilder.append(characters.charAt(index));
        }

        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        String orderNumber = generateOrderNumber();
        System.out.println("Generated Order Number: " + orderNumber);
    }
}