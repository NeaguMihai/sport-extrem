package com.mihaineagu.service.implementations;

import com.mihaineagu.data.api.v1.mappers.CountryMapper;
import com.mihaineagu.data.api.v1.mappers.RegionMapper;
import com.mihaineagu.data.api.v1.models.*;
import com.mihaineagu.data.domain.Region;
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
    private final CountryMapper countryMapper;
    private final LocationService locationService;

    public WebRegionService(RegionRepository regionRepository, RegionMapper regionMapper, CountryMapper countryMapper, LocationService locationService) {
        uri = "";
        this.countryMapper = countryMapper;
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
                    RegionDTO regionDTO = regionMapper.regionToDTO(region);
                    regionDTO.setLocations(locationService.findByRegionIdWithoutSports(region.getId()));
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
                    regionDTO.setLocations(locationService.findByRegionIdWithoutSports(region.getId()));
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

    @Override
    public Boolean findIfExists(RegionDTO regionDTO, Long id) {

        Optional<Region> returned = regionRepository.findRegionByCountryIdAndRegionName(id, regionDTO.getRegionName());

        return returned.isPresent();
    }

    @Override
    public Optional<RegionDTO> saveRegion(RegionDTO region, CountryDTO country) {
        Region toBeSaved = regionMapper.DTOToRegion(region);
        toBeSaved.setCountry(countryMapper.DTOTOCountry(country));
        Optional<Region> returned = Optional.of(regionRepository.save(toBeSaved));

        return returned.map(regionMapper::regionToDTO);
    }


}
