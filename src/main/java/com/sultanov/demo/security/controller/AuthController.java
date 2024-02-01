package com.sultanov.demo.security.controller;

import com.sultanov.demo.entity.User;
import com.sultanov.demo.security.entities.RefreshToken;
import com.sultanov.demo.security.requests.RefreshRequest;
import com.sultanov.demo.security.requests.UserRequest;
import com.sultanov.demo.security.responses.AuthResponse;
import com.sultanov.demo.security.security.JwtTokenProvider;
import com.sultanov.demo.security.service.RefreshTokenServiceImpl;
import com.sultanov.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenServiceImpl refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserRequest loginRequest) {
        AuthResponse authResponse = new AuthResponse();
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getPin(), loginRequest.getPassword());
            Authentication auth = authenticationManager.authenticate(authToken);

            SecurityContextHolder.getContext().setAuthentication(auth);
            String jwtToken = jwtTokenProvider.generateJwtToken(auth);
            User user = userService.getOneUserByUserPin(loginRequest.getPin());

            authResponse.setAccessToken("Bearer " + jwtToken);
            authResponse.setRefreshToken(refreshTokenService.createRefreshToken(user));
            authResponse.setUserId(user.getId());

            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } catch (AuthenticationException e) {
            authResponse.setMessage("Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserRequest registerRequest) {
        AuthResponse authResponse = new AuthResponse();
        if (userService.getOneUserByUserPin(registerRequest.getPin()) != null) {
            authResponse.setMessage("PIN already in use!");
            return new ResponseEntity<>(authResponse, HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setPin(registerRequest.getPin());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userService.createUser(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerRequest.getPin(), registerRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtTokenProvider.generateJwtToken(authentication);

        authResponse.setMessage("User successfully registered.");
        authResponse.setAccessToken("Bearer " + jwtToken);
        authResponse.setRefreshToken(refreshTokenService.createRefreshToken(user));
        authResponse.setUserId(user.getId());
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest refreshRequest) {
        AuthResponse response = new AuthResponse();
        RefreshToken token = refreshTokenService.getByUser(refreshRequest.getUserId());
        if (token.getToken().equals(refreshRequest.getRefreshToken()) &&
                !refreshTokenService.isRefreshExpired(token)) {

            User user = token.getUser();
            String jwtToken = jwtTokenProvider.generateJwtTokenByUserId(user.getId());
            response.setMessage("Token successfully refreshed.");
            response.setAccessToken("Bearer " + jwtToken);
            response.setUserId(user.getId());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setMessage("Refresh token is not valid.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}
