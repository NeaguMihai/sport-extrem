package com.mihaineagu.data.api.v1.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class InformationDTO {

    private Integer price;
    private String startingPeriod;
    private String endingPeriod;
}
