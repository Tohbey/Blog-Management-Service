package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.api.v1.DTO.UserDTO;
import com.example.springsecurityjwt.model.*;

import java.util.Optional;

public interface AuthenticationService {

    AuthenticationResponse createAuthenticationToken(AuthenticationRequest authenticationRequest) throws Exception;

    Optional<UserDTO> verifyUser(VerificationRequest verificationRequest) throws Exception;

    Boolean checkIfValidOldPassword(User user, String oldPassword);

    UserDTO changePassword(ForgotPasswordRequest forgotPasswordRequest) throws Exception;

    UserDTO recover(RecoverRequest recoverRequest);

    Optional<User> reset(String email, String token) throws Exception;

    UserDTO resetPassword(ResetPasswordRequest resetPasswordRequest) throws Exception;
}
