package com.mihaineagu.service.interfaces;

import com.mihaineagu.data.api.v1.models.LocationDTO;
import com.mihaineagu.data.api.v1.models.RegionDTO;
import com.mihaineagu.data.domain.Location;

import java.util.List;
import java.util.Optional;

public interface LocationService {

    List<LocationDTO> findAllWithoutSports();

    Optional<LocationDTO> findByIdWithSports(Long id);

    Optional<LocationDTO> findByIdWithoutSports(Long id);

    Optional<Location> findLocation(Long id);

    Optional<Location> findById(Long id);

    Boolean findIfExistent(LocationDTO locationDTO, Long id);

    List<LocationDTO> findByRegionIdWithoutSports(Long regionId);

    List<LocationDTO> findByRegionIdWithSports(Long regionId);

    Optional<LocationDTO> saveLocation(LocationDTO locationDTO, RegionDTO regionDTO);

    Optional<LocationDTO> saveLocation(Location location);

    void deleteLocation(Long id);

    void setUri(String locationUri, String sportUri);
}
