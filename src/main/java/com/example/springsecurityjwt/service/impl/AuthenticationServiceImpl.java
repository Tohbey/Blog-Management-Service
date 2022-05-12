package com.example.springsecurityjwt.service.impl;

import com.example.springsecurityjwt.api.v1.DTO.PasswordRetrieveDTO;
import com.example.springsecurityjwt.api.v1.DTO.UserDTO;
import com.example.springsecurityjwt.api.v1.mapper.PasswordRememberTokenMapper;
import com.example.springsecurityjwt.api.v1.mapper.UserMapper;
import com.example.springsecurityjwt.controller.UserController;
import com.example.springsecurityjwt.dao.PasswordRetrieveDao;
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

import java.util.Calendar;
import java.util.Date;
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

    @Autowired
    private PasswordRetrieveDao passwordRetrieveDao;

    @Autowired
    private PasswordRememberTokenMapper passwordRememberTokenMapper;

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
            throw new NotFoundException("User Not Found. for EMAIL value " +verificationRequest.getEmail());
        }

        //find token
        Optional<RememberToken> rememberToken = rememberTokenDao.findRememberTokenByToken(verificationRequest.getToken());
        if(rememberToken.isEmpty()){
            throw new NotFoundException("User Not Found. for EMAIL value " +verificationRequest.getEmail());
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
            throw new NotFoundException("User Not Found. for EMAIL value " +email);
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
    public UserDTO recover(RecoverRequest recoverRequest) {
        Optional<User> user = userDao.findUserByEmail(recoverRequest.getEmail());
        if(user.isEmpty()){
            throw new NotFoundException("User Not Found. for EMAIL value " +recoverRequest.getEmail());
        }

        String token = userService.generateRandomToken(20);
        PasswordRetrieve passwordRetrieve = new PasswordRetrieve();
        passwordRetrieve.setResetPasswordToken(token);

        //adding 20 minutes to the current time
        Calendar present = Calendar.getInstance();
        long timeInSecs = present.getTimeInMillis();
        Date expiredAt = new Date(timeInSecs + (20*60*1000));

        //save token
        passwordRetrieve.setUser(user.get());
        passwordRetrieve.setResetPasswordExpires(expiredAt);
        PasswordRetrieve savedPasswordRetrieveToken = passwordRetrieveDao.save(passwordRetrieve);

        //password retrieve token dto
        PasswordRetrieveDTO passwordRetrieveDTO = passwordRememberTokenMapper.passwordRetrieveToPasswordRetrieveDTO(passwordRetrieve);
        passwordRetrieveDTO.setUserURL(getUserUrl(user.get().getId()));

        user.get().setPasswordRetrieve(savedPasswordRetrieveToken);

        Optional<UserDTO> returnDTO = userService.update(user.get(),user.get().getId());

        returnDTO.get().setUserUrl(getUserUrl(user.get().getId()));
        returnDTO.get().setFullName(returnUserFullName(user.get()));
        returnDTO.get().setPasswordRetrieve(passwordRetrieveDTO);

        return returnDTO.get();
    }

    @Override
    public Optional<User> reset(String email, String token) throws Exception {
        Optional<User> user = userDao.findUserByEmail(email);
        if(user.isEmpty()){
            throw new NotFoundException("User Not Found. for EMAIL value " +email);
        }

        Optional<PasswordRetrieve> passwordRetrieve = passwordRetrieveDao.findPasswordRetrieveByResetPasswordToken(token);
        if(passwordRetrieve.isEmpty()){
            throw new NotFoundException("Token Not Found. for TOKEN value " +token);
        }

        if(!user.get().getPasswordRetrieve().getResetPasswordToken().equals(passwordRetrieve.get().getResetPasswordToken())){
            throw new Exception("Incorrect Token");
        }

        return user;
    }

    @Override
    public UserDTO resetPassword(ResetPasswordRequest resetPasswordRequest) throws Exception {
        Optional<User> user = reset(resetPasswordRequest.getEmail(), resetPasswordRequest.getToken());

        String encryptPwd = passwordEncoder.encode(resetPasswordRequest.getPassword());
        user.get().setPassword(encryptPwd);

        Optional<PasswordRetrieve> passwordRetrieve = passwordRetrieveDao.findPasswordRetrieveByResetPasswordToken(resetPasswordRequest.getToken());
        if(passwordRetrieve.isEmpty()){
            throw new NotFoundException("Token Not Found. for TOKEN value " +resetPasswordRequest.getToken());
        }
        passwordRetrieveDao.deleteById(passwordRetrieve.get().getId());

        user.get().setPasswordRetrieve(null);
        User savedUser = user.map(user1 -> {
            user1.setPassword(user.get().getPassword());

            return userDao.save(user1);
        }).orElseGet(() -> {
            return userDao.save(user.get());
        });

        UserDTO userDTO = userMapper.userToUserDTO(savedUser);
        userDTO.setUserUrl(getUserUrl(userDTO.getId()));
        userDTO.setFullName(returnUserFullName(savedUser));

        return userDTO;
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

    private String getUserUrl(long id){
        return UserController.BASE_URL + "/"+id;
    }

    private String returnUserFullName(User user){
        return user.getSurname() +" "+user.getOtherNames();
    }
}
