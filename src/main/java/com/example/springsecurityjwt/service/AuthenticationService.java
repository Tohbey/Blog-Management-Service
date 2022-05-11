package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.api.v1.DTO.UserDTO;
import com.example.springsecurityjwt.model.AuthenticationRequest;
import com.example.springsecurityjwt.model.AuthenticationResponse;
import com.example.springsecurityjwt.model.VerificationRequest;

import java.util.Optional;

public interface AuthenticationService {

    AuthenticationResponse createAuthenticationToken(AuthenticationRequest authenticationRequest) throws Exception;

    Optional<UserDTO> verifyUser(VerificationRequest verificationRequest) throws Exception;

    void changePassword();

    void recover();

    void reset();

    void resetPassword();
}
