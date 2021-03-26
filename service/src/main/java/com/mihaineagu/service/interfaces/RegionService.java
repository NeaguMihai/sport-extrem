package com.mihaineagu.service.interfaces;

import com.mihaineagu.data.api.v1.models.RegionDTO;

import java.util.List;

public interface RegionService {

    void setUri(String uri);

    List<RegionDTO> getAllRegionsWithoutLocations();

    List<RegionDTO> getAllRegionsWithLocation();

    List<RegionDTO> getByCountryIdWIthLocation(Long id);
}
