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

    private final String LOCATION_URI;
    private final String SPORT_URI;
    private final LocationMapper locationMapper;
    private final LocationRepository locationRepository;
    private final SportService sportService;

    public WebLocationService(String location_uri, String sport_uri, LocationMapper locationMapper, LocationRepository locationRepository, SportService sportService) {
        LOCATION_URI = location_uri;
        SPORT_URI = sport_uri;
        this.locationMapper = locationMapper;
        this.locationRepository = locationRepository;
        this.sportService = sportService;
    }

    @Override
    public List<LocationDTO> findAllWithoutSports() {
        return locationRepository.findAll()
                .stream()
                .map(locationMapper::locationToDTO)
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
                                    location.getId())));
            locationDTOList.add(locationDTO);
        });
        return locationDTOList;
    }
}
