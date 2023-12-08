package com.taproject.webapp.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taproject.webapp.dto.DepartmentDto;
import com.taproject.webapp.dto.RegistrationDto;
import com.taproject.webapp.service.DepartmentCourseService;
import com.taproject.webapp.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final DepartmentCourseService departmentCourseService;

    public AuthController(UserService userService, DepartmentCourseService departmentCourseService) {
        this.userService = userService;
        this.departmentCourseService = departmentCourseService;
    }

    @GetMapping("/register")
    public String registerForm(@RequestParam Map<String, String> params, Model model) {
        model.addAttribute("pageTitle",
                (params.containsKey("internal") ? "Internal" : "Applicant") + " " + "Registration");
        model.addAttribute("depts", departmentCourseService.listAllDepartments());
        model.addAttribute("user", RegistrationDto.builder()
                .role(params.containsKey("internal") ? null : "APPLICANT")
                .department(DepartmentDto.builder().build())
                .build());
        model.addAttribute("internal", params.containsKey("internal"));
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute(name = "user") RegistrationDto registrationDto,
            BindingResult result,
            @RequestParam Map<String, String> params,
            Model model) {
        if (registrationDto.getEmail() != null &&
                !registrationDto.getEmail().isEmpty() &&
                userService.existsByEmail(registrationDto.getEmail())) {
            result.rejectValue("email", null, "Email already exists!");
        }
        if (result.hasFieldErrors("department")) {
            registrationDto.setDepartment(
                    DepartmentDto.builder().build());
        }
        if (result.hasErrors()) {
            model.addAttribute("pageTitle",
                    (params.containsKey("internal") ? "Internal" : "Applicant") + " " + "Registration");
            model.addAttribute("depts", departmentCourseService.listAllDepartments());
            model.addAttribute("user", registrationDto);
            model.addAttribute("internal", params.containsKey("internal"));
            return "auth/register";
        }
        userService.save(registrationDto);
        return "redirect:/auth/login?regsuccess";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("pageTitle", "User Login");
        return "auth/login";
    }
}
