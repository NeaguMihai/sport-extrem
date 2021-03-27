package com.mihaineagu.service.interfaces;

import com.mihaineagu.data.api.v1.models.LocationDTO;
import com.mihaineagu.data.api.v1.models.RegionDTO;

import java.util.List;
import java.util.Optional;

public interface LocationService {

    List<LocationDTO> findAllWithoutSports();

    Optional<LocationDTO> findByIdWithSports(Long id);

    Optional<LocationDTO> findByIdWithoutSports(Long id);

    Boolean findIfExistent(LocationDTO locationDTO, Long id);

    List<LocationDTO> findByRegionIdWithoutSports(Long regionId);

    List<LocationDTO> findByRegionIdWithSports(Long regionId);

    Optional<LocationDTO> saveLocation(LocationDTO locationDTO, RegionDTO regionDTO);

    void setUri(String locationUri, String sportUri);
}
