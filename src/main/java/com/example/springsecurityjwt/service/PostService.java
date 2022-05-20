package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.api.v1.DTO.PostDTO;
import com.example.springsecurityjwt.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {

    List<PostDTO> getAllPostByUser();

    List<PostDTO> getAllPost();

    Optional<PostDTO> getPost(Long id);

    PostDTO save(Post post) throws Exception;

    void delete(long id);
}
