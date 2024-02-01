package com.sultanov.demo.security.security;


import com.sultanov.demo.security.service.UserDetailServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    UserDetailServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwtToken = extractJwtFromRequest(request);
            log.info("--filter1");
            if (StringUtils.hasText(jwtToken) && jwtTokenProvider.validateToken(jwtToken)) {
                log.info("--filter2");
                //Long id = jwtTokenProvider.getUserIdFromJwt(jwtToken);
                String pin = jwtTokenProvider.getUserIdFromJwt(jwtToken);
                log.info("--filter3");
                //UserDetails user = userDetailsService.loadUserById(id);
                UserDetails user = userDetailsService.loadUserByPin(pin);
                log.info("--filter4");
                log.info("--filter--name:" + user.getUsername());
                if (user != null) {
                    // Specifies that the user wants to authenticate using a username and password
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    // Bridge between servlet classes and Spring classes.
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // SecurityContext is used to store the details of the currently authenticated user
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception e) {
            log.info("------ catch ------");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer "))
            return bearer.substring("Bearer".length() + 1);
        return null;
    }
}
