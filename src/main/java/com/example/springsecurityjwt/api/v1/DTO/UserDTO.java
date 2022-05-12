package com.example.springsecurityjwt.api.v1.DTO;

import com.example.springsecurityjwt.model.PasswordRetrieve;
import com.example.springsecurityjwt.model.RememberToken;
import com.example.springsecurityjwt.model.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String fullName;
    private String email;
    private List<Role> roles;
    private RememberTokenDTO token;
    private PasswordRetrieveDTO passwordRetrieve;
    private String profile;

    @JsonProperty("user_url")
    private String userUrl;
}
