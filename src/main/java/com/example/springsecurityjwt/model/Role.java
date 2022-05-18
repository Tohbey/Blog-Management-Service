package com.example.springsecurityjwt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Data
@NoArgsConstructor
public class Role extends BaseEntity {

    public Role(long id) {
        super(id);
    }

    private String role;
}