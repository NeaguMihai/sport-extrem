package com.mihaineagu.web.controllers;

import com.mihaineagu.data.api.v1.models.SportDTO;
import com.mihaineagu.service.interfaces.SportService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SportController.class)
@AutoConfigureDataJpa
public class SportControllerTest extends AbstractControllerTest{

    public static final String URI1  = "/mock/1";
    public static final String SPORT_1 = "sport1";
    public static final long ID = 1L;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SportService sportService;

    SportDTO returnedSport;

    @BeforeEach
    void setUp(){

        returnedSport = SportDTO
                .builder()
                .uri(URI1)
                .sportType(SPORT_1)
                .build();
    }

    @Test
    void getAllSports() throws Exception {
        List<SportDTO> sportDTOList = List.of(SportDTO.builder().sportType("Tyep1").build(), SportDTO.builder().sportType("Tyep1").build());

        when(sportService.findAllSports()).thenReturn(sportDTOList);

        mockMvc.perform(get("/api/v1/sports")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sportList", hasSize(2)));
    }


    @Test
    void findByIdFoundTest() throws Exception {

        when(sportService.findById(ID)).thenReturn(Optional.of(returnedSport));

        mockMvc.perform(get("/api/v1/sports/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uri", equalTo(URI1)))
                .andExpect(jsonPath("sportType", equalTo(SPORT_1)));
    }

    @Test
    void findByIdNotFoundTest() throws Exception {

        when(sportService.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/sports/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorMessage", equalTo(NOT_FOUND)));
    }

    @Test
    void addSportNotExistingTest() throws Exception {
        SportDTO sportDTO = SportDTO
                .builder()
                .sportType(SPORT_1)
                .build();

        when(sportService.addNewSport(any())).thenReturn(Optional.ofNullable(sportDTO));

        mockMvc.perform(post("/api/v1/sports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(sportDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sportType", equalTo(SPORT_1)));
    }

    @Test
    void addSportExistingTest() throws Exception {
        SportDTO sportDTO = SportDTO
                .builder()
                .sportType(SPORT_1)
                .build();

        when(sportService.addNewSport(any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/sports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectWriter.writeValueAsString(sportDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errorMessage", equalTo(EXISTS_ERROR)));
    }

}
