package com.sultanov.demo.security.security;


import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    private String SECRET = "dsbceuwhdikso3648gfhf73ekjd82jdkd042iudu743udjfir84u3h2634hgfjd86";
    private long EXPIRES_IN = 604800000;

    public String generateJwtToken(Authentication auth) {
        JwtUserDetails userDetails = (JwtUserDetails) auth.getPrincipal();
        Date expireDate = new Date(new Date().getTime() + EXPIRES_IN);
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();
    }

    // Refresh Token
    public String generateJwtTokenByUserId(Long userId) {
        Date expireDate = new Date(new Date().getTime() + EXPIRES_IN);
        return Jwts.builder().setSubject(Long.toString(userId))
                .setIssuedAt(new Date()).setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();
    }

    public String getUserIdFromJwt(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (MalformedJwtException e) {
            return false;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (UnsupportedJwtException e) {
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getExpiration();
        return expiration.before(new Date());
    }
}
