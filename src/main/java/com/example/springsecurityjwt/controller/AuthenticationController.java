package com.example.springsecurityjwt.controller;

import com.example.springsecurityjwt.api.v1.DTO.UserDTO;
import com.example.springsecurityjwt.dtos.ResponseObject;
import com.example.springsecurityjwt.model.*;
import com.example.springsecurityjwt.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(AuthenticationController.BASE_URL)
public class AuthenticationController {
    public static final String BASE_URL= "/api/v1/auth";

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ResponseObject> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        ResponseObject responseObject = new ResponseObject();
        try{
            AuthenticationResponse response = this.authenticationService.createAuthenticationToken(authenticationRequest);
            responseObject.setData(response);
            responseObject.setValid(true);
            responseObject.setMessage("Login Successfully");
        }catch (Exception e){
            responseObject.setValid(false);
            responseObject.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseObject);
    }


    @RequestMapping(method = RequestMethod.PATCH,value = "/verify")
    public ResponseEntity<ResponseObject> verifyUser(@RequestBody VerificationRequest verificationRequest){
        ResponseObject responseObject = new ResponseObject();
        try{
            Optional<UserDTO> userDTO = authenticationService.verifyUser(verificationRequest);
            responseObject.setData(userDTO);
            responseObject.setValid(true);
            responseObject.setMessage("Verification Successful");
        }catch (Exception e){
            responseObject.setValid(false);
            responseObject.setMessage(e.getMessage());
            e.printStackTrace();
        }

        return ResponseEntity.ok().body(responseObject);
    }

    @RequestMapping(method = RequestMethod.PATCH,value = "/change-password")
    public ResponseEntity<ResponseObject> changePassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest){
        ResponseObject responseObject = new ResponseObject();
        try {
            UserDTO userDTO = authenticationService.changePassword(forgotPasswordRequest);
            responseObject.setData(userDTO);
            responseObject.setValid(true);
            responseObject.setMessage("Password Updated Successful");
        }catch (Exception e){
            responseObject.setValid(false);
            responseObject.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseObject);
    }
    @RequestMapping(method = RequestMethod.POST,value = "/reset")
    public ResponseEntity<ResponseObject> recover(@RequestBody RecoverRequest recoverRequest){
        ResponseObject responseObject = new ResponseObject();
        try {
            UserDTO userDTO = authenticationService.recover(recoverRequest);
            responseObject.setData(userDTO);
            responseObject.setValid(true);
            responseObject.setMessage("User Recover Process Started Successful");
        }catch (Exception e){
            responseObject.setValid(false);
            responseObject.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseObject);
    }
    @RequestMapping(method = RequestMethod.GET,value = "/forgot/{email}/{token}")
    public ResponseEntity<ResponseObject> reset(@PathVariable String email, @PathVariable String token){
        ResponseObject responseObject = new ResponseObject();
        try {
            Optional<User> user = authenticationService.reset(email, token);
            responseObject.setData(user);
            responseObject.setValid(true);
            responseObject.setMessage("User And Token Verified Successful");
        }catch (Exception e){
            responseObject.setValid(false);
            responseObject.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseObject);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/reset-password")
    public ResponseEntity<ResponseObject> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest){
        ResponseObject responseObject = new ResponseObject();
        try {
            UserDTO user = authenticationService.resetPassword(resetPasswordRequest);
            responseObject.setData(user);
            responseObject.setValid(true);
            responseObject.setMessage("User Password Updated Successful");
        }catch (Exception e){
            responseObject.setValid(false);
            responseObject.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(responseObject);
    }
}
