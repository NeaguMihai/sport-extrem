package com.mihaineagu.web.controllers;

import com.mihaineagu.data.api.v1.models.SportDTO;
import com.mihaineagu.service.interfaces.SportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SportControllerTest {

    @Mock
    SportService sportService;

    @InjectMocks
    SportController sportController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        sportService = mock(SportService.class);
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(sportController).build();
    }

    @Test
    void getAllSports() throws Exception {
        List<SportDTO> sportDTOList = List.of(SportDTO.builder().build(), SportDTO.builder().build());

        when(sportService.findAllSports()).thenReturn(sportDTOList);

        mockMvc.perform(get("/api/v1/sports")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sportList", hasSize(2)));
    }
}