package com.taproject.webapp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.taproject.webapp.dto.CourseDto;
import com.taproject.webapp.dto.DepartmentDto;
import com.taproject.webapp.model.RoleEnum;
import com.taproject.webapp.service.DepartmentCourseService;
import com.taproject.webapp.service.UserService;

@Controller
public class DepartmentCourseController {
    private final DepartmentCourseService departmentCourseService;
    private final UserService userService;

    public DepartmentCourseController(DepartmentCourseService departmentCourseService, UserService userService) {
        this.departmentCourseService = departmentCourseService;
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole({'ADMIN', 'DEPARTMENT_STAFF', 'COMMITTEE_MEMBER'})")
    @GetMapping("/courses")
    public String manageCourses(Model model) {
        model.addAttribute("title", "Department - Course Management");
        model.addAttribute("departments", departmentCourseService.listAllDepartments());
        model.addAttribute("courses", departmentCourseService.listAllCourses());
        model.addAttribute("instructors", userService.listAllByRole(RoleEnum.INSTRUCTOR));
        model.addAttribute("newDepartment", DepartmentDto.builder().build());
        model.addAttribute("newCourse", CourseDto.builder().build());
        return "management/course";
    }

    @PreAuthorize("hasAnyRole({'ADMIN', 'DEPARTMENT_STAFF', 'COMMITTEE_MEMBER'})")
    @PostMapping("/addDepartment")
    public String addDepartment(
        @ModelAttribute(name = "newDepartment") DepartmentDto department,
        Model model
    ) {
        departmentCourseService.addDepartment(department);
        return "redirect:/courses";
    }

    @PreAuthorize("hasAnyRole({'ADMIN', 'DEPARTMENT_STAFF', 'COMMITTEE_MEMBER'})")
    @PostMapping("/addCourse")
    public String addCourse(
        @ModelAttribute(name = "newCourse") CourseDto course,
        Model model
    ) {
        departmentCourseService.addCourse(course);
        return "redirect:/courses";
    }

    @PreAuthorize("hasAnyRole({'ADMIN', 'DEPARTMENT_STAFF', 'COMMITTEE_MEMBER'})")
    @PostMapping("/editCourse")
    public String editCourse(@ModelAttribute CourseDto course) {
        if (course.getInstructor().getId() == null) {
            course.setInstructor(null);
        }
        departmentCourseService.editCourse(course);
        return "redirect:/courses";
    }
}
