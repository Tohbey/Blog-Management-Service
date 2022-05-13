package com.example.springsecurityjwt.model;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String oldPassword;
    private String newPassword;
}
