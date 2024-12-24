package com.erwebsocket.service;

import com.erwebsocket.model.UserToken;
import com.erwebsocket.model.User;
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

    public UserToken saveOrUpdateToken(User userid, String fcmToken) {
        // Check if the token already exists for this user
        Optional<UserToken> existingToken = userTokenRepository.findByuserid(userid);

        if (existingToken.isPresent()) {
            UserToken token = existingToken.get();
            token.setFcmToken(fcmToken);
            token.setUpdatedAt(LocalDateTime.now());
            return userTokenRepository.save(token);
        }

        // Save a new token record
        UserToken newToken = new UserToken(userid, fcmToken, LocalDateTime.now());
        return userTokenRepository.save(newToken);
    }

    public Optional<UserToken> getTokenByUser(User userid) {
        return userTokenRepository.findByuserid(userid);
    }

    @Transactional
    public void deleteTokenByUser(User userid) {
        userTokenRepository.deleteByuserid(userid);
    }
}

