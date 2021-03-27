package com.mihaineagu.service.implementations;

import com.mihaineagu.data.api.v1.mappers.LocationMapper;
import com.mihaineagu.data.api.v1.mappers.RegionMapper;
import com.mihaineagu.data.api.v1.models.LocationDTO;
import com.mihaineagu.data.api.v1.models.RegionDTO;
import com.mihaineagu.data.domain.Location;
import com.mihaineagu.data.repository.LocationRepository;
import com.mihaineagu.service.interfaces.LocationService;
import com.mihaineagu.service.interfaces.SportService;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("webLocationService")
public class WebLocationService implements LocationService {

    private String locationUri;
    private String sportUri;
    private final LocationMapper locationMapper;
    private final LocationRepository locationRepository;
    private final RegionMapper regionMapper;
    private final SportService sportService;

    public WebLocationService(LocationMapper locationMapper, LocationRepository locationRepository, RegionMapper regionMapper, SportService sportService) {
        this.regionMapper = regionMapper;
        locationUri = "";
        sportUri = "";
        this.locationMapper = locationMapper;
        this.locationRepository = locationRepository;
        this.sportService = sportService;
    }

    @Override
    public void setUri(String locationUri, String sportUri) {
        this.locationUri = locationUri;
        this.sportUri = sportUri;
        this.sportService.setUri(sportUri);
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
    public Optional<LocationDTO> findByIdWithSports(Long id) {
        return locationRepository.findById(id)
                .map(location -> {
                   LocationDTO locationDTO = locationMapper.locationToDTO(location);
                   locationDTO.setSports(sportService.getSportAndInformationDTO(location.getId()));
                   locationDTO.setUri(locationUri + locationDTO.getUri());
                   return locationDTO;
                });
    }

    @Override
    public Optional<LocationDTO> findByIdWithoutSports(Long id) {
        return locationRepository.findById(id)
                .map(locationMapper::locationToDTO)
                .map(locationDTO -> {
                    locationDTO.setUri(locationUri + locationDTO.getUri());
                    return locationDTO;
                });
    }

    @Override
    public Boolean findIfExistent(LocationDTO locationDTO, Long id) {
        Optional<Location> location = locationRepository.findLocationByRegionIdAndLocationName(id, locationDTO.getLocationName());

        return location.isPresent();
    }

    @Override
    public List<LocationDTO> findByRegionIdWithoutSports(Long regionId) {
        List<LocationDTO> locationDTOList = new LinkedList<>();
        locationRepository.findByRegionId(regionId).forEach(location -> {
            LocationDTO locationDTO = locationMapper.locationToDTO(location);

            locationDTO.setUri(locationUri + locationDTO.getUri());
            locationDTOList.add(locationDTO);
        });
        return locationDTOList;
    }

    @Override
    public List<LocationDTO> findByRegionIdWithSports(Long regionId) {
        List<LocationDTO> locationDTOList = new LinkedList<>();
        locationRepository.findByRegionId(regionId).forEach(location -> {
            LocationDTO locationDTO = locationMapper.locationToDTO(location);
            locationDTO.setSports(sportService.getSportAndInformationDTO(location.getId()));
            locationDTO.setUri(locationUri + locationDTO.getUri());
            locationDTOList.add(locationDTO);
        });
        return locationDTOList;
    }

    @Override
    public Optional<LocationDTO> saveLocation(LocationDTO locationDTO, RegionDTO regionDTO) {
        Location toBeSaved = locationMapper.DTOTOLocation(locationDTO);
        toBeSaved.setRegion(regionMapper.DTOToRegion(regionDTO));
        Optional<Location> saved = Optional.of(locationRepository.save(toBeSaved));

        return saved.map(locationMapper::locationToDTO);

    }
}
