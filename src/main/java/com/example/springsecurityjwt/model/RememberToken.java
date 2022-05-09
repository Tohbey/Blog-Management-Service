package com.example.springsecurityjwt.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
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

    @OneToOne(mappedBy = "token")
    private User user;
}
