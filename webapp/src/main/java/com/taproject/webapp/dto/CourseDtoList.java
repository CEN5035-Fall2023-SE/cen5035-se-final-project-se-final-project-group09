package com.taproject.webapp.dto;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDtoList {
    private Collection<CourseDto> courses;

    public void addCourse(CourseDto course) {
        this.courses.add(course);
    }
}
