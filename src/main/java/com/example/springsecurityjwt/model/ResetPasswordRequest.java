package com.example.springsecurityjwt.model;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String email;
    private String token;
    private String password;
}
