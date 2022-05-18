package com.example.springsecurityjwt.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class PasswordRetrieve extends BaseEntity {

    private PasswordRetrieve(long id) {
        super(id);
    }

    private String resetPasswordToken;
    @Column(name = "reset_password_expired_at")
    private Date resetPasswordExpires;

    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
