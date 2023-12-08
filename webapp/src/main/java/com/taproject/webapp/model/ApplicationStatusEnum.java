package com.taproject.webapp.model;

public enum ApplicationStatusEnum {
    SUBMITTED("Waiting Staff Recommendations"),
    STAFF_RECOMMENDED("Waiting Committee Approvals"),
    COURSE_ALLOTED("Waiting Applicant Acceptance"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected"),
    ARCHIVED("Archived"),
    ILLEGAL("Illegal Status");

    private String description;

    private ApplicationStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
