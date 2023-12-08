package com.taproject.webapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseAllotDto {
    @NotNull(message = "Application ID is Null!")
    private Integer applicationId;
    @NotNull(message = "Please select a course to Allot!")
    private Integer courseId;
}
