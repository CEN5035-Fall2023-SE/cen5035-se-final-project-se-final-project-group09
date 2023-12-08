package com.taproject.webapp.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.stream.Stream;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import com.taproject.webapp.dto.ApplicationDto;
import com.taproject.webapp.dto.CourseDto;
import com.taproject.webapp.dto.DepartmentDto;
import com.taproject.webapp.dto.PreviousExperianceDto;
import com.taproject.webapp.dto.RegistrationDto;
import com.taproject.webapp.dto.UserDto;
import com.taproject.webapp.model.ApplicationStatusEnum;
import com.taproject.webapp.model.GenderEnum;
import com.taproject.webapp.model.RoleEnum;
import com.taproject.webapp.service.ApplicationService;
import com.taproject.webapp.service.DepartmentCourseService;
import com.taproject.webapp.service.UserService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {
    public final UserService userService;
    public final DepartmentCourseService departmentCourseService;
    public final ApplicationService applicationService;

    public HomeController(UserService userService, DepartmentCourseService departmentCourseService,
            ApplicationService applicationService) {
        this.userService = userService;
        this.departmentCourseService = departmentCourseService;
        this.applicationService = applicationService;
    }

    @GetMapping("/")
    public String home(HttpServletRequest request) {
        if (request.isUserInRole("APPLICANT")) {
            return "index";
        }
        return "redirect:/application/view";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public String allUsers(Model model) {
        model.addAttribute("pageTitle", "User Management");
        model.addAttribute("users", userService.findAll());
        return "management/users";
    }

    @GetMapping("/error")
    public String errorPage(Model model) {
        model.addAttribute("pageTitle", "ERROR!");
        return "error";
    }

    @GetMapping("/test")
    public String testDbBuild2() throws Exception {
        if (userService.existsByEmail("test@gg.com")) {
            throw new Exception("Test Admin User Exits!");
        }
        Stream.of("Computer Science", "Law", "Civil", "AI", "Electronics").forEach((dept) -> {
            DepartmentDto newDepartment = departmentCourseService.addDepartment(
                    DepartmentDto.builder().name(dept).build());
            Integer id = newDepartment.getId();
            for (int i = 1; i <= 2; i++) {
                userService.save(
                        RegistrationDto.builder()
                                .firstName("Department")
                                .lastName("Staff " + id)
                                .email("ds" + id + i + "@gg.com")
                                .password("1234")
                                .gender(GenderEnum.Male.name())
                                .role(RoleEnum.DEPARTMENT_STAFF.name())
                                .dob(LocalDate.of(2000, 10 + i, 20))
                                .department(newDepartment)
                                .build());
            }
            for (int i = 1; i <= 3; i++) {
                userService.save(
                        RegistrationDto.builder()
                                .firstName("Committee")
                                .lastName("Member " + id)
                                .email("cm" + id + i + "@gg.com")
                                .password("1234")
                                .gender(GenderEnum.Other.name())
                                .role(RoleEnum.COMMITTEE_MEMBER.name())
                                .dob(LocalDate.of(2010, 6 + i, 28))
                                .department(newDepartment)
                                .build());
            }
            for (int i = 1; i <= 5; i++) {
                UserDto newInstructor = userService.save(
                        RegistrationDto.builder()
                                .firstName("Lecturer " + id)
                                .lastName("Test " + i)
                                .email("l" + id + i + "@gg.com")
                                .password("1234")
                                .gender(GenderEnum.Female.name())
                                .role(RoleEnum.INSTRUCTOR.name())
                                .dob(LocalDate.of(1970, i, 2))
                                .department(newDepartment)
                                .build());
                departmentCourseService.addCourse(
                        CourseDto.builder()
                                .name(newDepartment.getName() +  " - Course - " + i)
                                .active(i % 2 == 1)
                                .department(newDepartment)
                                .instructor((i == 1 || i == 4) ? null : newInstructor)
                                .build());
            }
        });
        departmentCourseService.listAllDepartments().stream().forEach(
                (dept) -> {
                    Integer id = dept.getId();
                    Path filePath = Paths
                            .get("D:\\Working Files\\Ashwith\\TA Application Project\\group project 2 (1).pdf");
                    String fileName = "group project 2 (1).pdf";
                    String contentType = "application/pdf";
                    byte[] content = null;
                    try {
                        content = Files.readAllBytes(filePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    MultipartFile mockCv = new MockMultipartFile("cvFile", fileName, contentType, content);
                    for (int i = 1; i <= 4; i++) {
                        UserDto applicant = userService.save(RegistrationDto.builder()
                                .firstName("Applicant " + id)
                                .lastName("Test " + i)
                                .email("a" + id + i + "@gg.com")
                                .password("1234")
                                .gender(GenderEnum.Male.name())
                                .role(RoleEnum.APPLICANT.name())
                                .dob(LocalDate.of(2012, 4 + i, 10))
                                .department(dept)
                                .build());
                        String cvfileName = applicant.getEmail() + " - "
                                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss.SSS"))
                                + ".pdf";
                        String cvAbsolutePath = System.getProperty("user.dir") + "\\cv-uploads\\";

                        applicationService.save(
                                ApplicationDto.builder()
                                        .applicant(applicant)
                                        .description("Test Enrollment for " + applicant.getName())
                                        .coursesSelected(
                                                departmentCourseService.listAllCoursesByDepartment(dept)
                                                        .stream().filter((crs) -> crs.getActive()).toList())
                                        .isExperainced(true)
                                        .prevExperiances(
                                                Stream.of(
                                                        PreviousExperianceDto.builder()
                                                                .description("Fun Experiance")
                                                                .startDate(LocalDate.of(2020, 5, 15))
                                                                .endDate(LocalDate.of(2020, 6, 15))
                                                                .build(),
                                                        PreviousExperianceDto.builder()
                                                                .description("Not So Fun Experiance")
                                                                .startDate(LocalDate.of(2021, 5, 15))
                                                                .endDate(LocalDate.of(2021, 6, 15))
                                                                .build())
                                                        .toList())
                                        .isCoursesRecommended(true)
                                        .coursesRecommended(
                                                departmentCourseService.listAllCoursesByDepartment(dept)
                                                        .stream().filter((crs) -> crs.getActive()).toList())
                                        .coursesRecommendedDate(LocalDateTime.now())
                                        .isCourseAlloted(true)
                                        .courseAlloted(
                                                departmentCourseService.listAllCoursesByDepartment(dept)
                                                        .stream().filter((crs) -> crs.getActive()).findAny()
                                                        .orElseThrow(() -> new EntityNotFoundException(
                                                                "Error Alloting Course at [" + dept.getId() + ","
                                                                        + applicant.getId() + 1 + "]")))
                                        .courseAllotedDate(LocalDateTime.now())
                                        .cvFileString(cvAbsolutePath + cvfileName)
                                        .status(ApplicationStatusEnum.COURSE_ALLOTED)
                                        .build());
                        try {
                            mockCv.transferTo(new File(cvAbsolutePath + cvfileName));
                        } catch (IllegalStateException | IOException e) {
                            e.printStackTrace();
                        }

                        applicationService.save(
                                ApplicationDto.builder()
                                        .applicant(applicant)
                                        .description("Test Enrollment for " + applicant.getName())
                                        .coursesSelected(
                                                departmentCourseService.listAllCoursesByDepartment(dept)
                                                        .stream().filter((crs) -> crs.getActive()).toList())
                                        .isExperainced(false)
                                        .prevExperiances(new HashSet<>())
                                        .isCoursesRecommended(true)
                                        .coursesRecommended(
                                                departmentCourseService.listAllCoursesByDepartment(dept)
                                                        .stream().filter((crs) -> crs.getActive()).toList())
                                        .coursesRecommendedDate(LocalDateTime.now())
                                        .isCourseAlloted(true)
                                        .courseAlloted(
                                                departmentCourseService.listAllCoursesByDepartment(dept)
                                                        .stream().filter((crs) -> crs.getActive()).findAny()
                                                        .orElseThrow(() -> new EntityNotFoundException(
                                                                "Error Alloting Course at [" + dept.getId() + ","
                                                                        + applicant.getId() + 1 + "]")))
                                        .courseAllotedDate(LocalDateTime.now())
                                        .cvFileString(cvAbsolutePath + cvfileName)
                                        .status(ApplicationStatusEnum.COURSE_ALLOTED)
                                        .build());
                        try {
                            mockCv.transferTo(new File(cvAbsolutePath + cvfileName));
                        } catch (IllegalStateException | IOException e) {
                            e.printStackTrace();
                        }

                        applicationService.save(
                                ApplicationDto.builder()
                                        .applicant(applicant)
                                        .description("Test Enrollment for " + applicant.getName())
                                        .coursesSelected(
                                                departmentCourseService.listAllCoursesByDepartment(dept)
                                                        .stream().filter((crs) -> crs.getActive()).toList())
                                        .isExperainced(true)
                                        .prevExperiances(
                                                Stream.of(
                                                        PreviousExperianceDto.builder()
                                                                .description("Fun Experiance")
                                                                .startDate(LocalDate.of(2020, 5, 15))
                                                                .endDate(LocalDate.of(2020, 6, 15))
                                                                .build())
                                                        .toList())
                                        .isCoursesRecommended(true)
                                        .coursesRecommended(
                                                departmentCourseService.listAllCoursesByDepartment(dept)
                                                        .stream().filter((crs) -> crs.getActive()).toList())
                                        .coursesRecommendedDate(LocalDateTime.now())
                                        .isCourseAlloted(true)
                                        .courseAlloted(
                                                departmentCourseService.listAllCoursesByDepartment(dept)
                                                        .stream().filter((crs) -> crs.getActive()).findAny()
                                                        .orElseThrow(() -> new EntityNotFoundException(
                                                                "Error Alloting Course at [" + dept.getId() + ","
                                                                        + applicant.getId() + 2 + "]")))
                                        .courseAllotedDate(LocalDateTime.now())
                                        .cvFileString(cvAbsolutePath + cvfileName)
                                        .status(ApplicationStatusEnum.COURSE_ALLOTED)
                                        .build());
                        try {
                            mockCv.transferTo(new File(cvAbsolutePath + cvfileName));
                        } catch (IllegalStateException | IOException e) {
                            e.printStackTrace();
                        }

                        applicationService.save(
                                ApplicationDto.builder()
                                        .applicant(applicant)
                                        .description("Test Enrollment for " + applicant.getName())
                                        .coursesSelected(
                                                departmentCourseService.listAllCoursesByDepartment(dept)
                                                        .stream().filter((crs) -> crs.getActive()).toList())
                                        .isExperainced(true)
                                        .prevExperiances(
                                                Stream.of(
                                                        PreviousExperianceDto.builder()
                                                                .description("Fun Experiance")
                                                                .startDate(LocalDate.of(2020, 5, 15))
                                                                .endDate(LocalDate.of(2020, 6, 15))
                                                                .build(),
                                                        PreviousExperianceDto.builder()
                                                                .description("Not So Fun Experiance")
                                                                .startDate(LocalDate.of(2021, 5, 15))
                                                                .endDate(LocalDate.of(2021, 6, 15))
                                                                .build())
                                                        .toList())
                                        .isCoursesRecommended(true)
                                        .coursesRecommended(
                                                departmentCourseService.listAllCoursesByDepartment(dept)
                                                        .stream().filter((crs) -> crs.getActive()).toList())
                                        .coursesRecommendedDate(LocalDateTime.now())
                                        .isCourseAlloted(false)
                                        .cvFileString(cvAbsolutePath + cvfileName)
                                        .status(ApplicationStatusEnum.STAFF_RECOMMENDED)
                                        .build());
                        try {
                            mockCv.transferTo(new File(cvAbsolutePath + cvfileName));
                        } catch (IllegalStateException | IOException e) {
                            e.printStackTrace();
                        }

                        applicationService.save(
                                ApplicationDto.builder()
                                        .applicant(applicant)
                                        .description("Test Enrollment for " + applicant.getName())
                                        .coursesSelected(
                                                departmentCourseService.listAllCoursesByDepartment(dept)
                                                        .stream().filter((crs) -> crs.getActive()).toList())
                                        .isExperainced(false)
                                        .prevExperiances(new HashSet<>())
                                        .isCoursesRecommended(true)
                                        .coursesRecommended(
                                                departmentCourseService.listAllCoursesByDepartment(dept)
                                                        .stream().filter((crs) -> crs.getActive()).toList())
                                        .coursesRecommendedDate(LocalDateTime.now())
                                        .isCourseAlloted(false)
                                        .cvFileString(cvAbsolutePath + cvfileName)
                                        .status(ApplicationStatusEnum.STAFF_RECOMMENDED)
                                        .build());
                        try {
                            mockCv.transferTo(new File(cvAbsolutePath + cvfileName));
                        } catch (IllegalStateException | IOException e) {
                            e.printStackTrace();
                        }

                        applicationService.save(
                                ApplicationDto.builder()
                                        .applicant(applicant)
                                        .description("Test Enrollment for " + applicant.getName())
                                        .coursesSelected(
                                                departmentCourseService.listAllCoursesByDepartment(dept)
                                                        .stream().filter((crs) -> crs.getActive()).toList())
                                        .isExperainced(true)
                                        .prevExperiances(
                                                Stream.of(
                                                        PreviousExperianceDto.builder()
                                                                .description("Fun Experiance")
                                                                .startDate(LocalDate.of(2020, 5, 15))
                                                                .endDate(LocalDate.of(2020, 6, 15))
                                                                .build())
                                                        .toList())
                                        .isCoursesRecommended(false)
                                        .coursesRecommended(new HashSet<>())
                                        .isCourseAlloted(false)
                                        .cvFileString(cvAbsolutePath + cvfileName)
                                        .status(ApplicationStatusEnum.SUBMITTED)
                                        .build());
                        try {
                            mockCv.transferTo(new File(cvAbsolutePath + cvfileName));
                        } catch (IllegalStateException | IOException e) {
                            e.printStackTrace();
                        }

                        applicationService.save(
                                ApplicationDto.builder()
                                        .applicant(applicant)
                                        .description("Test Enrollment for " + applicant.getName())
                                        .coursesSelected(
                                                departmentCourseService.listAllCoursesByDepartment(dept)
                                                        .stream().filter((crs) -> crs.getActive()).toList())
                                        .isExperainced(false)
                                        .prevExperiances(new HashSet<>())
                                        .isCoursesRecommended(false)
                                        .coursesRecommended(new HashSet<>())
                                        .isCourseAlloted(false)
                                        .cvFileString(cvAbsolutePath + cvfileName)
                                        .status(ApplicationStatusEnum.SUBMITTED)
                                        .build());
                        try {
                            mockCv.transferTo(new File(cvAbsolutePath + cvfileName));
                        } catch (IllegalStateException | IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
        userService.save(
                RegistrationDto.builder()
                        .firstName("Admin")
                        .lastName("User 1")
                        .email("test@gg.com")
                        .password("1234")
                        .gender(GenderEnum.Male.name())
                        .role(RoleEnum.ADMIN.name())
                        .dob(LocalDate.of(2000, 4, 23))
                        .department(
                                departmentCourseService.getDepartmentByName("Weird Department"))
                        .build());
        return "redirect:/";
    }
}
