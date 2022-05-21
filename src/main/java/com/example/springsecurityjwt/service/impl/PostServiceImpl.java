package com.example.springsecurityjwt.service.impl;

import com.example.springsecurityjwt.api.v1.DTO.PostDTO;
import com.example.springsecurityjwt.api.v1.DTO.UserDTO;
import com.example.springsecurityjwt.api.v1.mapper.PostMapper;
import com.example.springsecurityjwt.controller.PostController;
import com.example.springsecurityjwt.dao.CategoryDao;
import com.example.springsecurityjwt.dao.PostDao;
import com.example.springsecurityjwt.dao.TagDao;
import com.example.springsecurityjwt.exceptions.NotFoundException;
import com.example.springsecurityjwt.model.Category;
import com.example.springsecurityjwt.model.Post;
import com.example.springsecurityjwt.model.Tag;
import com.example.springsecurityjwt.model.User;
import com.example.springsecurityjwt.service.AuthenticationService;
import com.example.springsecurityjwt.service.PostService;
import com.example.springsecurityjwt.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private final PostDao postDao;
    private final CategoryDao categoryDao;
    private final TagDao tagDao;
    private final PostMapper postMapper;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public PostServiceImpl(PostDao postDao, PostMapper postMapper
            , UserService userService,
                           AuthenticationService authenticationService,
                           CategoryDao categoryDao, TagDao tagDao){
        this.postDao = postDao;
        this.postMapper = postMapper;
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.categoryDao = categoryDao;
        this.tagDao = tagDao;
    }

    @Override
    public PostDTO quotePost(long parentId, Post childrenPost) throws Exception {
        Optional<Post> parentPost = postDao.findById(parentId);
        if(parentPost.isEmpty()){
            throw new NotFoundException("Parent Post Not Found. for ID value " + parentId);
        }
        Optional<Post> checkChildrenPost = this.postDao.findPostsByTitle(childrenPost.getTitle());
        if (checkChildrenPost.isPresent()) {
            throw new Exception("A post with this title already exist " + checkChildrenPost.get().getTitle());
        }
        Optional<User> user = authenticationService.getCurrentUser();

        childrenPost.setParent(parentPost.get());
        childrenPost.setUser(user.get());
        this.categoryDao.saveAll(childrenPost.getCategories());
        this.tagDao.saveAll(childrenPost.getTags());

        Post savedChildrenPost = this.postDao.save(childrenPost);

        PostDTO returnDTO = postMapper.postToPostDTO(savedChildrenPost);

        returnDTO.setAuthor(user.get().getSurname()+" "+user.get().getOtherNames());
        returnDTO.setPostUrl(getPostUrl(savedChildrenPost.getId()));
        returnDTO.setParentPostUrl(getPostUrl(parentPost.get().getId()));
        returnDTO.setChildrenPostUrl(savedChildrenPost.getChildren()
                .stream().map(
                        post -> {
                            return getPostUrl(post.getId());
                        }
                ).collect(Collectors.toList()));

        return returnDTO;
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
                            if(post.getParent() != null){
                                postDTO.setParentPostUrl(getPostUrl(post.getParent().getId()));
                            }else{
                                postDTO.setParentPostUrl("");
                            }
//                            postDTO.setParentPostUrl(getPostUrl(Optional.of(post.getParent().getId()).orElse(Long.valueOf("0"))));
                            postDTO.setChildrenPostUrl(post.getChildren().stream().map(
                                    post1 -> {
                                        return getPostUrl(post1.getId());
                                    }
                            ).collect(Collectors.toList()));
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
                            if(post.getParent() != null){
                                postDTO.setParentPostUrl(getPostUrl(post.getParent().getId()));
                            }else{
                                postDTO.setParentPostUrl("");
                            }
//                            postDTO.setParentPostUrl(getPostUrl(Optional.of(post.getParent().getId()).orElse(Long.valueOf("0"))));
                            postDTO.setChildrenPostUrl(post.getChildren().stream().map(
                                    post1 -> {
                                        return getPostUrl(post1.getId());
                                    }
                            ).collect(Collectors.toList()));
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
                    if(post.get().getParent() != null){
                        postDTO.setParentPostUrl(getPostUrl(post.get().getParent().getId()));
                    }else{
                        postDTO.setParentPostUrl("");
                    }
//                    postDTO.setParentPostUrl(getPostUrl(Optional.of(post.get().getParent().getId()).orElse(Long.valueOf("0"))));
                    postDTO.setChildrenPostUrl(post.get().getChildren().stream().map(
                            post1 -> {
                                return getPostUrl(post1.getId());
                            }
                    ).collect(Collectors.toList()));
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

        List<Category> categories = this.categoryDao.saveAll(post.getCategories());
        List<Tag> tags = this.tagDao.saveAll(post.getTags());

        Post savedPost = this.postDao.save(post);

        PostDTO returnDTO = postMapper.postToPostDTO(savedPost);

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
