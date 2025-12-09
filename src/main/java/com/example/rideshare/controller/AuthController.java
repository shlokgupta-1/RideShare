package com.example.rideshare.controller;

import com.example.rideshare.dto.RegisterRequest;
import com.example.rideshare.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final Map<String, Map<String, String>> users = new HashMap<>();

    public AuthController(PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {

        if (!"USER".equalsIgnoreCase(req.getRole()) && !"DRIVER".equalsIgnoreCase(req.getRole())) {
            return ResponseEntity.badRequest().body("Role must be USER or DRIVER");
        }

        if (users.containsKey(req.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        Map<String, String> user = new HashMap<>();
        user.put("password", passwordEncoder.encode(req.getPassword()));
        user.put("role", req.getRole().toUpperCase());

        users.put(req.getUsername(), user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RegisterRequest req) {

        if (!users.containsKey(req.getUsername())) {
            return ResponseEntity.status(401).body("User not found");
        }

        Map<String, String> user = users.get(req.getUsername());

        if (!passwordEncoder.matches(req.getPassword(), user.get("password"))) {
            return ResponseEntity.status(401).body("Invalid password");
        }

        String token = jwtUtil.generateToken(req.getUsername(), "ROLE_" + user.get("role"));

        return ResponseEntity.ok(Map.of("token", token));
    }
}
