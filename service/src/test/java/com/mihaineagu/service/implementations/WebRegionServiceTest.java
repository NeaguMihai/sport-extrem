package com.mihaineagu.service.implementations;

import com.mihaineagu.data.api.v1.mappers.CountryMapper;
import com.mihaineagu.data.api.v1.mappers.RegionMapper;
import com.mihaineagu.data.api.v1.models.CountryDTO;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WebRegionServiceTest {


    public static final String MOCK = "/mock/";
    public static final String REG_NAME1 = "RegName";
    public static final long ID1 = 1L;
    public static final long ID = 2L;
    public static final String URI = "/mock/1";
    @Mock
    RegionRepository regionRepository;
    @Mock
    LocationService locationService;
    RegionMapper regionMapper;
    CountryMapper countryMapper;
    RegionService regionService;

    @BeforeEach
    void setUp() {
        regionMapper = RegionMapper.INSTANCE;
        countryMapper = CountryMapper.INSTANCE;
        locationService = mock(LocationService.class);
        regionRepository = mock(RegionRepository.class);

        regionService = new WebRegionService(regionRepository, regionMapper, countryMapper, locationService);
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

        List<RegionDTO> regionDTOList = regionService.findAllRegionsWithoutLocations();

        assertEquals(regionList.size(), regionDTOList.size());
        assertEquals(MOCK + region.getId(), regionDTOList.get(0).getUri());
        assertEquals(MOCK + region1.getId(), regionDTOList.get(1).getUri());
    }

    @Test
    void getAllByCountryIdWithLocation() {
        Region region = new Region();
        region.setRegionName(REG_NAME1);
        region.setId(ID1);

        Region region1 = new Region();
        region1.setRegionName(REG_NAME1);
        region1.setId(ID);

        when(regionRepository.findByCountryId(anyLong())).thenReturn(List.of(region, region1));


        List<RegionDTO> regionDTOList = regionService.findByCountryIdWithoutLocation(anyLong());

        assertEquals(2, regionDTOList.size());

    }

    @Test
    void findByIdWithLocationTest() {
        Region region = new Region();
        region.setId(ID);

        when(regionRepository.findById(anyLong())).thenReturn(Optional.of(region));

        when(locationService.findByRegionIdWithoutSports(anyLong())).thenReturn(List.of(new LocationDTO(), new LocationDTO()));

        Optional<RegionDTO> regionDTO = regionService.findByIdWithLocation(anyLong());

        assertTrue(regionDTO.isPresent());
        assertEquals(region, regionMapper.DTOToRegion(regionDTO.get()));
        assertEquals(2, regionDTO.get().getLocations().size());
    }

    @Test
    void findByIdWithoutLocationTest() {
        Region region = new Region();
        region.setId(ID);

        when(regionRepository.findById(anyLong())).thenReturn(Optional.of(region));

        Optional<RegionDTO> regionDTO = regionService.findByIdWithoutLocation(anyLong());

        assertTrue(regionDTO.isPresent());
        assertEquals(region, regionMapper.DTOToRegion(regionDTO.get()));
        assertNull(regionDTO.get().getLocations());
    }

    @Test
    void findByIdEmptyOptionalTest() {
        when(regionRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<RegionDTO> regionDTO = regionService.findByIdWithoutLocation(anyLong());

        assertTrue(regionDTO.isEmpty());
    }

    @Test
    void addRegionSuccessTest() {

        RegionDTO toBeSaved = RegionDTO
                .builder()
                .regionName(REG_NAME1)
                .build();

        CountryDTO country  = new CountryDTO();
        country.setUri(URI);

        when(regionRepository.findRegionByCountryIdAndRegionName(anyLong(), anyString())).thenReturn(Optional.empty());
        when(regionRepository.save(any())).thenReturn(regionMapper.DTOToRegion(toBeSaved));

        Boolean exists = regionService.findIfExists(toBeSaved, ID);

        assertFalse(exists);

        Optional<RegionDTO> returned = regionService.saveRegion(toBeSaved, country);

        assertTrue(returned.isPresent());
        assertEquals(toBeSaved.toString(), returned.get().toString());
    }
}