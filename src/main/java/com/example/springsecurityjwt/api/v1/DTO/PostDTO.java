package com.example.springsecurityjwt.api.v1.DTO;

import com.example.springsecurityjwt.model.Category;
import com.example.springsecurityjwt.model.Tag;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostDTO {
    private String title;
    private String summary;
    private Boolean publishedAt;
    private Date published;
    private String slug;
    private int comments;
    private String author;
    private List<Tag> tags;
    private List<Category> categories;
    private Date uploaded;

    @JsonProperty("post_url")
    private String postUrl;
}
