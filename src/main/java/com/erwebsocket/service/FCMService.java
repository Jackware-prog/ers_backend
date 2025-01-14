package com.erwebsocket.service;

import com.erwebsocket.model.UserToken;
import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FCMService {

    public void sendNotification(UserToken userToken, String title, String body) {
        Message message = Message.builder()
                .setToken(userToken.getFcmToken())
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendNotificationToMultipleTokens(List<String> tokens, String title, String body) {
        System.out.println("Preparing to send notifications to tokens: " + tokens);

        for (String token : tokens) {
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .build();

            try {
                String response = FirebaseMessaging.getInstance().send(message);
                System.out.println("Successfully sent message to token: " + token + ", response: " + response);
            } catch (FirebaseMessagingException e) {
                System.err.println("Error sending message to token: " + token);
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
