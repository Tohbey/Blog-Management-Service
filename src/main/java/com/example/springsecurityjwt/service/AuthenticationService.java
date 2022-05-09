package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.model.AuthenticationRequest;
import com.example.springsecurityjwt.model.AuthenticationResponse;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    AuthenticationResponse createAuthenticationToken(AuthenticationRequest authenticationRequest) throws Exception;

    void verifyUser();

    void changePassword();

    void recover();

    void reset();

    void resetPassword();
}
