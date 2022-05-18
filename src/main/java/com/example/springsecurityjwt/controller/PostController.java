package com.example.springsecurityjwt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PostController.BASE_URL)
public class PostController {
    public static final  String BASE_URL = "/api/v1/post";
}
