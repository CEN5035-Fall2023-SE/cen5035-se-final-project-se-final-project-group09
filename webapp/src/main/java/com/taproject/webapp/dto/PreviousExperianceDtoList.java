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
public class PreviousExperianceDtoList {
    private Collection<PreviousExperianceDto> prevExps;

    public void addPrevExp(PreviousExperianceDto prevExp) {
        prevExps.add(prevExp);
    }
}
