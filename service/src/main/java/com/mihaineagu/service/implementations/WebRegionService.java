package com.mihaineagu.service.implementations;

import com.mihaineagu.data.api.v1.mappers.RegionMapper;
import com.mihaineagu.data.api.v1.models.LocationDTO;
import com.mihaineagu.data.api.v1.models.LocationListDTO;
import com.mihaineagu.data.api.v1.models.RegionDTO;
import com.mihaineagu.data.api.v1.models.SportListDTO;
import com.mihaineagu.data.repository.RegionRepository;
import com.mihaineagu.service.interfaces.LocationService;
import com.mihaineagu.service.interfaces.RegionService;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WebRegionService implements RegionService {

    private String uri;
    private final RegionRepository regionRepository;
    private final RegionMapper regionMapper;
    private final LocationService locationService;

    public WebRegionService(RegionRepository regionRepository, RegionMapper regionMapper, LocationService locationService) {
        uri = "";
        this.regionRepository = regionRepository;
        this.regionMapper = regionMapper;
        this.locationService = locationService;
    }

    @Override
    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public List<RegionDTO> findAllRegionsWithoutLocations() {
        return regionRepository
                .findAll()
                .stream()
                .map(regionMapper::regionToDTO)
                .map(regionDTO -> {
                    regionDTO.setUri(uri + regionDTO.getUri());
                    return regionDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<RegionDTO> findByCountryIdWithoutLocation(Long id) {
        List<RegionDTO> regionDTOList = new LinkedList<>();
        regionRepository.findByCountryId(id)
                .forEach(region -> {
                    RegionDTO regionDTO = regionMapper.regionToDTO(region);
                    regionDTO.setUri(uri + regionDTO.getUri());
                    regionDTOList.add(regionDTO);
                });
        return regionDTOList;
    }

    @Override
    public List<RegionDTO> findByCountryIdWIthLocation(Long id) {
        List<RegionDTO> regionDTOList = new LinkedList<>();
        regionRepository.findByCountryId(id)
                .forEach(region -> {
                    LocationListDTO locationListDTO;
                    locationListDTO = new LocationListDTO(locationService.findByRegionIdWithoutSports(region.getId()));
                    RegionDTO regionDTO = regionMapper.regionToDTO(region);
                    regionDTO.setLocation(locationListDTO);
                    regionDTO.setUri(uri + regionDTO.getUri());
                    regionDTOList.add(regionDTO);
                });
        return regionDTOList;
    }

    @Override
    public Optional<RegionDTO> findByIdWithLocation(Long id) {
        return regionRepository.findById(id)
                .map(region -> {
                    RegionDTO regionDTO = regionMapper.regionToDTO(region);
                    regionDTO.setLocation(new LocationListDTO(locationService.findByRegionIdWithoutSports(region.getId())));
                    regionDTO.setUri(uri + regionDTO.getUri());
                    return regionDTO;
                });
    }

    @Override
    public Optional<RegionDTO> findByIdWithoutLocation(Long id) {
        return regionRepository.findById(id)
                .map(region -> {
                    RegionDTO regionDTO = regionMapper.regionToDTO(region);
                    regionDTO.setUri(uri + regionDTO.getUri());
                    return regionDTO;
                });
    }


}
