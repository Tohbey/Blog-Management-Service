package com.example.springsecurityjwt.dao;

import com.example.springsecurityjwt.model.PasswordRetrieve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRetrieveDao extends JpaRepository<PasswordRetrieve, Long> {
}
