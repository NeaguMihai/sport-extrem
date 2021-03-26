package com.mihaineagu.service.implementations;

import com.mihaineagu.data.api.v1.mappers.LocationMapper;
import com.mihaineagu.data.api.v1.models.LocationDTO;
import com.mihaineagu.data.api.v1.models.SportListDTO;
import com.mihaineagu.data.repository.LocationRepository;
import com.mihaineagu.service.interfaces.LocationService;
import com.mihaineagu.service.interfaces.SportService;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service("webLocationService")
public class WebLocationService implements LocationService {

    private String locationUri;
    private String sportUri;
    private final LocationMapper locationMapper;
    private final LocationRepository locationRepository;
    private final SportService sportService;

    public WebLocationService(LocationMapper locationMapper, LocationRepository locationRepository, SportService sportService) {
        locationUri = "";
        sportUri = "";
        this.locationMapper = locationMapper;
        this.locationRepository = locationRepository;
        this.sportService = sportService;
    }

    @Override
    public void SetUri(String locationUri, String sportUri) {
        this.locationUri = locationUri;
        this.sportUri = sportUri;
    }

    @Override
    public List<LocationDTO> findAllWithoutSports() {
        return locationRepository.findAll()
                .stream()
                .map(locationMapper::locationToDTO)
                .map(locationDTO -> {
                    locationDTO.setUri(locationUri + locationDTO.getUri());
                    return locationDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<LocationDTO> findAllWithSports() {
        List<LocationDTO> locationDTOList = new LinkedList<>();
        locationRepository.findAll().forEach(location -> {
            LocationDTO locationDTO = locationMapper.locationToDTO(location);
            locationDTO.setSport(
                    new SportListDTO(
                            sportService.getSportAndInformationDTO(
                                    location.getId())
                                    .stream()
                                    .map(sportDTO -> {
                                     sportDTO.setUri(sportUri + sportDTO.getUri());
                                     return sportDTO; })
                                    .collect(Collectors.toList())));
            locationDTOList.add(locationDTO);
        });
        return locationDTOList;
    }

    @Override
    public List<LocationDTO> findByRegionIdWithSports(Long regionId) {
        List<LocationDTO> locationDTOList = new LinkedList<>();
        locationRepository.findByRegionId(regionId).forEach(location -> {
            LocationDTO locationDTO = locationMapper.locationToDTO(location);
            locationDTO.setSport(
                    new SportListDTO(
                            sportService.getSportAndInformationDTO(
                                    location.getId())
                                    .stream()
                                    .map(sportDTO -> {
                                        sportDTO.setUri(sportUri + sportDTO.getUri());
                                        return sportDTO; })
                                    .collect(Collectors.toList())));
            locationDTOList.add(locationDTO);
        });
        return locationDTOList;
    }
}
