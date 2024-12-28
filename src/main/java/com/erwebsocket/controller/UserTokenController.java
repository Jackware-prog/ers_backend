package com.erwebsocket.controller;

import com.erwebsocket.model.Admin;
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
        String fcmToken = payload.get("fcmToken");
        Long userId = payload.containsKey("userId") ? Long.parseLong(payload.get("userId")) : null;
        Long adminId = payload.containsKey("adminId") ? Long.parseLong(payload.get("adminId")) : null;

        User user = userId != null ? new User() : null;
        Admin admin = adminId != null ? new Admin() : null;

        if (user != null) user.setUserid(userId);
        if (admin != null) admin.setAdminid(adminId);

        UserToken savedToken = userTokenService.saveOrUpdateToken(user, admin, fcmToken);
        return ResponseEntity.ok(savedToken);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteToken(@RequestParam(required = false) Long userId,
                                            @RequestParam(required = false) Long adminId) {
        if (userId != null) {
            userTokenService.deleteTokenByUser(new User() {{ setUserid(userId); }});
        } else if (adminId != null) {
            userTokenService.deleteTokenByAdmin(new Admin() {{ setAdminid(adminId); }});
        }
        return ResponseEntity.noContent().build();
    }
}