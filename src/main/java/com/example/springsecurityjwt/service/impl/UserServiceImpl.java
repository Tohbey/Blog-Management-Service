package com.example.springsecurityjwt.service.impl;

import com.example.springsecurityjwt.api.v1.DTO.UserDTO;
import com.example.springsecurityjwt.api.v1.mapper.UserMapper;
import com.example.springsecurityjwt.controller.UserController;
import com.example.springsecurityjwt.dao.UserDao;
import com.example.springsecurityjwt.exceptions.NotFoundException;
import com.example.springsecurityjwt.model.User;
import com.example.springsecurityjwt.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private UserMapper userMapper;

    public UserServiceImpl(UserDao userDao, UserMapper userMapper){
        this.userDao = userDao;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDTO> getAllUser(){
        return this.userDao
                .findAll().stream().map(
                        user -> {
                            UserDTO userDTO = userMapper.userToUserDTO(user);
                            userDTO.setUserUrl(getUserUrl(user.getId()));
                            userDTO.setFullName(returnUserFullName(user));

                            return userDTO;
                        }
                ).collect(Collectors.toList());
    }
    @Override
    public Optional<UserDTO> getUser(Long id){
        Optional<User> user =  this.userDao.findById(id);

        if(user.isEmpty()){
            throw new NotFoundException("User Not Found. for ID value" +id);
        }

       return user.map(userMapper::userToUserDTO)
               .map(userDTO -> {
                   userDTO.setUserUrl(getUserUrl(user.get().getId()));
                   userDTO.setFullName(returnUserFullName(user.get()));
                   return userDTO;
               });
    }
    @Override
    public UserDTO save(User user){
        return saveAndReturnDTO(user);
    }

    @Override
    public void delete(long id){
        this.userDao.deleteById(id);
    }

    @Override
    public Optional<UserDTO> update(User user, long id){
        Optional<User> currentUser = this.userDao.findById(id);
        if(currentUser.isEmpty()){
            throw new NotFoundException("User Not Found. for ID value" +id);
        }

        return currentUser.map(user1 -> {
            if(user.getEmail() != null){
                user1.setEmail(user.getEmail());
            }


            return saveAndReturnDTO(user1);
        });
    }

    private UserDTO saveAndReturnDTO(User user){
        User savedUser = this.userDao.save(user);

        UserDTO returnDTO = userMapper.userToUserDTO(savedUser);

        returnDTO.setUserUrl(getUserUrl(savedUser.getId()));
        returnDTO.setFullName(returnUserFullName(savedUser));

        return returnDTO;
    }

    private String getUserUrl(long id){
        return UserController.BASE_URL + "/"+id;
    }

    private String returnUserFullName(User user){
        return user.getSurname() +" "+user.getOtherNames();
    }
}
