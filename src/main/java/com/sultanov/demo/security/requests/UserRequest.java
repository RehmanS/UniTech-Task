package com.sultanov.demo.security.requests;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserRequest {
    String pin;
    String password;
}
