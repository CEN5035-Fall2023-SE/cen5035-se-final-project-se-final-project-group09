package com.taproject.webapp.service;

import java.util.Collection;

import com.taproject.webapp.dto.RegistrationDto;
import com.taproject.webapp.dto.UserDto;
import com.taproject.webapp.model.RoleEnum;

public interface UserService {
    UserDto save(RegistrationDto user);
    UserDto findByEmail(String email);
    Collection<UserDto> findAll();
    Boolean existsByEmail(String email);
    Collection<UserDto> listAllByRole(RoleEnum role);
}
