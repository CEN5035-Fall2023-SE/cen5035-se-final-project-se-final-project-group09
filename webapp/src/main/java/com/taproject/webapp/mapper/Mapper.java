package com.taproject.webapp.mapper;

import java.util.HashSet;
import java.util.stream.Collectors;

import com.taproject.webapp.dto.ApplicationDto;
import com.taproject.webapp.dto.CourseDto;
import com.taproject.webapp.dto.DepartmentDto;
import com.taproject.webapp.dto.PreviousExperianceDto;
import com.taproject.webapp.dto.RegistrationDto;
import com.taproject.webapp.dto.UserDto;
import com.taproject.webapp.model.ApplicationEntity;
import com.taproject.webapp.model.CourseEntity;
import com.taproject.webapp.model.DepartmentEntity;
import com.taproject.webapp.model.GenderEnum;
import com.taproject.webapp.model.PreviousExperianceEntity;
import com.taproject.webapp.model.RoleEnum;
import com.taproject.webapp.model.UserEntity;

public class Mapper {
    public static UserDto mapToDto(UserEntity entity) {
        return (entity == null) ? UserDto.builder().build()
                : UserDto.builder()
                        .id(entity.getId())
                        .firstName(entity.getFirstName())
                        .lastName(entity.getLastName())
                        .email(entity.getEmail())
                        .role(entity.getRole())
                        .gender(entity.getGender())
                        .dob(entity.getDob())
                        .department(mapToDto(entity.getDepartment()))
                        .build();
    }

    public static UserEntity mapToEntity(UserDto dto) {
        return (dto == null) ? null
                : UserEntity.builder()
                        .id(dto.getId())
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .email(dto.getEmail())
                        .role(dto.getRole())
                        .gender(dto.getGender())
                        .dob(dto.getDob())
                        .department(mapToEntity(dto.getDepartment()))
                        .build();
    }

    public static DepartmentDto mapToDto(DepartmentEntity entity) {
        return (entity == null) ? DepartmentDto.builder().build()
                : DepartmentDto.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .build();
    }

    public static DepartmentEntity mapToEntity(DepartmentDto dto) {
        return (dto == null) ? null
                : DepartmentEntity.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .build();
    }

    public static CourseDto mapToDto(CourseEntity entity) {
        return (entity == null) ? CourseDto.builder().build()
                : CourseDto.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .active(entity.getActive())
                        .department(mapToDto(entity.getDepatment()))
                        .instructor(mapToDto(entity.getInstructor()))
                        .build();
    }

    public static CourseEntity mapToEntity(CourseDto dto) {
        return (dto == null) ? null
                : CourseEntity.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .active(dto.getActive())
                        .depatment(mapToEntity(dto.getDepartment()))
                        .instructor(mapToEntity(dto.getInstructor()))
                        .build();
    }

    public static UserEntity mapToEntity(RegistrationDto dto) {
        return (dto == null) ? null
                : UserEntity.builder()
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .email(dto.getEmail())
                        .gender(GenderEnum.valueOf(dto.getGender()))
                        .dob(dto.getDob())
                        .role(RoleEnum.valueOf(dto.getRole()))
                        .department(mapToEntity(dto.getDepartment()))
                        .build();
    }

    public static PreviousExperianceEntity mapToEntity(PreviousExperianceDto dto) {
        return (dto == null) ? null
                : PreviousExperianceEntity.builder()
                        .id(dto.getId())
                        .description(dto.getDescription())
                        .starDate(dto.getStartDate())
                        .endDate(dto.getEndDate())
                        .build();
    }

    public static PreviousExperianceDto mapToDto(PreviousExperianceEntity entity) {
        return (entity == null) ? PreviousExperianceDto.builder().build()
                : PreviousExperianceDto.builder()
                        .id(entity.getId())
                        .description(entity.getDescription())
                        .startDate(entity.getStarDate())
                        .endDate(entity.getEndDate())
                        .build();
    }

    public static ApplicationEntity mapToEntity(ApplicationDto dto) {
        return (dto == null) ? null
                : ApplicationEntity.builder()
                        .id(dto.getId())
                        .applicant(mapToEntity(dto.getApplicant()))
                        .description(dto.getDescription())
                        .coursesSelected(
                                dto.getCoursesSelected()
                                        .stream()
                                        .map((course) -> mapToEntity(course))
                                        .collect(Collectors.toList()))
                        .isExperianced(dto.getIsExperainced())
                        .prevExperiances(
                                dto.getPrevExperiances()
                                        .stream()
                                        .map((prevExp) -> mapToEntity(prevExp))
                                        .collect(Collectors.toList()))
                        .coursesRecommended(
                                dto.getCoursesRecommended()
                                        .stream()
                                        .map((course) -> mapToEntity(course))
                                        .collect(Collectors.toList()))
                        .isCoursesRecommended(dto.getIsCoursesRecommended())
                        .coursesRecommendedDate(dto.getCoursesRecommendedDate())
                        .isCourseAlloted(dto.getIsCourseAlloted())
                        .courseAlloted(mapToEntity(dto.getCourseAlloted()))
                        .courseAllotedDate(dto.getCourseAllotedDate())
                        .accepted(dto.getIsAccepted())
                        .acceptanceDate(dto.getAcceptanceDate())
                        .status(dto.getStatus())
                        .submittedDate(dto.getSubmittedDate())
                        .cvFileString(dto.getCvFileString())
                        .feedbacks(dto.getFeedbacks())
                        .build();
    }

    public static ApplicationDto mapToDto(ApplicationEntity entity) {
        return (entity == null) ? ApplicationDto.builder().build()
                : ApplicationDto.builder()
                        .id(entity.getId())
                        .applicant(mapToDto(entity.getApplicant()))
                        .description(entity.getDescription())
                        .coursesSelected(
                                entity.getCoursesSelected()
                                        .stream()
                                        .map((course) -> mapToDto(course))
                                        .collect(Collectors.toList()))
                        .isExperainced(entity.getIsExperianced())
                        .prevExperiances((entity.getPrevExperiances() == null) ? new HashSet<>()
                                : entity.getPrevExperiances()
                                        .stream()
                                        .map((prevExp) -> mapToDto(prevExp))
                                        .collect(Collectors.toList()))
                        .coursesRecommended((entity.getCoursesRecommended() == null) ? new HashSet<>()
                                : entity.getCoursesRecommended()
                                        .stream()
                                        .map((course) -> mapToDto(course))
                                        .collect(Collectors.toList()))
                        .isCoursesRecommended(entity.getIsCoursesRecommended())
                        .coursesRecommendedDate(entity.getCoursesRecommendedDate())
                        .isCourseAlloted(entity.getIsCourseAlloted())
                        .courseAlloted(mapToDto(entity.getCourseAlloted()))
                        .courseAllotedDate(entity.getCourseAllotedDate())
                        .isAccepted(entity.getAccepted())
                        .acceptanceDate(entity.getAcceptanceDate())
                        .status(entity.getStatus())
                        .submittedDate(entity.getSubmittedDate())
                        .cvFileString(entity.getCvFileString())
                        .feedbacks((entity.getFeedbacks() == null) ? new HashSet<>() : entity.getFeedbacks())
                        .build();
    }
}
