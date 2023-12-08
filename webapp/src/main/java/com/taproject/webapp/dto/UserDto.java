package com.taproject.webapp.dto;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taproject.webapp.model.GenderEnum;
import com.taproject.webapp.model.RoleEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private RoleEnum role;
    private GenderEnum gender;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;
    @Builder.Default
    private DepartmentDto department = DepartmentDto.builder().build();

    @JsonIgnore
    public String getName() {
        return (firstName == null && lastName == null) ? "--None--"
                : (((firstName == null) ? "" : firstName) + " " + ((lastName == null) ? "" : lastName));
    }

    @JsonIgnore
    public Integer getAge() {
        return Period.between((dob == null) ? LocalDate.EPOCH : dob,
                LocalDate.now()).getYears();
    }
}
