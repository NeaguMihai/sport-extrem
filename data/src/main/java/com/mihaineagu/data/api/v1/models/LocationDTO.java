package com.mihaineagu.data.api.v1.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {

    private String locationName;
    private String uri;
    private SportListDTO sport;

}
