package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.api.v1.DTO.UserDTO;

import com.example.springsecurityjwt.model.User;

import java.util.List;
import java.util.Optional;


public interface UserService {

    List<UserDTO> getAllUser();

    Optional<UserDTO> getUser(Long id);

    UserDTO save(User user);

    void delete(long id);

    Optional<UserDTO> update(User user, long id);
}
