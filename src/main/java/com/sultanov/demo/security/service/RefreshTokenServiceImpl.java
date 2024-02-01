package com.sultanov.demo.security.service;


import com.sultanov.demo.entity.User;
import com.sultanov.demo.security.entities.RefreshToken;
import com.sultanov.demo.security.repos.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl {
    int expireSeconds = 604800;

    private RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String createRefreshToken(User user) {
        RefreshToken token = refreshTokenRepository.findByUserId(user.getId());
        if (token == null) {
            token = new RefreshToken();
            token.setUser(user);
        }
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Date.from(Instant.now().plusSeconds(expireSeconds)));
        refreshTokenRepository.save(token);
        return token.getToken();
    }

    public boolean isRefreshExpired(RefreshToken token) {
        return token.getExpiryDate().before(new Date());
    }

    public RefreshToken getByUser(Long userId) {
        return refreshTokenRepository.findByUserId(userId);
    }

}
