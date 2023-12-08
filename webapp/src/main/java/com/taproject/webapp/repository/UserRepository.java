package com.taproject.webapp.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taproject.webapp.model.RoleEnum;
import com.taproject.webapp.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>{
    Optional<UserEntity> findByEmail(String email);
    Boolean existsByEmail(String email);
    Collection<UserEntity> findByRole(RoleEnum role);
}
