package com.taproject.webapp.model;

import java.time.LocalDateTime;
import java.util.Collection;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "applications")
public class ApplicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private UserEntity applicant;

    @Column(nullable = false)
    @ColumnDefault("''")
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Collection<CourseEntity> coursesSelected;

    @Column(nullable = false)
    private Boolean isExperianced;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Collection<PreviousExperianceEntity> prevExperiances;

    @Enumerated(value = EnumType.STRING)
    private ApplicationStatusEnum status;

    private String cvFileString;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isCoursesRecommended = false;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<CourseEntity> coursesRecommended;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime coursesRecommendedDate;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isCourseAlloted = false;

    @ManyToOne(fetch = FetchType.EAGER)
    private CourseEntity courseAlloted;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime courseAllotedDate;

    private Boolean accepted;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime acceptanceDate;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "applications_feedbacks", joinColumns = @JoinColumn(name = "application_id"))
    private Collection<String> feedbacks;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime submittedDate;

}
