package com.example.springsecurityjwt.api.v1.mapper;

import com.example.springsecurityjwt.api.v1.DTO.PasswordRetrieveDTO;
import com.example.springsecurityjwt.model.PasswordRetrieve;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PasswordRememberTokenMapper {
    PasswordRememberTokenMapper INSTANCE = Mappers.getMapper(PasswordRememberTokenMapper.class);

    PasswordRetrieveDTO passwordRetrieveToPasswordRetrieveDTO(PasswordRetrieve passwordRetrieve);

    PasswordRetrieve passwordRetrieveDTOToPasswordRetrieve(PasswordRetrieveDTO passwordRetrieveDTO);
}
