package com.erwebsocket.repository;

import com.erwebsocket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);

    User deleteById(int id);
}
