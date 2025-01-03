package com.erwebsocket.controller;

import com.erwebsocket.model.User;
import com.erwebsocket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Object> registerUser(@RequestBody RegisterRequest registerRequest) {

        // Check if IC already exists
        Optional<User> existingUserByIC = userRepository.findByic(registerRequest.getIc());
        if (existingUserByIC.isPresent()) {
            return ResponseEntity.badRequest().body("IC is already in use!");
        }

        // Check if email already exists
        Optional<User> existingUserByEmail = userRepository.findByemail(registerRequest.getEmail());
        if (existingUserByEmail.isPresent()) {
            return ResponseEntity.badRequest().body("Email is already in use!");
        }

        // Check if phonenumber already exists
        Optional<User> existingUserByPhonenum = userRepository.findByphonenumber(registerRequest.getPhonenumber());
        if (existingUserByPhonenum.isPresent()) {
            return ResponseEntity.badRequest().body("Phone Number is already in use!");
        }

        // Encrypt the password
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));


        User user = new User();
        user.setName(registerRequest.getName());
        user.setIC(registerRequest.getIc());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setPhonenumber(registerRequest.getPhonenumber());
        user.setAddress(registerRequest.getAddress());
        user.setState(registerRequest.getState());

        // Save the user
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    public static class RegisterRequest {
        private String name;
        private String ic;
        private String password;
        private String email;
        private String phonenumber;
        private String address;
        private String state;

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIc() {
            return ic;
        }

        public void setIc(String ic) {
            this.ic = ic;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhonenumber() {
            return phonenumber;
        }

        public void setPhonenumber(String phonenumber) {
            this.phonenumber = phonenumber;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

    @PostMapping("auth/login")
    public ResponseEntity<Object> loginUser(@RequestBody LoginRequest loginRequest) {
        // Validate payload
        if (loginRequest == null || loginRequest.getIc() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.badRequest().body("Missing IC or Password");
        }

        // Find user by IC
        Optional<User> existingUser = userRepository.findByic(loginRequest.getIc());
        if (existingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid IC or Password");
        }

        User user = existingUser.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid IC or Password");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getUserid());

        return ResponseEntity.ok(response);
    }


    public static class LoginRequest {
        private String ic;
        private String password;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getIc() {
            return ic;
        }

        public void setIc(String ic) {
            this.ic = ic;
        }
        // Getters and Setters
    }



    // API to find a user by userid
    @GetMapping("/{userid}")
    public ResponseEntity<?> findUserByUserid(@PathVariable Long userid) {
        try {
            User user = userRepository.findByUserid(userid);
            if (user == null) {
                return ResponseEntity.status(404).body("User not found with userid: " + userid);
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    // API to update user profile
    @PutMapping("/{userid}/update")
    public ResponseEntity<Object> updateUserDetails(
            @PathVariable Long userid,
            @RequestBody Map<String, String> updates) {
        try {
            // Find user by userid
            Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUserid(userid));
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(404).body("User not found with userid: " + userid);
            }

            User user = optionalUser.get();

            // Update fields if they are provided
            if (updates.containsKey("phonenumber")) {
                String newPhoneNumber = updates.get("phonenumber");
                if (!newPhoneNumber.matches("^\\+60\\d{9,10}$")) {
                    return ResponseEntity.badRequest().body("Invalid Malaysian phone number format.");
                }
                user.setPhonenumber(newPhoneNumber);
            }

            if (updates.containsKey("address")) {
                String newAddress = updates.get("address");
                if (newAddress.isEmpty()) {
                    return ResponseEntity.badRequest().body("Address cannot be empty.");
                }
                user.setAddress(newAddress);
            }

            if (updates.containsKey("state")) {
                String newState = updates.get("state");
                // Assuming state validation
                List<String> validStates = List.of("Johor", "Kedah", "Kelantan", "Malacca",
                        "Negeri Sembilan", "Pahang", "Penang", "Perak", "Perlis",
                        "Sabah", "Sarawak", "Selangor", "Terengganu");
                if (!validStates.contains(newState)) {
                    return ResponseEntity.badRequest().body("Invalid state provided.");
                }
                user.setState(newState);
            }

            // Save updated user
            userRepository.save(user);
            return ResponseEntity.ok("User details updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

}
