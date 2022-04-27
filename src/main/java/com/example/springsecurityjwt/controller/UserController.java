package com.example.springsecurityjwt.controller;

import com.example.springsecurityjwt.model.User;
import com.example.springsecurityjwt.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Collection<User> getAllUsers(){
        return this.userService.getAllUser();
    }

    @RequestMapping(value ="/{id}" ,method = RequestMethod.GET)
    public User getUserById(@PathVariable long id){
        return this.userService.getUser(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void insertUser(@RequestBody User user){
        String pwd = user.getPassword();
        String encryptPwd = this.passwordEncoder.encode(pwd);
        user.setPassword(encryptPwd);
        this.userService.save(user);
    }

    @RequestMapping(value ="/{id}" ,method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable long id){
        this.userService.delete(id);
    }

    @RequestMapping(value ="/{id}" ,method = RequestMethod.PUT)
    public void updateUser(@PathVariable long id, @RequestBody User user){
        this.userService.update(user, id);
    }
}
