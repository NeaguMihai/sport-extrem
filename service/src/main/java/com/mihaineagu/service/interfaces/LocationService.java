package com.mihaineagu.service.interfaces;

import com.mihaineagu.data.api.v1.models.LocationDTO;

import java.util.List;

public interface LocationService {

    List<LocationDTO> findAllWithoutSports();

    List<LocationDTO> findAllWithSports();

    List<LocationDTO> findByRegionIdWithSports(Long regionId);

    void SetUri(String locationUri, String sportUri);
}
