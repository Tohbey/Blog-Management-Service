package com.example.springsecurityjwt.dao;

import com.example.springsecurityjwt.model.RememberToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RememberTokenDao extends JpaRepository<RememberToken, Long> {
}
