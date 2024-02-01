package com.sultanov.demo.service;

import com.sultanov.demo.entity.User;
import com.sultanov.demo.security.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getOneUserByUserPin(String pin) {
        return userRepository.findUserByPin(pin);
    }
}