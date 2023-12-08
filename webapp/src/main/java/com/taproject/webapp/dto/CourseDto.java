package com.taproject.webapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private Integer id;
    private String name;
    private Boolean active;
    private DepartmentDto department;
    private UserDto instructor;
}
