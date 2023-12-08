package com.taproject.webapp.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreviousExperianceDto {
    private Integer id;
    // @NotEmpty(message = "Please provide a brief on your experiance")
    private String description;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    // @NotNull(message = "Please provide Start Date")
    private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    // @NotNull(message = "Please provide End Date")
    private LocalDate endDate;
}
