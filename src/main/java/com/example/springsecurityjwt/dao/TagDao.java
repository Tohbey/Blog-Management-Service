package com.example.springsecurityjwt.dao;

import com.example.springsecurityjwt.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagDao extends JpaRepository<Tag, Long> {
}
