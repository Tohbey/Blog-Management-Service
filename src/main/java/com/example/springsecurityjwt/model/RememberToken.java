package com.example.springsecurityjwt.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class RememberToken extends BaseEntity{

    public RememberToken(long id){
        super(id);
    }

    private String token;
    @Column(name = "expired_at")
    private Date expiredAt;

    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
