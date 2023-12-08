package com.taproject.webapp.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.taproject.webapp.mapper.Mapper;
import com.taproject.webapp.model.AuthUser;
import com.taproject.webapp.model.UserEntity;
import com.taproject.webapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthUserServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity authUserEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        AuthUser authUser = new AuthUser(
                Mapper.mapToDto(authUserEntity),
                authUserEntity.getEmail(),
                authUserEntity.getPassword(),
                authUserEntity.getRole().getAuthorities());
        return authUser;
    }
}
