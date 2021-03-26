package com.mihaineagu.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mihaineagu.data.api.v1.models.LocationDTO;

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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LocationController.class)
@AutoConfigureDataJpa
class LocationControllerTest {

    public static final String URI = "/mock/1";
    @MockBean
    LocationService locationService;

    @Autowired
    MockMvc mockMvc;

    ObjectWriter objectWriter;


    @BeforeEach
    void setUp(){

        objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }


    @Test
    void getAllLocationsNoSport() throws Exception {
        List<LocationDTO> locationDTOList = List.of(new LocationDTO(), new LocationDTO(), new LocationDTO());

        when(locationService.findAllWithoutSports()).thenReturn(locationDTOList);

        mockMvc.perform(get("/api/v1/locations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.locationListDTO", hasSize(3)));

    }

    @Test
    void getLocationIdFound() throws Exception {
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
}