package com.example.springsecurityjwt.service.impl;

import com.example.springsecurityjwt.api.v1.DTO.PostDTO;
import com.example.springsecurityjwt.api.v1.DTO.UserDTO;
import com.example.springsecurityjwt.api.v1.mapper.PostMapper;
import com.example.springsecurityjwt.controller.PostController;
import com.example.springsecurityjwt.controller.UserController;
import com.example.springsecurityjwt.dao.PostDao;
import com.example.springsecurityjwt.exceptions.NotFoundException;
import com.example.springsecurityjwt.model.Post;
import com.example.springsecurityjwt.model.User;
import com.example.springsecurityjwt.service.AuthenticationService;
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
    private final AuthenticationService authenticationService;

    public PostServiceImpl(PostDao postDao, PostMapper postMapper, UserService userService, AuthenticationService authenticationService){
        this.postDao = postDao;
        this.postMapper = postMapper;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @Override
    public List<PostDTO> getAllPostByUser() {
        Optional<User> user  = authenticationService.getCurrentUser();
        return this.postDao
                .findPostsByUser(user.get()).stream().map(
                        post -> {
                            PostDTO postDTO = postMapper.postToPostDTO(post);
                            postDTO.setPostUrl(getPostUrl(post.getId()));
                            postDTO.setAuthor(user.get().getSurname()+" "+user.get().getOtherNames());
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
                            postDTO.setPostUrl(getPostUrl(post.getId()));
                            postDTO.setAuthor(returnAuthorDetails(post.getUser().getId()));

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
    public PostDTO save(Post post) throws Exception {
        Optional<Post> checkPost = this.postDao.findPostsByTitle(post.getTitle());
        if (checkPost.isPresent()) {
            throw new Exception("A post with this title already exist " + checkPost.get().getTitle());
        }
        Optional<User> user = authenticationService.getCurrentUser();
        post.setUser(user.get());

        Post savedPost = this.postDao.save(post);

        PostDTO returnDTO = postMapper.postToPostDTO(post);

        returnDTO.setAuthor(user.get().getSurname()+" "+user.get().getOtherNames());
        returnDTO.setPostUrl(getPostUrl(savedPost.getId()));

        return returnDTO;
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
