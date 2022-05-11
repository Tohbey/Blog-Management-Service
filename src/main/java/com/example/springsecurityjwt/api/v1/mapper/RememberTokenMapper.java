package com.example.springsecurityjwt.api.v1.mapper;

import com.example.springsecurityjwt.api.v1.DTO.RememberTokenDTO;
import com.example.springsecurityjwt.model.RememberToken;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RememberTokenMapper {
    RememberTokenMapper INSTANCE = Mappers.getMapper(RememberTokenMapper.class);

    RememberTokenDTO rememberTokenToRememberTokenDTO(RememberToken rememberToken);

    RememberToken rememberTokenDTOToRememberToken(RememberTokenDTO rememberTokenDTO);
}
