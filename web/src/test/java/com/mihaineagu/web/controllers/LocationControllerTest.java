package com.mihaineagu.web.controllers;

import com.mihaineagu.data.api.v1.models.CountryDTO;
import com.mihaineagu.data.api.v1.models.LocationDTO;

import com.mihaineagu.data.api.v1.models.RegionDTO;
import com.mihaineagu.service.interfaces.LocationService;
import com.mihaineagu.service.interfaces.RegionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LocationController.class)
@AutoConfigureDataJpa
class LocationControllerTest extends AbstractControllerTest{


    public static final String LOCATION = "Location";
    @MockBean
    LocationService locationService;

    @MockBean
    RegionService regionService;

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
                .andExpect(jsonPath("$.locationListDTO[0].sports", nullValue()))
                .andExpect(jsonPath("$.locationListDTO[0].sports", nullValue()))
                .andExpect(jsonPath("$.locationListDTO[0].sports", nullValue()));

    }

    @Test
    void getLocationByRegionWithSportTest() throws Exception {
        List<LocationDTO> locationDTOList = List.of(LocationDTO.builder().sports(Collections.emptyList()).build(),
                LocationDTO.builder().sports(Collections.emptyList()).build(),
                LocationDTO.builder().sports(Collections.emptyList()).build());

        when(locationService.findByRegionIdWithoutSports(any())).thenReturn(locationDTOList);

        mockMvc.perform(get("/api/v1/regions/1/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .param("sports","false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.locationListDTO", hasSize(3)))
                .andExpect(jsonPath("$.locationListDTO[0].sports",notNullValue()))
                .andExpect(jsonPath("$.locationListDTO[1].sports",notNullValue()))
                .andExpect(jsonPath("$.locationListDTO[2].sports",notNullValue()));

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


    @Test
    void addNewLocationMissingRegionTest() throws Exception {

        LocationDTO locationDTO  = LocationDTO.builder().locationName(LOCATION).build();

        when(regionService.findByIdWithoutLocation(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/regions/1/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(locationDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorMessage", equalTo(NOT_FOUND)));

    }

    @Test
    void addNewLocationAlreadyExistsTest() throws Exception {

        LocationDTO locationDTO  = LocationDTO.builder().locationName(LOCATION).build();

        when(regionService.findByIdWithoutLocation(anyLong())).thenReturn(Optional.of(new RegionDTO()));
        when(locationService.findIfExistent(ArgumentMatchers.any(),anyLong())).thenReturn(Boolean.TRUE);

        mockMvc.perform(post("/api/v1/regions/1/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(locationDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorMessage", equalTo(EXISTS_ERROR)));

    }

    @Test
    void addNewLocationNotSavedTest() throws Exception {

        LocationDTO locationDTO  = LocationDTO.builder().locationName(LOCATION).build();

        when(regionService.findByIdWithoutLocation(anyLong())).thenReturn(Optional.of(new RegionDTO()));
        when(locationService.findIfExistent(ArgumentMatchers.any(),anyLong())).thenReturn(Boolean.FALSE);

        when(locationService.saveLocation(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/regions/1/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(locationDTO)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.errorMessage", equalTo(OPERATION_FAILED)));

    }

    @Test
    void addNewLocationSavedTest() throws Exception {

        LocationDTO locationDTO  = LocationDTO.builder().locationName(LOCATION).build();

        when(regionService.findByIdWithoutLocation(anyLong())).thenReturn(Optional.of(new RegionDTO()));
        when(locationService.findIfExistent(ArgumentMatchers.any(),anyLong())).thenReturn(Boolean.FALSE);

        when(locationService.saveLocation(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(Optional.of(locationDTO));

        mockMvc.perform(post("/api/v1/regions/1/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(locationDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.locationName", equalTo(locationDTO.getLocationName())));

    }
    //TODO:add tests for put

}