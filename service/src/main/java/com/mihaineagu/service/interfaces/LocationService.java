package com.mihaineagu.service.interfaces;

import com.mihaineagu.data.api.v1.models.LocationDTO;

import java.util.List;
import java.util.Optional;

public interface LocationService {

    List<LocationDTO> findAllWithoutSports();

    Optional<LocationDTO> findByIdWithSports(Long id);

    Optional<LocationDTO> findByIdWithoutSports(Long id);

    List<LocationDTO> findByRegionIdWithoutSports(Long regionId);

    List<LocationDTO> findByRegionIdWithSports(Long regionId);

    void setUri(String locationUri, String sportUri);
}
