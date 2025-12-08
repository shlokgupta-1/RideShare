package com.example.rideshare.service;

import com.example.rideshare.dto.LoginRequest;
import com
        .example.rideshare.dto.RegisterRequest;
import com
        .example.rideshare.exception.BadRequestException;
import com
        .example.rideshare.model.User;
import com
        .example.rideshare.repository.UserRepository;
import com
        .example.rideshare.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder encoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    public void register(RegisterRequest req) {
        if(userRepository.findByUsername(req.getUsername()).isPresent())
            throw new BadRequestException("Username already exists");
        User u = new User(req.getUsername(),
                encoder.encode(req.getPassword()),
                req.getRole());
        userRepository.save(u);
    }

    public String login(LoginRequest req) {
        User u = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new BadRequestException("Invalid username/password"));
        if(!encoder.matches(req.getPassword(), u.getPassword()))
            throw new BadRequestException("Invalid username/password");
        return jwtUtil.generateToken(u.getUsername(), u.getRole());
    }
}
