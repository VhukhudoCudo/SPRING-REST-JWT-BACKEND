package com.example.demo.controller;

import com.example.demo.model.AppUser;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.config.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AppUserRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AppUser user) {
        if (repo.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username exists");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        AppUser saved = repo.save(user);
        System.out.println("Registered new user: " + saved.getUsername());
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AppUser user) {
        System.out.println("Login attempt: " + user.getUsername());

        AppUser found = repo.findByUsername(user.getUsername()).orElse(null);
        if (found != null) {
            System.out.println("Found user. Stored password hash: " + found.getPassword());
            System.out.println("Password matches: " + encoder.matches(user.getPassword(), found.getPassword()));
        } else {
            System.out.println("User not found in database");
        }

        if (found != null && encoder.matches(user.getPassword(), found.getPassword())) {
            String token = jwtUtils.generateToken(found.getUsername());
            System.out.println("Generated token: " + token); // debug
            return ResponseEntity.ok(Map.of("token", token));
        }

        System.out.println("Login failed for: " + user.getUsername());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
