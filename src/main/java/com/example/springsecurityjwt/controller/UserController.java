package com.example.springsecurityjwt.controller;

import com.example.springsecurityjwt.api.v1.DTO.UserDTO;
import com.example.springsecurityjwt.api.v1.DTO.UserListDTO;
import com.example.springsecurityjwt.model.User;
import com.example.springsecurityjwt.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(UserController.BASE_URL)
public class UserController {

    private final UserService userService;
    public static final String BASE_URL = "/api/v1/user";

    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(method = RequestMethod.GET)
    public UserListDTO getAllUsers(){
        return new UserListDTO(this.userService.getAllUser());
    }

    @RequestMapping(value ="/{id}" ,method = RequestMethod.GET)
    public Optional<UserDTO> getUserById(@PathVariable long id){
        return this.userService.getUser(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public UserDTO insertUser(@RequestBody User user){
        String pwd = user.getPassword();
        String encryptPwd = this.passwordEncoder.encode(pwd);
        user.setPassword(encryptPwd);
        return this.userService.save(user);
    }

    @RequestMapping(value ="/{id}" ,method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable long id){
        this.userService.delete(id);
    }

    @RequestMapping(value ="/{id}" ,method = RequestMethod.PUT)
    public Optional<UserDTO> updateUser(@PathVariable long id, @RequestBody User user){
        return this.userService.update(user, id);
    }
}
