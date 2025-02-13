package com.maktabti.Controllers;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;

public class EmailService {

    // Replace with your actual SendGrid API Key
    private static final String SENDGRID_API_KEY = "SK53af7d2740516e37293ace98ed1a0a6c"; // Replace with your API Key

    /**
     * Sends an email using SendGrid API
     *
     * @param toEmail   The recipient email address
     * @param subject   The subject of the email
     * @param body      The body of the email
     */
    public void sendEmail(String toEmail, String subject, String body) {
        Email from = new Email("moenesjaouadi2s2@gmail.com"); // Replace with your verified sender email
        Email to = new Email(toEmail); // Recipient email address
        Content content = new Content("text/plain", body); // Email content (plain text)
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(SENDGRID_API_KEY);  // Initialize SendGrid API with your API key
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);  // Send the email
            System.out.println("Email sent to: " + toEmail);
            System.out.println("Response code: " + response.getStatusCode());
            System.out.println("Response body: " + response.getBody());
        } catch (IOException ex) {
            System.out.println("Error sending email: " + ex.getMessage());
        }
    }
}
