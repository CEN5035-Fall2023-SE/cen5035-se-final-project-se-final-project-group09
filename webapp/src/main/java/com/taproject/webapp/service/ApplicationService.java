package com.taproject.webapp.service;

import java.io.IOException;
import java.util.Collection;

import com.taproject.webapp.dto.ApplicationDto;
import com.taproject.webapp.dto.CourseAllotDto;
import com.taproject.webapp.dto.CoursesRecommendedDto;
import com.taproject.webapp.dto.EnrollFormDto;
import com.taproject.webapp.dto.UserDto;

public interface ApplicationService {
    ApplicationDto submitNew(EnrollFormDto application) 
            throws IllegalStateException, IOException;
    ApplicationDto getApplicationById(Integer id);
    Collection<ApplicationDto> listAllApplications();
    Collection<ApplicationDto> listAllApplicationsByApplicant(UserDto applicant);
    ApplicationDto updateCoursesRecommended(CoursesRecommendedDto dto);
    ApplicationDto updateCourseAlloted(CourseAllotDto dto);
    ApplicationDto save(ApplicationDto dto);
    ApplicationDto updateAcceptance(Integer id, Boolean acceptance);
    ApplicationDto addFeedback(Integer id, String feedback);
    ApplicationDto archiveApplication(Integer id);
}
