package com.sultanov.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    Long accountNumber;
    Integer state;
    BigDecimal balance;
    String currencyType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

}
