package com.taproject.webapp.service.impl;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.taproject.webapp.dto.RegistrationDto;
import com.taproject.webapp.dto.UserDto;
import com.taproject.webapp.mapper.Mapper;
import com.taproject.webapp.model.RoleEnum;
import com.taproject.webapp.model.UserEntity;
import com.taproject.webapp.repository.DepartmentRepository;
import com.taproject.webapp.repository.UserRepository;
import com.taproject.webapp.service.UserService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    public final UserRepository userRepository;
    public final DepartmentRepository departmentRepository;
    public final PasswordEncoder passwordEncoder;

    @Override
    public UserDto save(RegistrationDto registrationDto) {
        UserEntity newUser = Mapper.mapToEntity(registrationDto);
        newUser.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        return Mapper.mapToDto(userRepository.saveAndFlush(newUser));
    }

    @Override
    public UserDto findByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with Email - [" + email + "] Not Found!"));
        return Mapper.mapToDto(user);
    }

    @Override
    public Collection<UserDto> findAll() {
        return userRepository.findAll()
                .stream().map((user) -> Mapper.mapToDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Collection<UserDto> listAllByRole(RoleEnum role) {
        return userRepository.findByRole(role)
                .stream().map((user) -> Mapper.mapToDto(user))
                .collect(Collectors.toList());
    }
}
