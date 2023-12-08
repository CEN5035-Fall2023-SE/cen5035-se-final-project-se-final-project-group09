package com.taproject.webapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taproject.webapp.model.CourseEntity;
import com.taproject.webapp.model.DepartmentEntity;

import java.util.List;


@Repository
public interface CourseReporsitory extends JpaRepository<CourseEntity, Integer>{
    Optional<CourseEntity> findByName(String name);
    List<CourseEntity> findByDepatment(DepartmentEntity depatment);
}
