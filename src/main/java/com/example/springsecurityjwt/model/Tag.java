package com.example.springsecurityjwt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@Entity
@NoArgsConstructor
public class Tag extends BaseEntity {

    public Tag(long id) {
        super(id);
    }

    private String title;
    private String slug;
}
