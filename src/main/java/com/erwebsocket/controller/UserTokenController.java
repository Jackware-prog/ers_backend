package com.erwebsocket.controller;

import com.erwebsocket.model.User;
import com.erwebsocket.model.UserToken;
import com.erwebsocket.service.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/fcm")
public class UserTokenController {

    @Autowired
    private UserTokenService userTokenService;

    @PostMapping("/save")
    public ResponseEntity<UserToken> saveOrUpdateToken(@RequestBody Map<String, String> payload) {
        Long userId = Long.parseLong(payload.get("userId"));
        String fcmToken = payload.get("fcmToken");

        User user = new User();
        user.setUserid(userId);

        UserToken savedToken = userTokenService.saveOrUpdateToken(user, fcmToken);
        return ResponseEntity.ok(savedToken);
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<Optional<UserToken>> getTokenByUserId(@PathVariable Long userId) {
        User user = new User();
        user.setUserid(userId);
        Optional<UserToken> userToken = userTokenService.getTokenByUser(user);

        if (userToken.isPresent()) {
            return ResponseEntity.ok(userToken);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteTokenByUserId(@PathVariable Long userId) {
        User user = new User();
        user.setUserid(userId);
        userTokenService.deleteTokenByUser(user);
        return ResponseEntity.noContent().build();
    }
}