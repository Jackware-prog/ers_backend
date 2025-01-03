package com.erwebsocket.repository;

import com.erwebsocket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByemail(String email);

    Optional<User> findByic(String IC);

    Optional<User> findByphonenumber(String phonenumber);

    User findByUserid(Long userid);

}
