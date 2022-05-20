package com.example.springsecurityjwt.controller;

import com.example.springsecurityjwt.api.v1.DTO.PostDTO;
import com.example.springsecurityjwt.api.v1.DTO.PostListDTO;
import com.example.springsecurityjwt.dtos.ResponseObject;
import com.example.springsecurityjwt.model.Post;
import com.example.springsecurityjwt.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(PostController.BASE_URL)
public class PostController {
    public static final  String BASE_URL = "/api/v1/post";
    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ResponseObject> getAllPost(){
        ResponseObject object = new ResponseObject();
        try {
            List<PostDTO> postDTOList = postService.getAllPost();
            object.setData(new PostListDTO(postDTOList));
            object.setValid(true);
            object.setMessage("Resource Retrieved Successfully");
        }catch (Exception e) {
            object.setValid(false);
            object.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(object);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ResponseObject> createPost(@RequestBody Post post){
        ResponseObject object = new ResponseObject();
        try {
            PostDTO postDTO = postService.save(post);
            object.setData(postDTO);
            object.setValid(true);
            object.setMessage("Resource Created Successfully");
        }catch (Exception e) {
            object.setValid(false);
            object.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return new ResponseEntity<>(object, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseObject> deletePost(@PathVariable long id){
        ResponseObject object = new ResponseObject();
        try {
            postService.delete(id);
            object.setValid(true);
            object.setMessage("Resource Deleted Successfully");
        }catch (Exception e) {
            object.setValid(false);
            object.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(object);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user")
    public ResponseEntity<ResponseObject> getAllPostByUser(){
        ResponseObject object = new ResponseObject();
        try {
            List<PostDTO> postDTOList = postService.getAllPostByUser();
            object.setData(new PostListDTO(postDTOList));
            object.setValid(true);
            object.setMessage("Resource Retrieved Successfully");
        }catch (Exception e) {
            object.setValid(false);
            object.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(object);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseObject> getPostById(@PathVariable long id){
        ResponseObject object = new ResponseObject();
        try {
            Optional<PostDTO> postDTO = postService.getPost(id);
            object.setData(postDTO);
            object.setValid(true);
            object.setMessage("Resource Retrieved Successfully");
        }catch (Exception e) {
            object.setValid(false);
            object.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(object);
    }
}
