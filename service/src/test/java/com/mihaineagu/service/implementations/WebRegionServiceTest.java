package com.mihaineagu.service.implementations;

import com.mihaineagu.data.api.v1.mappers.RegionMapper;
import com.mihaineagu.data.api.v1.models.LocationDTO;
import com.mihaineagu.data.api.v1.models.RegionDTO;
import com.mihaineagu.data.domain.Region;
import com.mihaineagu.data.repository.RegionRepository;
import com.mihaineagu.service.interfaces.LocationService;
import com.mihaineagu.service.interfaces.RegionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WebRegionServiceTest {


    public static final String MOCK = "/mock/";
    public static final String REG_NAME1 = "RegName";
    public static final long ID1 = 1L;
    public static final long ID = 2L;
    @Mock
    RegionRepository regionRepository;
    @Mock
    LocationService locationService;
    RegionMapper regionMapper;

    RegionService regionService;

    @BeforeEach
    void setUp() {
        regionMapper = RegionMapper.INSTANCE;
        locationService = mock(LocationService.class);
        regionRepository = mock(RegionRepository.class);

        regionService = new WebRegionService(regionRepository, regionMapper, locationService);
        regionService.setUri(MOCK);
    }

    @Test
    void getAllRegionsWithoutLocations() {
        Region region = new Region();
        region.setRegionName(REG_NAME1);
        region.setId(ID1);

        Region region1 = new Region();
        region1.setRegionName(REG_NAME1);
        region1.setId(ID1);

        List<Region> regionList = List.of(region1, region);

        when(regionRepository.findAll()).thenReturn(regionList);

        List<RegionDTO> regionDTOList = regionService.getAllRegionsWithoutLocations();

        assertEquals(regionList.size(), regionDTOList.size());
        assertEquals(regionMapper.regionToDTO(region), regionDTOList.get(0));
        assertEquals(regionMapper.regionToDTO(region1), regionDTOList.get(1));
    }

    @Test
    void getAllRegionsWithLocation() {
        Region region = new Region();
        region.setRegionName(REG_NAME1);
        region.setId(ID1);

        Region region1 = new Region();
        region1.setRegionName(REG_NAME1);
        region1.setId(ID);

        LocationDTO locationDTO1 = LocationDTO.builder().build();
        LocationDTO locationDTO2 = LocationDTO.builder().build();

        when(regionRepository.findAll()).thenReturn(List.of(region, region1));

        when(locationService.findByRegionIdWithSports(ID1)).thenReturn(List.of(locationDTO1, locationDTO2));
        when(locationService.findByRegionIdWithSports(ID)).thenReturn(List.of(locationDTO1));

        List<RegionDTO> regionDTOList = regionService.getAllRegionsWithLocation();

        assertEquals(2, regionDTOList.size());
        assertEquals(2, regionDTOList.get(0).getLocation().getLocationListDTO().size());
        assertEquals(1, regionDTOList.get(1).getLocation().getLocationListDTO().size());
    }
}