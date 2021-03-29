package com.mihaineagu.data.api.v1.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegionDTO {

    @NotEmpty
    @Size(min = 1, message = "The name cannot be empty")
    private String regionName;
    private String uri;
    private List<LocationDTO> locations;

}
