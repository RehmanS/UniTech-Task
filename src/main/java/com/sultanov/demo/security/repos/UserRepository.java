package com.sultanov.demo.security.repos;


import com.sultanov.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByPin(String pin);

}
