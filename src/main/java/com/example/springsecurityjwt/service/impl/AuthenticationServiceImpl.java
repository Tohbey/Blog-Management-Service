package com.example.springsecurityjwt.service.impl;

import com.example.springsecurityjwt.dao.UserDao;
import com.example.springsecurityjwt.jwt.JwtUtils;
import com.example.springsecurityjwt.model.AuthenticationRequest;
import com.example.springsecurityjwt.model.AuthenticationResponse;
import com.example.springsecurityjwt.model.User;
import com.example.springsecurityjwt.model.VerificationRequest;
import com.example.springsecurityjwt.service.AuthenticationService;
import com.example.springsecurityjwt.webConfig.CustomDetail;
import com.example.springsecurityjwt.webConfig.CustomDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtTokenUtil;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CustomDetailService userDetailsService;

    @Override
    public AuthenticationResponse createAuthenticationToken(AuthenticationRequest authenticationRequest) throws Exception {
        this.authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

        final CustomDetail userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getEmail());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return new AuthenticationResponse(token);
    }

    @Override
    public void verifyUser(VerificationRequest verificationRequest)  throws Exception{

    }

    @Override
    public void changePassword() {

    }

    @Override
    public void recover() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void resetPassword() {

    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}
