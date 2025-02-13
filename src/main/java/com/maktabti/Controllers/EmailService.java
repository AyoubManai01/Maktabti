package com.maktabti.Controllers;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class EmailService {
    private static final String API_KEY = "042975d37a2e1bcf7c8c845602f3f133-d74c6fae-9bec-45aa-9272-30786942307a";  // Replace with your actual API key
    private static final String FROM_EMAIL = "moenesjaouadi2s2@gmail.com";  // Replace with your sender email
    private static final String API_URL = "https://api.infobip.com/email/2/send";  // Correct API endpoint

    public void sendSignUpConfirmation(String recipientEmail) {
        String subject = "Sign-Up Confirmation";
        String message = "Thank you for signing up! Your account has been created.";

        try {
            sendEmail(recipientEmail, subject, message);
            System.out.println("Sign-up email sent to: " + recipientEmail);
        } catch (IOException e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }

    void sendEmail(String recipientEmail, String subject, String message) throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");  // Set Content-Type header
        connection.setRequestProperty("Authorization", "App " + API_KEY);  // Set Authorization header
        connection.setDoOutput(true);

        // Create JSON payload
        String jsonPayload = String.format(
                "{\"from\":\"%s\",\"to\":\"%s\",\"subject\":\"%s\",\"text\":\"%s\"}",
                FROM_EMAIL, recipientEmail, subject, message
        );

        // Send the request
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Get the response
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String response;
                StringBuilder responseContent = new StringBuilder();
                while ((response = br.readLine()) != null) {
                    responseContent.append(response);
                }
                System.out.println("Email sent successfully: " + responseContent.toString());
            }
        } else {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                String response;
                StringBuilder errorContent = new StringBuilder();
                while ((response = br.readLine()) != null) {
                    errorContent.append(response);
                }
                System.err.println("Error in response: " + errorContent.toString());
            }
        }
    }
}