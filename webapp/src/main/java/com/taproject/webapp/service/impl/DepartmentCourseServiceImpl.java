package com.taproject.webapp.service.impl;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.taproject.webapp.dto.CourseDto;
import com.taproject.webapp.dto.DepartmentDto;
import com.taproject.webapp.mapper.Mapper;
import com.taproject.webapp.model.CourseEntity;
import com.taproject.webapp.model.DepartmentEntity;
import com.taproject.webapp.repository.CourseReporsitory;
import com.taproject.webapp.repository.DepartmentRepository;
import com.taproject.webapp.service.DepartmentCourseService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DepartmentCourseServiceImpl implements DepartmentCourseService {
    private final DepartmentRepository departmentRepository;
    private final CourseReporsitory courseReporsitory;

    @Override
    public DepartmentDto addDepartment(DepartmentDto department) {
        DepartmentEntity newDepartment = departmentRepository.saveAndFlush(Mapper.mapToEntity(department));
        return Mapper.mapToDto(newDepartment);
    }

    @Override
    public Collection<DepartmentDto> listAllDepartments() {
        Collection<DepartmentEntity> depatmentsList = departmentRepository.findAll();
        return depatmentsList.stream()
                .map((department) -> Mapper.mapToDto(department))
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDto getDepartmentByName(String name) {
        DepartmentEntity department = departmentRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Department with Name [" + name + "] Not Found!"));
        return Mapper.mapToDto(department);
    }

    @Override
    public DepartmentDto getDepartmentById(Integer id) {
        DepartmentEntity department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Department with ID - [" + id + "] Not Found!"));
        return Mapper.mapToDto(department);
    }

    @Override
    public CourseDto addCourse(CourseDto course) {
        CourseEntity newCourse = courseReporsitory.saveAndFlush(Mapper.mapToEntity(course));
        return Mapper.mapToDto(newCourse);
    }

    @Override
    public Collection<CourseDto> listAllCourses() {
        Collection<CourseEntity> courses = courseReporsitory.findAll();
        return courses.stream()
                .map((course) -> Mapper.mapToDto(course))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<CourseDto> listAllCoursesByDepartment(DepartmentDto department) {
        Collection<CourseEntity> couses = courseReporsitory.findByDepatment(
                departmentRepository.getReferenceById(department.getId()));
        return couses.stream()
                .map((course) -> Mapper.mapToDto(course))
                .collect(Collectors.toList());
    }

    @Override
    public CourseDto getCourseByName(String name) {
        CourseEntity course = courseReporsitory.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Course with Name - [" + name + "] Not Found!"));
        return Mapper.mapToDto(course);
    }

    @Override
    public CourseDto getCourseById(Integer id) {
        CourseEntity course = courseReporsitory.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Course with ID - [" + id + "] Not Found!"));
        return Mapper.mapToDto(course);
    }

    @Override
    public CourseDto editCourse(CourseDto course) {
        CourseEntity newCourse = courseReporsitory.save(Mapper.mapToEntity(course));
        return Mapper.mapToDto(newCourse);
    }
}
