package com.example.springsecurityjwt.model;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private final String jwt;
}
