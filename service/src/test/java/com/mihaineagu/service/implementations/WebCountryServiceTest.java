package com.mihaineagu.service.implementations;

import com.mihaineagu.data.api.v1.mappers.CountryMapper;
import com.mihaineagu.data.api.v1.models.CountryDTO;
import com.mihaineagu.data.api.v1.models.RegionDTO;
import com.mihaineagu.data.domain.Country;
import com.mihaineagu.data.repository.CountryRepository;
import com.mihaineagu.service.interfaces.CountryService;
import com.mihaineagu.service.interfaces.RegionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class WebCountryServiceTest {

    public static final String URI = "/mock/";
    public static final String COUNTRY_1 = "country1";
    public static final long ID1 = 1L;
    public static final String COUNTRY_2 = "country2";
    public static final long ID2 = 2L;
    public static final String REGION_1 = "region1";
    @Mock
    CountryRepository countryRepository;
    @Mock
    RegionService regionService;
    CountryMapper countryMapper;

    CountryService countryService;

    @BeforeEach
    void setUp() {
        countryRepository = mock(CountryRepository.class);
        regionService = mock(RegionService.class);

        countryMapper = CountryMapper.INSTANCE;

        countryService = new WebCountryService(countryRepository, regionService, countryMapper);
        countryService.setUri(URI);
    }

    @Test
    void findAllCountriesNoRegion() {

        Country country1 = new Country();
        country1.setCountryName(COUNTRY_1);
        country1.setId(ID1);
        Country country2 = new Country();
        country2.setCountryName(COUNTRY_2);
        country2.setId(ID2);

        List<Country> countryList = List.of(country1, country2);

        when(countryRepository.findAll()).thenReturn(countryList);

        List<CountryDTO> countryDTOList = countryService.findAllCountriesNoRegion();

        assertEquals(countryList.size(), countryDTOList.size());
        assertEquals(country1, countryMapper.DTOTOCountry(countryDTOList.get(0)));
        assertEquals(country2, countryMapper.DTOTOCountry(countryDTOList.get(1)));

    }

    @Test
    void findAllCountriesWithRegion() {
        Country country1 = new Country();
        country1.setCountryName(COUNTRY_1);
        country1.setId(ID1);
        Country country2 = new Country();
        country2.setCountryName(COUNTRY_2);
        country2.setId(ID2);

        RegionDTO regionDTO1 = RegionDTO
                .builder()
                .regionName(REGION_1)
                .build();
        RegionDTO regionDTO2 = RegionDTO
                .builder()
                .uri(URI+ID1)
                .build();

        when(countryRepository.findAll()).thenReturn(List.of(country1, country2));

        when(regionService.findByCountryIdWIthLocation(ID1)).thenReturn(List.of(regionDTO1, regionDTO2));
        when(regionService.findByCountryIdWIthLocation(ID2)).thenReturn(Collections.singletonList(regionDTO1));

        List<CountryDTO> countryDTOList = countryService.findAllCountriesWithRegion();

        assertEquals(2, countryDTOList.size());
        assertEquals(2, countryDTOList.get(0).getRegionDTO().getRegionDTOList().size());
        assertEquals(1, countryDTOList.get(1).getRegionDTO().getRegionDTOList().size());


    }
}