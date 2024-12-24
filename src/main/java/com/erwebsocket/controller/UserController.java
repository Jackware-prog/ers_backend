package com.erwebsocket.controller;

import com.erwebsocket.model.User;
import com.erwebsocket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Register a new User
    @PostMapping("auth/register")
    public ResponseEntity<Object> registerUser(@RequestBody User user) {

        // Check if IC already exists
        Optional<User> existingUserByIC = userRepository.findByic(user.getIC());
        if (existingUserByIC.isPresent()) {
            return ResponseEntity.badRequest().body("IC is already in use!");
        }

        // Check if email already exists
        Optional<User> existingUserByEmail = userRepository.findByemail(user.getEmail());
        if (existingUserByEmail.isPresent()) {
            return ResponseEntity.badRequest().body("Email is already in use!");
        }

        // Check if phonenumber already exists
        Optional<User> existingUserByPhonenum = userRepository.findByphonenumber(user.getPhonenumber());
        if (existingUserByPhonenum.isPresent()) {
            return ResponseEntity.badRequest().body("Phone Number is already in use!");
        }

        // Encrypt the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("auth/login")
    public ResponseEntity<Object> loginUser(@RequestBody User loginRequest) {
        // Check if payload is null
        if (loginRequest.getIC() == null && loginRequest.getPassword() == null) {
            return ResponseEntity.status(401).body("Missing IC or Password");
        }

        // Find user by IC
        Optional<User> existingUser = userRepository.findByic(loginRequest.getIC());
        if (existingUser.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid IC or Password");
        }

        // Check if password matches
        User user = existingUser.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid IC or Password");
        }


        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getUserid());

        // Successful login
        return ResponseEntity.ok(response);
    }

    // Retrieve all Users
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll(); // Fetch all users from the repository
    }
}
