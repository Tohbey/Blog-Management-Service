package com.example.springsecurityjwt.dao;


import com.example.springsecurityjwt.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostDao extends JpaRepository<Post, Long> {
    List<Post> findAllByUser(Long userId);
}
