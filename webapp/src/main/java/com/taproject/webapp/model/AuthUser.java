package com.taproject.webapp.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.taproject.webapp.dto.UserDto;

import lombok.Getter;

@Getter
public class AuthUser extends User{
    private final UserDto userDto;

    public AuthUser(UserDto dto, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userDto = dto;
    }
}
