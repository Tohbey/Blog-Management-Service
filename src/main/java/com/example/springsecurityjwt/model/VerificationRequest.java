package com.example.springsecurityjwt.model;

import lombok.Data;

@Data
public class VerificationRequest {
    private String email;
    private String token;
}
