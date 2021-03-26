package com.mihaineagu.data.api.v1.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountryDTO {

    private String countryName;
    private String uri;
    private RegionListDTO regionDTO;
}
