package com.example.springsecurityjwt.api.v1.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CommentDTO {
    private String title;
    private Boolean published;
    private Date publishedAt;
    private String detail;
    private Date uploaded;
    private String author;
    private int comments;

    @JsonProperty("comment_url")
    private String commentUrl;
}
