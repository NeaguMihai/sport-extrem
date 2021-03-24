package com.mihaineagu.data.api.v1.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SportDTO {

    private String sportType;
    private String uri;
    private InformationDTO information;
}
