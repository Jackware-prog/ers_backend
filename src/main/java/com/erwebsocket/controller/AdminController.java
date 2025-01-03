package com.erwebsocket.controller;

import com.erwebsocket.model.Admin;
import com.erwebsocket.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Admin Registration Endpoint
    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@RequestBody Admin admin) {
        try {
            // Check if admin email already exists
            Optional<Admin> existingAdmin = adminRepository.findByAdminemail(admin.getAdminemail());
            if (existingAdmin.isPresent()) {
                return ResponseEntity.badRequest().body("Error: Email is already registered.");
            }

            // Set default status to offline (false)
            admin.setStatus("offline");

            // Encrypt the password
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));

            // Save the admin to the database
            Admin registeredAdmin = adminRepository.save(admin);

            // Return the registered admin (without password)
            registeredAdmin.setPassword(null);
            return ResponseEntity.ok(registeredAdmin);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Admin Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody Admin loginRequest) {
        try {
            // Find admin by email
            Optional<Admin> admin = adminRepository.findByAdminemail(loginRequest.getAdminemail());

            // Check if admin exists and password matches
            if (admin.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), admin.get().getPassword())) {
                Admin loggedInAdmin = admin.get();

                // Hide password before returning the response
                loggedInAdmin.setPassword(null);
                return ResponseEntity.ok(loggedInAdmin);
            }

            return ResponseEntity.status(401).body("Invalid email or password.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
