package com.AiNotesGenerator.Service;

import com.AiNotesGenerator.Entity.User;
import com.AiNotesGenerator.Reopsitory.UserRepository;
import com.AiNotesGenerator.Security.JwtUtil;
import com.AiNotesGenerator.Security.UserDetailsServiceImpl;
import com.AiNotesGenerator.dto.AuthResponse;
import com.AiNotesGenerator.dto.LoginRequest;
import com.AiNotesGenerator.dto.RegisterRequest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            AuthenticationManager authenticationManager,
            UserDetailsServiceImpl userDetailsService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(user.getEmail());

        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getName()
        );
    }

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(user.getEmail());

        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getName()
        );
    }
}