package com.example.springsecurityjwt.service.impl;

import com.example.springsecurityjwt.api.v1.DTO.UserDTO;
import com.example.springsecurityjwt.api.v1.mapper.UserMapper;
import com.example.springsecurityjwt.controller.UserController;
import com.example.springsecurityjwt.dao.RememberTokenDao;
import com.example.springsecurityjwt.dao.UserDao;
import com.example.springsecurityjwt.exceptions.NotFoundException;
import com.example.springsecurityjwt.model.RememberToken;
import com.example.springsecurityjwt.model.User;
import com.example.springsecurityjwt.service.UserService;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final RememberTokenDao rememberTokenDao;

    private UserMapper userMapper;
    final String alphabet = "0123456789ABCDE";
    final int N = alphabet.length();

    public UserServiceImpl(UserDao userDao, RememberTokenDao rememberTokenDao, UserMapper userMapper){
        this.userDao = userDao;
        this.rememberTokenDao = rememberTokenDao;
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

        if(!user.isPresent()){
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
        //save user
        User savedUser = this.userDao.save(user);

        RememberToken rememberToken = new RememberToken();
        rememberToken.setToken(this.generateRandomToken(20));

        //adding 20 minutes to the current time
        Calendar present = Calendar.getInstance();
        long timeInSecs = present.getTimeInMillis();
        Date expiredAt = new Date(timeInSecs + (20*60*1000));

        //save token
        rememberToken.setUser(savedUser);
        rememberToken.setExpiredAt(expiredAt);
        RememberToken savedToken = this.rememberTokenDao.save(rememberToken);

        //add token
        savedUser.setToken(savedToken);

        //update user
        Optional<UserDTO> returnDTO = this.update(savedUser, savedUser.getId());

        if(returnDTO.isPresent()){
            returnDTO.get().setUserUrl(getUserUrl(savedUser.getId()));
            returnDTO.get().setFullName(returnUserFullName(savedUser));
        }

        return returnDTO.get();
    }

    @Override
    public void delete(long id){
        this.userDao.deleteById(id);
    }

    @Override
    public Optional<UserDTO> update(User user, long id){
        Optional<User> currentUser = this.userDao.findById(id);
        if(!currentUser.isPresent()){
            throw new NotFoundException("User Not Found. for ID value" +id);
        }

        return currentUser.map(user1 -> {
            if(user.getEmail() != null){
                user1.setEmail(user.getEmail());
            }

            if(user.getSurname() != null){
                user1.setSurname(user.getSurname());
            }

            if(user.getOtherNames() != null){
                user1.setOtherNames(user.getOtherNames());
            }

            if(user.getPasswordRetrieve() != null){
                user1.setPasswordRetrieve(user.getPasswordRetrieve());
            }

            if(user.getToken() != null){
                user1.setToken(user.getToken());
            }

            if(user.getProfile() != null){
                user1.setProfile(user.getProfile());
            }
            return saveAndReturnDTO(user1);
        });
    }

    private UserDTO saveAndReturnDTO(User user){
        User savedUser = this.userDao.save(user);

        UserDTO returnDTO = userMapper.userToUserDTO(savedUser);

        returnDTO.setFullName(returnUserFullName(savedUser));
        returnDTO.setUserUrl(getUserUrl(savedUser.getId()));

        return returnDTO;
    }

    private String generateRandomToken(int length){
        String token = "";

        Random r = new Random();
        for(int i =0; i<length;i++){
            String s = token + alphabet.charAt(r.nextInt(N));

            token = s;
        }
        return token;
    }
    private String getUserUrl(long id){
        return UserController.BASE_URL + "/"+id;
    }

    private String returnUserFullName(User user){
        return user.getSurname() +" "+user.getOtherNames();
    }
}
