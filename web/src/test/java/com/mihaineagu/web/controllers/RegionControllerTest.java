package com.mihaineagu.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mihaineagu.data.api.v1.models.LocationDTO;
import com.mihaineagu.data.api.v1.models.LocationListDTO;
import com.mihaineagu.data.api.v1.models.RegionDTO;
import com.mihaineagu.service.interfaces.RegionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static com.mihaineagu.web.controllers.AbstractControllerTest.URI;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RegionController.class)
@AutoConfigureDataJpa
class RegionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RegionService regionService;

    @Test
    void getAllRegionsByCountryWithLocationsTest() throws Exception {

        List<RegionDTO> regionDTOList  = List.of(
                RegionDTO
                        .builder()
                        .uri(URI)
                        .location(
                                new LocationListDTO(List.of(
                                        LocationDTO.builder().build(),
                                        LocationDTO.builder().build()))
                        )
                        .build());
        when(regionService.findByCountryIdWIthLocation(anyLong())).thenReturn(regionDTOList);

        mockMvc.perform(get("/api/v1/countries/1/regions")
                .contentType(MediaType.APPLICATION_JSON)
                .param("location","true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.regionDTOList", hasSize(1)))
                .andExpect(jsonPath("$.regionDTOList[0].location.locationListDTO", hasSize(2)));
    }
    @Test
    void getAllRegionsByCountryWithoutLocationsTest() throws Exception {
        List<RegionDTO> regionDTOList  = List.of(
                RegionDTO
                        .builder()
                        .uri(URI)
                        .build());
        when(regionService.findByCountryIdWithoutLocation(anyLong())).thenReturn(regionDTOList);

        mockMvc.perform(get("/api/v1/countries/1/regions")
                .contentType(MediaType.APPLICATION_JSON)
                .param("location","false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.regionDTOList", hasSize(1)))
                .andExpect(jsonPath("$.regionDTOList[0].location", nullValue()));
    }

    @Test
    void getRegionByIdWithLocationFoundTest() {

        Optional<RegionDTO> regionDTO = Optional.of(
                RegionDTO
                        .builder()
                        .regionName("region1")
                        .uri(URI)
                        .location(new LocationListDTO(List.of(new LocationDTO())))
                        .build());

        when(regionService.findByIdWithLocation(anyLong())).thenReturn(regionDTO);

        Optional<RegionDTO> returned = regionService.findByIdWithLocation(anyLong());

        assertTrue(returned.isPresent());
        assertEquals(regionDTO.get(), returned.get());
    }
    @Test
    void getRegionByIdNotFoundTest() {

        when(regionService.findByIdWithLocation(anyLong())).thenReturn(Optional.empty());

        Optional<RegionDTO> returned = regionService.findByIdWithLocation(anyLong());

        assertTrue(returned.isEmpty());
    }
}