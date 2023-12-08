package com.taproject.webapp.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

import com.taproject.webapp.annotation.ValidCv;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollFormDto {
    private Integer id;
    private UserDto applicant;
    @NotEmpty(message = "Please provide a brief!")
    private String description;
    @NotEmpty(message = "Please select atlease one Course!")
    @Builder.Default
    private List<Integer> coursesSelected = new ArrayList<>();
    @Builder.Default
    // @Valid
    private List<PreviousExperianceDto> prevExp = Stream.of(PreviousExperianceDto.builder().build(), PreviousExperianceDto.builder().build()).toList();
    @ValidCv
    private MultipartFile cvFile;
}
