package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.model.AuthenticationRequest;
import com.example.springsecurityjwt.model.AuthenticationResponse;
import com.example.springsecurityjwt.model.VerificationRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    AuthenticationResponse createAuthenticationToken(AuthenticationRequest authenticationRequest) throws Exception;

    void verifyUser(VerificationRequest verificationRequest) throws Exception;

    void changePassword();

    void recover();

    void reset();

    void resetPassword();
}
