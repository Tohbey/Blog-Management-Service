package com.example.springsecurityjwt.service.impl;

import com.example.springsecurityjwt.api.v1.DTO.PostDTO;
import com.example.springsecurityjwt.api.v1.DTO.UserDTO;
import com.example.springsecurityjwt.api.v1.mapper.PostMapper;
import com.example.springsecurityjwt.controller.PostController;
import com.example.springsecurityjwt.controller.UserController;
import com.example.springsecurityjwt.dao.PostDao;
import com.example.springsecurityjwt.exceptions.NotFoundException;
import com.example.springsecurityjwt.model.Post;
import com.example.springsecurityjwt.service.PostService;
import com.example.springsecurityjwt.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private final PostDao postDao;
    private final PostMapper postMapper;
    private final UserService userService;

    public PostServiceImpl(PostDao postDao, PostMapper postMapper, UserService userService){
        this.postDao = postDao;
        this.postMapper = postMapper;
        this.userService = userService;
    }

    @Override
    public List<PostDTO> getAllPostByUser(Long userId) {
        return this.postDao
                .findAllByUser(userId).stream().map(
                        post -> {
                            PostDTO postDTO = postMapper.postToPostDTO(post);
                            return postDTO;
                        }
                ).collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> getAllPost() {
        return this.postDao
                .findAll().stream().map(
                        post -> {
                            PostDTO postDTO = postMapper.postToPostDTO(post);
                            return postDTO;
                        }
                ).collect(Collectors.toList());
    }

    @Override
    public Optional<PostDTO> getPost(Long id) {
        Optional<Post> post = this.postDao.findById(id);

        if (post.isEmpty()) {
            throw new NotFoundException("Post Not Found. for ID value " + id);
        }
        return post.map(postMapper::postToPostDTO)
                .map(postDTO -> {
                    postDTO.setPostUrl(getPostUrl(post.get().getId()));
                    postDTO.setAuthor(returnAuthorDetails(post.get().getUser().getId()));
                    return postDTO;
                });
    }

    @Override
    public PostDTO save(Post post) {
        return null;
    }

    @Override
    public void delete(long id) {
        this.postDao.deleteById(id);
    }

    private String getPostUrl(long id) {
        return PostController.BASE_URL + "/" + id;
    }

    private String returnAuthorDetails(long id){
        Optional<UserDTO> userDTO = userService.getUser(id);

        return userDTO.get().getFullName();
    }
}
