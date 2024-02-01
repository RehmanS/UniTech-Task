package com.sultanov.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "users")
@Data
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String pin;
    String password;

    @OneToMany(mappedBy = "accountNumber")
    List<Account> accounts;
}
