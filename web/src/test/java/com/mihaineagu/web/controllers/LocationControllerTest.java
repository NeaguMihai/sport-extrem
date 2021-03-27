package com.mihaineagu.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mihaineagu.data.api.v1.models.LocationDTO;

import com.mihaineagu.data.api.v1.models.SportListDTO;
import com.mihaineagu.service.interfaces.LocationService;
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

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LocationController.class)
@AutoConfigureDataJpa
class LocationControllerTest extends AbstractControllerTest{


    @MockBean
    LocationService locationService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getLocationByRegionWithoutSportTest() throws Exception {
        List<LocationDTO> locationDTOList = List.of(LocationDTO.builder().uri("Mock").build(),
                LocationDTO.builder().uri("Mock1").build(), LocationDTO.builder().uri("Mock2").build());

        when(locationService.findByRegionIdWithoutSports(any())).thenReturn(locationDTOList);

        mockMvc.perform(get("/api/v1/regions/1/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .param("sports","false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.locationListDTO", hasSize(3)))
                .andExpect(jsonPath("$.locationListDTO[0].sport", nullValue()))
                .andExpect(jsonPath("$.locationListDTO[0].sport", nullValue()))
                .andExpect(jsonPath("$.locationListDTO[0].sport", nullValue()));

    }

    @Test
    void getLocationByRegionWithSportTest() throws Exception {
        List<LocationDTO> locationDTOList = List.of(LocationDTO.builder().sport(new SportListDTO()).build(),
                LocationDTO.builder().sport(new SportListDTO()).build(), LocationDTO.builder().sport(new SportListDTO()).build());

        when(locationService.findByRegionIdWithoutSports(any())).thenReturn(locationDTOList);

        mockMvc.perform(get("/api/v1/regions/1/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .param("sports","false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.locationListDTO", hasSize(3)))
                .andExpect(jsonPath("$.locationListDTO[0].sport",notNullValue()))
                .andExpect(jsonPath("$.locationListDTO[1].sport",notNullValue()))
                .andExpect(jsonPath("$.locationListDTO[2].sport",notNullValue()));

    }

    @Test
    void getLocationIdFoundTest() throws Exception {
        LocationDTO locationDTO = LocationDTO
                .builder()
                .uri(URI)
                .build();

        when(locationService.findByIdWithoutSports(anyLong())).thenReturn(Optional.of(locationDTO));

        mockMvc.perform(get("/api/v1/locations/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uri", equalTo(URI)));
    }
    @Test
    void getLocationIdNotFoundTest() throws Exception {

        when(locationService.findByIdWithoutSports(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/locations/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorMessage", equalTo("Requested entity not found!")));
    }
}