package com.mihaineagu.web.controllers;

import com.mihaineagu.data.api.v1.models.CountryDTO;
import com.mihaineagu.data.api.v1.models.CountryListDTO;
import com.mihaineagu.data.api.v1.models.RegionDTO;
import com.mihaineagu.data.api.v1.models.RegionListDTO;
import com.mihaineagu.service.interfaces.CountryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CountryController.class)
@AutoConfigureDataJpa
class CountryControllerTest extends AbstractControllerTest{

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CountryService countryService;

    @Test
    void getAllCountriesWithRegionsTest() throws Exception {
        List<CountryDTO> countryDTOList = List.of(
                CountryDTO
                        .builder()
                        .uri(URI)
                        .regionDTO(new RegionListDTO(List.of(new RegionDTO(), new RegionDTO())))
                        .build(),
                CountryDTO
                        .builder()
                        .uri(URI)
                        .regionDTO(new RegionListDTO(List.of(new RegionDTO(), new RegionDTO())))
                        .build());

        when(countryService.findAllCountriesWithRegion()).thenReturn(countryDTOList);

        mockMvc.perform(get("/api/v1/countries")
                .contentType(MediaType.APPLICATION_JSON)
                .param("region","true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryDTOList", hasSize(2)))
                .andExpect(jsonPath("$.countryDTOList[0].regionDTO.regionDTOList", hasSize(2)));
    }
    @Test
    void getAllCountriesWithoutRegionsTest() throws Exception {
        List<CountryDTO> countryDTOList = List.of(
                CountryDTO
                        .builder()
                        .uri(URI)
                        .build(),
                CountryDTO
                        .builder()
                        .uri(URI)
                        .build());

        when(countryService.findAllCountriesNoRegion()).thenReturn(countryDTOList);

        mockMvc.perform(get("/api/v1/countries")
                .contentType(MediaType.APPLICATION_JSON)
                .param("region", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryDTOList", hasSize(2)))
                .andExpect(jsonPath("$.countryDTOList[0].regionDTO", nullValue()));
    }

    @Test
    void getCountryByIdWithRegionFoundTest() throws Exception {

        Optional<CountryDTO> countryDTO = Optional.of(
                CountryDTO
                        .builder()
                        .uri(URI)
                        .regionDTO(new RegionListDTO(Collections.singletonList(RegionDTO.builder().build())))
                        .build());
        when(countryService.findCountryByIdWithRegion(anyLong())).thenReturn(countryDTO);

        mockMvc.perform(get("/api/v1/countries/1")
                .contentType(MediaType.APPLICATION_JSON)
                .param("region","true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uri", equalTo(URI)))
                .andExpect(jsonPath("$.regionDTO.regionDTOList", hasSize(1)));
    }
    @Test
    void getCountryByIdWithoutRegionFoundTest() throws Exception {

        Optional<CountryDTO> countryDTO = Optional.of(
                CountryDTO
                        .builder()
                        .uri(URI)
                        .build());
        when(countryService.findCountryByIdWithoutRegion(anyLong())).thenReturn(countryDTO);

        mockMvc.perform(get("/api/v1/countries/1")
                .contentType(MediaType.APPLICATION_JSON)
                .param("region","false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uri", equalTo(URI)))
                .andExpect(jsonPath("$.regionDTO", nullValue()));
    }
}