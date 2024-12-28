package com.erwebsocket.service;

import com.erwebsocket.model.UserToken;
import com.erwebsocket.model.User;
import com.erwebsocket.model.Admin;
import com.erwebsocket.repository.UserTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserTokenService {

    @Autowired
    private UserTokenRepository userTokenRepository;

    public UserToken saveOrUpdateToken(User user, Admin admin, String fcmToken) {
        // Check if the token already exists for this user or admin
        Optional<UserToken> existingToken = user != null ?
                userTokenRepository.findByuserid(user) :
                userTokenRepository.findByadminid(admin);

        if (existingToken.isPresent()) {
            UserToken token = existingToken.get();
            token.setFcmToken(fcmToken);
            token.setUpdatedAt(LocalDateTime.now());
            return userTokenRepository.save(token);
        }

        // Save a new token record
        UserToken newToken = new UserToken(user, admin, fcmToken, LocalDateTime.now());
        return userTokenRepository.save(newToken);
    }

    public Optional<UserToken> getTokenByUser(User user) {
        return userTokenRepository.findByuserid(user);
    }

    public Optional<UserToken> getTokenByAdmin(Admin admin) {
        return userTokenRepository.findByadminid(admin);
    }

    @Transactional
    public void deleteTokenByUser(User user) {
        userTokenRepository.deleteByuserid(user);
    }

    @Transactional
    public void deleteTokenByAdmin(Admin admin) {
        userTokenRepository.deleteByadminid(admin);
    }
}
