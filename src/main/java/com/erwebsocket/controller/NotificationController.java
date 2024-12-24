package com.erwebsocket.controller;

import com.erwebsocket.model.UserToken;
import com.erwebsocket.model.User;
import com.erwebsocket.service.FCMService;
import com.erwebsocket.service.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private FCMService fcmService;

    @Autowired
    private UserTokenService userTokenService;

    @PostMapping("/send")
    public String sendNotification(@RequestParam Long userId, @RequestParam String title, @RequestParam String body) {
        User user = new User();
        user.setUserid(userId);
        UserToken userToken = userTokenService.getTokenByUser(user).orElseThrow(() -> new RuntimeException("Token not found"));

        fcmService.sendNotification(userToken, title, body);
        return "Notification sent successfully!";
    }
}