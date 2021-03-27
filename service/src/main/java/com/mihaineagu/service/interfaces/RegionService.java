package com.mihaineagu.service.interfaces;

import com.mihaineagu.data.api.v1.models.CountryDTO;
import com.mihaineagu.data.api.v1.models.RegionDTO;
import com.mihaineagu.data.domain.Country;
import com.mihaineagu.data.domain.Region;

import java.util.List;
import java.util.Optional;

public interface RegionService {

    void setUri(String uri);

    List<RegionDTO> findAllRegionsWithoutLocations();

    List<RegionDTO> findByCountryIdWithoutLocation(Long id);

    List<RegionDTO> findByCountryIdWIthLocation(Long id);

    Optional<RegionDTO> findByIdWithLocation(Long id);

    Optional<RegionDTO> findByIdWithoutLocation(Long id);

    Boolean findIfExists(RegionDTO regionDTO, Long id);

    Optional<RegionDTO> saveRegion(RegionDTO region, CountryDTO country);
}
