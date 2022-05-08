package com.example.springsecurityjwt.api.v1.mapper;

import com.example.springsecurityjwt.api.v1.DTO.PostDTO;
import com.example.springsecurityjwt.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    PostDTO postToPostDTO(Post post);

    Post postDTOToPost(PostDTO postDTO);
}
