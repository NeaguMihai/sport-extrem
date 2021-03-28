package com.mihaineagu.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mihaineagu.data.api.v1.models.CountryDTO;
import com.mihaineagu.data.api.v1.models.LocationDTO;
import com.mihaineagu.data.api.v1.models.LocationListDTO;
import com.mihaineagu.data.api.v1.models.RegionDTO;
import com.mihaineagu.service.interfaces.CountryService;
import com.mihaineagu.service.interfaces.RegionService;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.List;
import java.util.Optional;

import static com.mihaineagu.web.controllers.AbstractControllerTest.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RegionController.class)
@AutoConfigureDataJpa
class RegionControllerTest extends AbstractControllerTest{

    public static final String REGION_1 = "Region1";
    @Autowired
    MockMvc mockMvc;

    @MockBean
    RegionService regionService;

    @MockBean
    CountryService countryService;

    @Test
    void getAllRegionsByCountryWithLocationsTest() throws Exception {

        List<RegionDTO> regionDTOList  = List.of(
                RegionDTO
                        .builder()
                        .uri(URI)
                        .locations(List.of(
                                        LocationDTO.builder().build(),
                                        LocationDTO.builder().build()))
                        .build());
        when(regionService.findByCountryIdWIthLocation(anyLong())).thenReturn(regionDTOList);

        mockMvc.perform(get("/api/v1/countries/1/regions")
                .contentType(MediaType.APPLICATION_JSON)
                .param("location","true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.regionDTOList", hasSize(1)))
                .andExpect(jsonPath("$.regionDTOList[0].locations", hasSize(2)));
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
                .andExpect(jsonPath("$.regionDTOList[0].locations", nullValue()));
    }

    @Test
    void getRegionByIdWithLocationFoundTest() {

        Optional<RegionDTO> regionDTO = Optional.of(
                RegionDTO
                        .builder()
                        .regionName("region1")
                        .uri(URI)
                        .locations(List.of(new LocationDTO()))
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

    @Test
    void addNewRegionMissingCountryTest() throws Exception {

        RegionDTO regionDTO  = RegionDTO.builder().regionName(REGION_1).build();

        when(countryService.findCountryByIdWithoutRegion(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/countries/1/regions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(regionDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorMessage", equalTo(NOT_FOUND)));

    }

    @Test
    void addNewRegionAlreadyExistsTest() throws Exception {

        RegionDTO regionDTO  = RegionDTO.builder().regionName(REGION_1).build();

        when(countryService.findCountryByIdWithoutRegion(anyLong())).thenReturn(Optional.of(new CountryDTO()));
        when(regionService.findIfExists(ArgumentMatchers.any(),anyLong())).thenReturn(Boolean.TRUE);

        mockMvc.perform(post("/api/v1/countries/1/regions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(regionDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorMessage", equalTo(EXISTS_ERROR)));

    }

    @Test
    void addNewRegionNotSavedTest() throws Exception {

        RegionDTO regionDTO = RegionDTO.builder().regionName(REGION_1).build();

        when(countryService.findCountryByIdWithoutRegion(anyLong())).thenReturn(Optional.of(new CountryDTO()));
        when(regionService.findIfExists(ArgumentMatchers.any(),anyLong())).thenReturn(Boolean.FALSE);
        when(regionService.saveRegion(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/countries/1/regions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(regionDTO)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.errorMessage", equalTo(OPERATION_FAILED)));

    }

    @Test
    void addNewRegionSavedTest() throws Exception {

        RegionDTO regionDTO = RegionDTO.builder().regionName(REGION_1).build();

        when(countryService.findCountryByIdWithoutRegion(anyLong())).thenReturn(Optional.of(new CountryDTO()));
        when(regionService.findIfExists(ArgumentMatchers.any(),anyLong())).thenReturn(Boolean.FALSE);
        when(regionService.saveRegion(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(Optional.of(regionDTO));

        mockMvc.perform(post("/api/v1/countries/1/regions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(regionDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.regionName", equalTo(regionDTO.getRegionName())));

    }
    //TODO:add tests for put

}