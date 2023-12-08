package com.taproject.webapp.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taproject.webapp.dto.ApplicationDto;
import com.taproject.webapp.dto.CourseAllotDto;
import com.taproject.webapp.dto.CoursesRecommendedDto;
import com.taproject.webapp.dto.EnrollFormDto;
import com.taproject.webapp.model.ApplicationStatusEnum;
import com.taproject.webapp.model.AuthUser;
import com.taproject.webapp.model.RoleEnum;
import com.taproject.webapp.service.ApplicationService;
import com.taproject.webapp.service.DepartmentCourseService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/application")
public class ApplicationController {
    private final DepartmentCourseService departmentCourseService;
    private final ApplicationService applicationService;

    public ApplicationController(DepartmentCourseService departmentCourseService,
            ApplicationService applicationService) {
        this.departmentCourseService = departmentCourseService;
        this.applicationService = applicationService;
    }

    @GetMapping("/enroll")
    @PreAuthorize("hasAnyRole({'ADMIN', 'APPLICANT'})")
    public String enrollForm(Model model) {
        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("courses",
                departmentCourseService.listAllCoursesByDepartment(authUser.getUserDto().getDepartment()));
        model.addAttribute("newApplication",
                EnrollFormDto.builder().applicant(authUser.getUserDto()).build());
        return "application/enroll";
    }

    @PostMapping("/enroll/save")
    @PreAuthorize("hasAnyRole({'ADMIN', 'APPLICANT'})")
    public String enroll(
            @Valid @ModelAttribute(name = "newApplication") EnrollFormDto application,
            BindingResult result,
            Model model) throws IllegalStateException, IOException {
        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (result.hasErrors()) {
            model.addAttribute("courses",
                    departmentCourseService.listAllCoursesByDepartment(authUser.getUserDto().getDepartment()));
            model.addAttribute("newApplication", application);
            return "application/enroll";
        }
        applicationService.submitNew(application);
        return "redirect:/application/view";
    }

    @GetMapping("/view")
    public String viewList(Model model) {
        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("applications",
                applicationService.listAllApplications()
                        .stream().filter(
                                (application) -> {
                                    if (authUser.getUserDto().getRole() == RoleEnum.ADMIN) {
                                        return true;
                                    } else if (authUser.getUserDto().getRole() == RoleEnum.APPLICANT) {
                                        return application.getApplicant().getId() == authUser.getUserDto().getId();
                                    } else {
                                        return application.getApplicant().getDepartment().getId() == authUser
                                                .getUserDto().getDepartment().getId();
                                    }
                                })
                        .sorted((app1, app2) -> app2.getId().compareTo(app1.getId())).toList());
        return "application/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Integer id, Model model) {
        ApplicationDto application = applicationService.getApplicationById(id);
        model.addAttribute("app", application);
        model.addAttribute("updateCoursesRecommended", CoursesRecommendedDto.builder().build());
        model.addAttribute("updateCourseAlloted", CourseAllotDto.builder().build());
        return "application/view";
    }

    @PostMapping("/view/{id}/coursesRecommended")
    @PreAuthorize("hasAnyRole({'ADMIN', 'DEPARTMENT_STAFF'})")
    public String updateCoursesRecommended(
            @PathVariable(name = "id") Integer id,
            @Valid @ModelAttribute(name = "updateCoursesRecommended") CoursesRecommendedDto dto,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            ApplicationDto application = applicationService.getApplicationById(id);
            model.addAttribute("app", application);
            model.addAttribute("updateCoursesRecommended", CoursesRecommendedDto.builder().build());
            model.addAttribute("updateCourseAlloted", CourseAllotDto.builder().build());
            return "application/view";
        }
        applicationService.updateCoursesRecommended(dto);
        return "redirect:/application/view/" + id;
    }

    @PostMapping("/view/{id}/courseAllot")
    @PreAuthorize("hasAnyRole({'ADMIN', 'COMMITTEE_MEMBER'})")
    public String updateCoursesRecommended(
            @PathVariable(name = "id") Integer id,
            @Valid @ModelAttribute(name = "updateCourseAlloted") CourseAllotDto dto,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            ApplicationDto application = applicationService.getApplicationById(id);
            model.addAttribute("app", application);
            model.addAttribute("updateCoursesRecommended", CoursesRecommendedDto.builder().build());
            model.addAttribute("updateCourseAlloted", dto);
            return "application/view";
        }
        applicationService.updateCourseAlloted(dto);
        return "redirect:/application/view/" + id;
    }

    @PostMapping("/view/{id}/addFeedback")
    @PreAuthorize("hasAnyRole({'ADMIN', 'INSTRUCTOR'})")
    public String updateFeedback(
            @PathVariable(name = "id") Integer id,
            @ModelAttribute(name = "feedback") String feedback,
            BindingResult result, Model model) {
        if (feedback == null || feedback.length() <= 0) {
            ApplicationDto application = applicationService.getApplicationById(id);
            model.addAttribute("app", application);
            model.addAttribute("updateCoursesRecommended", CoursesRecommendedDto.builder().build());
            model.addAttribute("updateCourseAlloted", CourseAllotDto.builder().build());
            return "application/view";
        }
        applicationService.addFeedback(id, feedback);
        return "redirect:/application/view/" + id;
    }

    @PostMapping(value = "/view/{id}/{acceptance}")
    @PreAuthorize("hasAnyRole({'ADMIN', 'APPLICANT'})")
    public String updateAcceptanceString(
            @PathVariable(name = "id") Integer id,
            @PathVariable(name = "acceptance") Boolean acceptance,
            Model model) {
        ApplicationDto application = applicationService.getApplicationById(id);
        if (application.getStatus().equals(ApplicationStatusEnum.COURSE_ALLOTED)) {
            applicationService.updateAcceptance(id, acceptance);
        }
        return "redirect:/application/view/" + id;
    }

    @PostMapping(value = "/view/{id}/archive")
    @PreAuthorize("hasAnyRole({'ADMIN', 'DEPARTMENT_STAFF'})")
    public String archive(
            @PathVariable(name = "id") Integer id,
            Model model) {
        ApplicationDto application = applicationService.getApplicationById(id);
        if (application.getStatus().equals(ApplicationStatusEnum.ACCEPTED)
                || application.getStatus().equals(ApplicationStatusEnum.REJECTED)) {
            applicationService.archiveApplication(id);
        }
        return "redirect:/application/view/" + id;
    }

    @GetMapping("/view/{id}/cv")
    public ResponseEntity<byte[]> viewCv(@PathVariable(name = "id") Integer id) throws IOException {
        ApplicationDto application = applicationService.getApplicationById(id);

        String filePath = application.getCvFileString();
        String[] pathSplits = filePath.split("\\\\");
        String fileName = pathSplits[pathSplits.length - 1];
        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add("content-disposition", "inline;filename=" + fileName);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        ResponseEntity<byte[]> response = new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);

        return response;
    }
}
