package com.example.springsecurityjwt.controller;

import com.example.springsecurityjwt.api.v1.DTO.UserDTO;
import com.example.springsecurityjwt.dtos.ResponseObject;
import com.example.springsecurityjwt.model.AuthenticationRequest;
import com.example.springsecurityjwt.model.AuthenticationResponse;
import com.example.springsecurityjwt.model.ForgotPasswordRequest;
import com.example.springsecurityjwt.model.VerificationRequest;
import com.example.springsecurityjwt.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    public void recover(){}

    public void reset(){}

    public void resetPassword(){}
}
