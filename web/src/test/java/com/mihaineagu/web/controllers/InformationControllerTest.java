package com.mihaineagu.web.controllers;

import com.mihaineagu.data.api.v1.models.InformationDTO;
import com.mihaineagu.data.api.v1.models.SportDTO;
import com.mihaineagu.data.domain.Location;
import com.mihaineagu.service.interfaces.InformationService;
import com.mihaineagu.service.interfaces.LocationService;
import com.mihaineagu.service.interfaces.SportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@WebMvcTest(InformationController.class)
@AutoConfigureDataJpa
class InformationControllerTest extends AbstractControllerTest{

    public static final String ENDING = "10-10-2021";
    public static final String STARTING = "01-01-2020";
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    @Autowired
    MockMvc mockMvc;

    @MockBean
    InformationService informationService;
    @MockBean
    SportService sportService;
    @MockBean
    LocationService locationService;

    @Test
    void addNewSportToLocationMissingLocationTest() throws Exception {

        InformationDTO informationDTO = InformationDTO
                .builder()
                .price(100)
                .endingPeriod(sdf.parse(ENDING))
                .startingPeriod(sdf.parse(STARTING))
                .build();


        when(sportService.findById(anyLong())).thenReturn(Optional.empty());
        when(locationService.findByIdWithoutSports(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/locations/1/sports/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(informationDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorMessage", equalTo(NOT_FOUND)));

    }

    @Test
    void addNewSportToLocationDuplicateInformationTest() throws Exception {


        InformationDTO informationDTO = InformationDTO
                .builder()
                .price(100)
                .endingPeriod(sdf.parse(ENDING))
                .startingPeriod(sdf.parse(STARTING))
                .build();


        when(sportService.findById(anyLong())).thenReturn(Optional.of(new SportDTO()));
        when(locationService.findLocation(anyLong())).thenReturn(Optional.of(new Location()));
        when(informationService.getInformationById(anyLong(),anyLong())).thenReturn(Optional.of(informationDTO));

        mockMvc.perform(post("/api/v1/locations/1/sports/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(informationDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorMessage", equalTo(EXISTS_ERROR)));

    }

    @Test
    void addNewSportToLocationFailSaveTest() throws Exception {

        InformationDTO informationDTO = InformationDTO
                .builder()
                .price(100)
                .endingPeriod(sdf.parse(ENDING))
                .startingPeriod(sdf.parse(STARTING))
                .build();


        when(sportService.findById(anyLong())).thenReturn(Optional.of(new SportDTO()));
        when(locationService.findLocation(anyLong())).thenReturn(Optional.of(new Location()));
        when(informationService.getInformationById(anyLong(),anyLong())).thenReturn(Optional.empty());
        when(informationService.saveInformation(any(), any(), any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/locations/1/sports/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(informationDTO)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.errorMessage", equalTo(OPERATION_FAILED)));

    }

    @Test
    void addNewSportToLocationSuccessTest() throws Exception {

        InformationDTO informationDTO = InformationDTO
                .builder()
                .price(100)
                .endingPeriod(sdf.parse(ENDING))
                .startingPeriod(sdf.parse(STARTING))
                .build();


        when(sportService.findById(anyLong())).thenReturn(Optional.of(new SportDTO()));
        when(locationService.findLocation(anyLong())).thenReturn(Optional.of(new Location()));
        when(informationService.getInformationById(anyLong(),anyLong())).thenReturn(Optional.empty());
        when(informationService.saveInformation(any(), any(), any())).thenReturn(Optional.of(informationDTO));

        mockMvc.perform(post("/api/v1/locations/1/sports/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(informationDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sports", hasSize(1)))
                .andExpect(jsonPath("$.sports[0].information", notNullValue()));

    }
    //TODO:add tests for put

}