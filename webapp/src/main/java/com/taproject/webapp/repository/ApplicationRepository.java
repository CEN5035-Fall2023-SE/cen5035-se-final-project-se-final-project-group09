package com.taproject.webapp.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.taproject.webapp.model.ApplicationEntity;
import com.taproject.webapp.model.ApplicationStatusEnum;
import com.taproject.webapp.model.UserEntity;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Integer> {
    Collection<ApplicationEntity> findByApplicant(UserEntity applicant);

    @Modifying
    @Query(value = "UPDATE applications a SET a.cv_file_string = :cvFileString WHERE a.id = :id", nativeQuery = true)
    Integer updateCvFileStringById(
            @Param("id") Integer id,
            @Param("cvFileString") String cvFileString);

    @Modifying
    @Query(value = "INSERT INTO applications_feedbacks(application_id, feedbacks) VALUES(:id, :feedback)", nativeQuery = true)
    void addFeedbackById(
            @Param("id") Integer id,
            @Param("feedback") String feedback);

    @Modifying
    @Query(value = "UPDATE ApplicationEntity a SET a.status = :status WHERE a.id = :id")
    Integer updateStatusById(
            @Param("id") Integer id,
            @Param("status") ApplicationStatusEnum status);
}
