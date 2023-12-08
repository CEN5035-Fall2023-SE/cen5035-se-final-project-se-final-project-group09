package com.taproject.webapp.model;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RoleEnum {
    ADMIN("Admin"),
    APPLICANT("Applicant"),
    DEPARTMENT_STAFF("Department Staff"),
    COMMITTEE_MEMBER("Committee Member"),
    INSTRUCTOR("Instructor");

    public String description;
    private String role;

    RoleEnum(String desc) {
        this.description = desc;
        this.role = "ROLE_" + name();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of(
                new SimpleGrantedAuthority(this.role)
            ).toList();
    }

    public static Collection<RoleEnum> listRoles() {
        return Stream.of(RoleEnum.values())
                .filter((role) -> role != ADMIN)
                .collect(Collectors.toList());
    }

}
