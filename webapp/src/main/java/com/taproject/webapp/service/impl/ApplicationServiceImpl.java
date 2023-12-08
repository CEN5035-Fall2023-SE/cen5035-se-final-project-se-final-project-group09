package com.taproject.webapp.service.impl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.stereotype.Service;

import com.taproject.webapp.dto.ApplicationDto;
import com.taproject.webapp.dto.CourseAllotDto;
import com.taproject.webapp.dto.CoursesRecommendedDto;
import com.taproject.webapp.dto.EnrollFormDto;
import com.taproject.webapp.dto.PreviousExperianceDto;
import com.taproject.webapp.dto.UserDto;
import com.taproject.webapp.mapper.Mapper;
import com.taproject.webapp.model.ApplicationEntity;
import com.taproject.webapp.model.ApplicationStatusEnum;
import com.taproject.webapp.repository.ApplicationRepository;
import com.taproject.webapp.repository.CourseReporsitory;
import com.taproject.webapp.repository.UserRepository;
import com.taproject.webapp.service.ApplicationService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final CourseReporsitory courseReporsitory;
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public ApplicationDto submitNew(EnrollFormDto application) throws IllegalStateException, IOException {
        Collection<PreviousExperianceDto> previousExperiances = new HashSet<>();
        for (PreviousExperianceDto prevExp : application.getPrevExp()) {
            if (prevExp.getStartDate() != null
                    && prevExp.getEndDate() != null
                    && prevExp.getStartDate().isBefore(prevExp.getEndDate())
                    && prevExp.getDescription() != null
                    && prevExp.getDescription().length() > 0) {
                previousExperiances.add(prevExp);
            }
        }

        ApplicationEntity newApplication = ApplicationEntity.builder()
                .applicant(userRepository.findById(
                        application.getApplicant().getId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "User with ID - [" + application.getApplicant().getId() + "] Not Found!")))
                .description(application.getDescription())
                .isExperianced(previousExperiances.size() > 0)
                .prevExperiances(previousExperiances.stream()
                        .map((prevExp) -> Mapper.mapToEntity(prevExp)).toList())
                .status(ApplicationStatusEnum.SUBMITTED)
                .coursesSelected(application.getCoursesSelected()
                        .stream().map((id) -> courseReporsitory.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException(
                                        "Course with ID - [" + id + "] Not Found!")))
                        .toList())
                .build();

        newApplication = applicationRepository.saveAndFlush(newApplication);
        final Integer id = newApplication.getId();

        String cvfileName = id + " - " + newApplication.getApplicant().getEmail() + " - "
                + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".pdf";
        String cvAbsolutePath = System.getProperty("user.dir") + "\\cv-uploads\\";
        application.getCvFile().transferTo(new File(cvAbsolutePath + cvfileName));

        entityManager.joinTransaction();
        applicationRepository.updateCvFileStringById(newApplication.getId(), cvAbsolutePath + cvfileName);
        entityManager.flush();

        return Mapper.mapToDto(applicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Application with ID [" + id + "]")));
    }

    @Override
    public ApplicationDto getApplicationById(Integer id) {
        return Mapper.mapToDto(applicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Application with ID - [" + id + "]")));
    }

    @Override
    public Collection<ApplicationDto> listAllApplications() {
        return applicationRepository.findAll().stream().map(
                (application) -> Mapper.mapToDto(application)).toList();
    }

    @Override
    public Collection<ApplicationDto> listAllApplicationsByApplicant(UserDto applicant) {
        return applicationRepository.findByApplicant(Mapper.mapToEntity(applicant))
                .stream().map((application) -> Mapper.mapToDto(application)).toList();
    }

    @Override
    public ApplicationDto updateCoursesRecommended(CoursesRecommendedDto dto) {
        ApplicationEntity application = applicationRepository.findById(dto.getApplicationId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Application with ID [" + dto.getApplicationId() + "] Not Found!"));

        if (application.getCoursesRecommended() == null) {
            application.setCoursesRecommended(
                    dto.getCourseIds()
                            .stream()
                            .map((id) -> courseReporsitory.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException(
                                            "Course with ID [" + id + "] Not Found!")))
                            .toList());
        } else {
            application.getCoursesRecommended().addAll(
                    dto.getCourseIds()
                            .stream()
                            .map((id) -> courseReporsitory.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException(
                                            "Course with ID [" + id + "] Not Found!")))
                            .toList());
        }
        application.setIsCoursesRecommended(true);
        application.setCoursesRecommendedDate(LocalDateTime.now());
        application.setStatus(ApplicationStatusEnum.STAFF_RECOMMENDED);
        return Mapper.mapToDto(applicationRepository.saveAndFlush(application));
    }

    @Override
    public ApplicationDto updateCourseAlloted(CourseAllotDto dto) {
        ApplicationEntity application = applicationRepository.findById(dto.getApplicationId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Application with ID [" + dto.getApplicationId() + "] Not Found!"));
        application.setCourseAlloted(courseReporsitory.findById(dto.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Course with ID [" + dto.getCourseId() + "] Not Found!")));
        application.setIsCourseAlloted(true);
        application.setCourseAllotedDate(LocalDateTime.now());
        application.setStatus(ApplicationStatusEnum.COURSE_ALLOTED);
        return Mapper.mapToDto(applicationRepository.saveAndFlush(application));
    }

    @Override
    public ApplicationDto save(ApplicationDto dto) {
        return Mapper.mapToDto(applicationRepository.saveAndFlush(Mapper.mapToEntity(dto)));
    }

    @Override
    public ApplicationDto updateAcceptance(Integer id, Boolean acceptance) {
        ApplicationEntity application = applicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Application with ID [" + id + "] Not Found!"));
        application.setAccepted(acceptance);
        application.setAcceptanceDate(LocalDateTime.now());
        application.setStatus((acceptance) ? ApplicationStatusEnum.ACCEPTED : ApplicationStatusEnum.REJECTED);
        return Mapper.mapToDto(applicationRepository.saveAndFlush(application));
    }

    @Override
    @Transactional
    public ApplicationDto addFeedback(Integer id, String feedback) {
        entityManager.joinTransaction();
        applicationRepository.addFeedbackById(id, feedback);
        entityManager.flush();
        return Mapper.mapToDto(applicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Application with ID [" + id + "] Not Found!")));
    }

    @Override
    @Transactional
    public ApplicationDto archiveApplication(Integer id) {
        entityManager.joinTransaction();
        applicationRepository.updateStatusById(id, ApplicationStatusEnum.ARCHIVED);
        entityManager.flush();
        return Mapper.mapToDto(applicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Application with ID [" + id + "] Not Found!")));
    }

}
