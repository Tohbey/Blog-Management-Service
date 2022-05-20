package com.example.springsecurityjwt.dao;


import com.example.springsecurityjwt.model.Post;
import com.example.springsecurityjwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostDao extends JpaRepository<Post, Long> {
    List<Post> findPostsByUser(User user);

    Optional<Post> findPostsByTitle(String title);
}
