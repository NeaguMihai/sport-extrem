package com.mihaineagu.data.api.v1.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RegionListDTO {

    private List<RegionDTO> regionDTOList;
}
