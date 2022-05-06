package com.example.springsecurityjwt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@Entity
@NoArgsConstructor
public class Category extends BaseEntity{

    public Category(long id){
        super(id);
    }

    private String title;
    private String slug;
}
