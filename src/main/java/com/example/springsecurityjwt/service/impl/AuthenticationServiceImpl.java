package com.example.springsecurityjwt.service.impl;

import com.example.springsecurityjwt.api.v1.DTO.UserDTO;
import com.example.springsecurityjwt.api.v1.mapper.UserMapper;
import com.example.springsecurityjwt.dao.RememberTokenDao;
import com.example.springsecurityjwt.dao.UserDao;
import com.example.springsecurityjwt.exceptions.NotFoundException;
import com.example.springsecurityjwt.jwt.JwtUtils;
import com.example.springsecurityjwt.model.*;
import com.example.springsecurityjwt.service.AuthenticationService;
import com.example.springsecurityjwt.service.UserService;
import com.example.springsecurityjwt.webConfig.CustomDetail;
import com.example.springsecurityjwt.webConfig.CustomDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private RememberTokenDao rememberTokenDao;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
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
    public Optional<UserDTO> verifyUser(VerificationRequest verificationRequest) throws Exception {
        //find user
        Optional<User> user = userDao.findUserByEmail(verificationRequest.getEmail());
        if(user.isEmpty()){
            throw new NotFoundException("User Not Found. for ID value " +verificationRequest.getEmail());
        }

        //find token
        Optional<RememberToken> rememberToken = rememberTokenDao.findRememberTokenByToken(verificationRequest.getToken());
        if(rememberToken.isEmpty()){
            throw new NotFoundException("User Not Found. for ID value " +verificationRequest.getEmail());
        }

        //validate the token
        if(!user.get().getToken().getToken().equals(rememberToken.get().getToken())){
            throw new Exception("Incorrect Token");
        }

        //update user
        user.get().setIsActive(1);
        user.get().setToken(null);

        rememberTokenDao.deleteById(rememberToken.get().getId());

        return userService.update(user.get(), user.get().getId());
    }

    @Override
    public Boolean checkIfValidOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public UserDTO changePassword(ForgotPasswordRequest forgotPasswordRequest) throws Exception {
        CustomDetail userDetail = (CustomDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetail.getUsername();
        Optional<User> user = userDao.findUserByEmail(email);
        if(user.isEmpty()){
            throw new NotFoundException("User Not Found. for ID value " +email);
        }
        if(!checkIfValidOldPassword(user.get(), forgotPasswordRequest.getOldPassword())){
            throw new Exception("Invalid Old Password");
        }

        String pwd = forgotPasswordRequest.getNewPassword();
        String encryptPwd = passwordEncoder.encode(pwd);
        user.get().setPassword(encryptPwd);

        User savedUser = user.map(user1->{
            user1.setPassword(user.get().getPassword());
            return userDao.save(user1);
        }).orElseGet(() -> {
            return userDao.save(user.get());
        });

        return userMapper.userToUserDTO(savedUser);
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
