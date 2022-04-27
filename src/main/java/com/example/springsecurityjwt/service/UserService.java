package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.dao.UserDao;
import com.example.springsecurityjwt.exceptions.NotFoundException;
import com.example.springsecurityjwt.model.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserService {
    private UserDao userDao;

    public UserService(UserDao userDao){
        this.userDao = userDao;
    }

    public Collection<User> getAllUser(){
        return this.userDao.findAll();
    }

    public User getUser(Long id){
        Optional<User> user =  this.userDao.findById(id);

        if(user.isEmpty()){
            throw new NotFoundException("User Not Found. for ID value" +id);
        }

        return user.get();
    }

    public void save(User user){
        Optional<User> currentUser = this.userDao.findUserByEmail(user.getEmail());
        if(currentUser.isPresent()){
            this.userDao.save(user);
        }
        this.userDao.save(user);
    }

    public void delete(long id){
        this.userDao.deleteById(id);
    }

    public void update(User user, long id){
        Optional<User> currentUser = this.userDao.findById(id);
        if(currentUser.isEmpty()){
            throw new NotFoundException("User Not Found. for ID value" +id);
        }
        this.userDao.save(user);
    }
}
