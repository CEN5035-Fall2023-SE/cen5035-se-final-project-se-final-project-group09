package com.taproject.webapp.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto {
    @NotEmpty(message = "First Name cannot be empty!")
    private String firstName;
    @NotEmpty(message = "Last Name cannot be empty!")
    private String lastName;
    @NotEmpty(message = "Email cannot be empty!")
    // @Email(regexp = ".+[@].+[\\.].+", message = "Please enter a valid email!")
    @Email(regexp = "^([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6})?$", message = "Please enter a valid email!")
    private String email;
    @NotEmpty(message = "Password cannot be empty!")
    private String password;
    @NotNull(message = "Date of birth is required!")
    @Past(message = "Date of birth cannot be future!")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;
    @NotNull(message = "Please select your Gender!")
    private String gender;
    @NotEmpty(message = "Please select your Role!")
    private String role;
    @NotNull(message = "Please select Department!")
    private DepartmentDto department;
}
