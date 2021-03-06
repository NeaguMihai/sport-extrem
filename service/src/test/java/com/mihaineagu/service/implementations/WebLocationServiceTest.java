package com.mihaineagu.service.implementations;

import com.mihaineagu.data.api.v1.mappers.LocationMapper;
import com.mihaineagu.data.api.v1.mappers.RegionMapper;
import com.mihaineagu.data.api.v1.models.LocationDTO;
import com.mihaineagu.data.domain.Location;
import com.mihaineagu.data.repository.LocationRepository;
import com.mihaineagu.service.interfaces.InformationService;
import com.mihaineagu.service.interfaces.LocationService;
import com.mihaineagu.service.interfaces.SportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WebLocationServiceTest {


    public static final String URI = "/mock/";
    public static final String LOCATION_1 = "Location1";
    public static final long ID = 1L;
    public static final long ID1 = 2L;
    public static final String SPORT_1 = "Sport1";
    public static final String SPORT_2 = "Sport2";
    LocationService locationService;
    LocationMapper locationMapper;
    RegionMapper regionMapper;
    @Mock
    LocationRepository locationRepository;
    @Mock
    SportService sportService;

    @Mock
    InformationService informationService;

    @BeforeEach
    void setUp() {
        locationMapper = LocationMapper.INSTANCE;

        locationRepository = mock(LocationRepository.class);
        sportService = mock(SportService.class);

        locationService = new WebLocationService(locationMapper, locationRepository, regionMapper, sportService, informationService);

    }

    @Test
    void findAllWithoutSports() {

        Location location1 = new Location();
        location1.setLocationName(LOCATION_1);
        location1.setId(ID);

        Location location2 = new Location();
        location1.setLocationName(LOCATION_1);
        location1.setId(ID1);

        List<Location> locations = List.of(location1, location2);

        when(locationRepository.findAll()).thenReturn(locations);

        List<LocationDTO> locationDTOList = locationService.findAllWithoutSports();

        assertEquals(locations.size(), locationDTOList.size());
        assertEquals(locationMapper.locationToDTO(location1),locationDTOList.get(0));
        assertEquals(locationMapper.locationToDTO(location2),locationDTOList.get(1));
    }



}