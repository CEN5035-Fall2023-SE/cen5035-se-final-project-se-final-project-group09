package com.taproject.webapp.dto;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.format.annotation.DateTimeFormat;

import com.taproject.webapp.model.ApplicationStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDto {
    private Integer id;
    private UserDto applicant;
    private String description;
    private Collection<CourseDto> coursesSelected;
    private Boolean isExperainced;
    private Collection<PreviousExperianceDto> prevExperiances;
    private Boolean isCoursesRecommended;
    private Collection<CourseDto> coursesRecommended;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime coursesRecommendedDate;
    private Boolean isCourseAlloted;
    private CourseDto courseAlloted;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime courseAllotedDate;
    private Boolean isAccepted;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime acceptanceDate;
    private ApplicationStatusEnum status;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime submittedDate;
    private String cvFileString;
    private Collection<String> feedbacks;
}
