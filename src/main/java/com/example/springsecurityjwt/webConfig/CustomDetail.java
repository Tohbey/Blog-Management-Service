package com.example.springsecurityjwt.webConfig;

import com.example.springsecurityjwt.model.User;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CustomDetail implements UserDetails {
    private User user;

    @Override
    public List<SimpleGrantedAuthority> getAuthorities() {
        return user.getRoles().stream().map(r -> new
                SimpleGrantedAuthority("ROLE_" + r)).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.getIsActive() == 1;
    }
}
