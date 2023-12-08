package com.taproject.webapp.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoursesRecommendedDto {
    @NotNull(message = "Application ID is Null!")
    private Integer applicationId;
    @NotEmpty(message = "Please select atlease one Course!")
    @Builder.Default
    private List<Integer> courseIds = new ArrayList<>();
}
