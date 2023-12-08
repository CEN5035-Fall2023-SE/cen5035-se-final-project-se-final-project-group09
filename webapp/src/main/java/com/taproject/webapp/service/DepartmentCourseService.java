package com.taproject.webapp.service;

import java.util.Collection;

import com.taproject.webapp.dto.CourseDto;
import com.taproject.webapp.dto.DepartmentDto;

public interface DepartmentCourseService {
    DepartmentDto addDepartment(DepartmentDto department);
    Collection<DepartmentDto> listAllDepartments();
    DepartmentDto getDepartmentByName(String name);
    DepartmentDto getDepartmentById(Integer id);

    CourseDto addCourse(CourseDto course);
    CourseDto editCourse(CourseDto course);
    Collection<CourseDto> listAllCourses();
    Collection<CourseDto> listAllCoursesByDepartment(DepartmentDto department);
    CourseDto getCourseByName(String name);
    CourseDto getCourseById(Integer id);
}
