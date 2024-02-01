package com.sultanov.demo.service;

import com.sultanov.demo.entity.User;
import com.sultanov.demo.security.repos.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @Test
    void testGetAllUsers() {
        List<User> mockUsers = Arrays.asList(new User(1L, "mockPin", "12345", null)
                , new User(1L, "mockPin1", "54321", null));
        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> result = userService.getAllUsers();
        assertEquals(mockUsers, result);
    }

    @Test
    void testCreateUser() {
        User newUser = new User(1L, "mockPin", "12345", null);

        when(userRepository.save(newUser)).thenReturn(newUser);

        User result = userService.createUser(newUser);
        assertEquals(newUser, result);
    }

    @Test
    void testGetOneUserByUserPin() {
        String pin = "123";
        User mockUser = new User(123L, "mockPin", "12345", null);

        when(userRepository.findUserByPin(pin)).thenReturn(mockUser);

        User result = userService.getOneUserByUserPin(pin);
        assertEquals(mockUser, result);
    }
}