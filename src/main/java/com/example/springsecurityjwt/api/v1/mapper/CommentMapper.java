package com.example.springsecurityjwt.api.v1.mapper;

import com.example.springsecurityjwt.api.v1.DTO.CommentDTO;
import com.example.springsecurityjwt.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    CommentDTO commentToCommentDTO(Comment comment);

    Comment commentDTOToComment(CommentDTO commentDTO);
}
