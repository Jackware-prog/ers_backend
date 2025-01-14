package com.erwebsocket.service;

import com.erwebsocket.model.User;
import com.erwebsocket.model.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private FCMService fcmService;

    @Autowired
    private UserTokenService userTokenService;

    public void sendNotification(Long userId, String title, String body) {
        User user = new User();
        user.setUserid(userId);

        // Fetch the user token
        UserToken userToken = userTokenService.getTokenByUser(user)
                .orElseThrow(() -> new RuntimeException("Token not found for userId: " + userId));

        // Use FCMService to send the notification
        fcmService.sendNotification(userToken, title, body);
    }

    // New method for all users
    public void sendNotificationToAllUsers(String title, String body) {
        List<String> allTokens = userTokenService.getAllTokens();

        if (allTokens.isEmpty()) {
            System.err.println("No tokens found for all users");
            return;
        }


        fcmService.sendNotificationToMultipleTokens(allTokens, title, body);
    }
}