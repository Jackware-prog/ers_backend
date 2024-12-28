package com.erwebsocket.repository;

import com.erwebsocket.model.Admin;
import com.erwebsocket.model.User;
import com.erwebsocket.model.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    Optional<UserToken> findByuserid(User userid);

    Optional<UserToken> findByadminid(Admin adminId);

    Optional<UserToken> findByFcmToken(String fcmToken);

    void deleteByuserid(User userid);

    void deleteByadminid(Admin adminid);
}
