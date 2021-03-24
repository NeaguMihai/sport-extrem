package com.mihaineagu.data.api.v1.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CountryDTO {

    private String countryName;
    private String uri;
    private RegionDTO regionDTO;
}
