package com.example.springsecurityjwt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@Entity
@NoArgsConstructor
public class Role extends BaseEntity{
    private String role;
}