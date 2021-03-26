package com.mihaineagu.data.api.v1.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionListDTO {

    private List<RegionDTO> regionDTOList;
}
