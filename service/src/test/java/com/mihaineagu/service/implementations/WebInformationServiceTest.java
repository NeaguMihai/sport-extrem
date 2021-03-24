package com.mihaineagu.service.implementations;

import com.mihaineagu.data.api.v1.mappers.InformationMapper;
import com.mihaineagu.data.api.v1.models.InformationDTO;
import com.mihaineagu.data.domain.Information;
import com.mihaineagu.data.repository.InformationRepository;
import com.mihaineagu.service.interfaces.InformationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class WebInformationServiceTest {

    public static final String END_1 = "end1";
    public static final String START_1 = "start1";
    public static final long ID = 1L;
    public static final int PRICE = 100;
    InformationService informationService;

    @Mock
    InformationRepository informationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        informationService = new WebInformationService(informationRepository, InformationMapper.INSTANCE);
    }

    @Test
    void getInformationByIdFoundTest() {
        Information returnedInformation = new Information();
        returnedInformation.setStartPeriod(START_1);
        returnedInformation.setEndPeriod(END_1);
        returnedInformation.setLocationId(ID);
        returnedInformation.setPrice(PRICE);

        when(informationRepository.getInformationByLocationIdAndSportId(ID,ID)).thenReturn(Optional.of(returnedInformation));

        Optional<InformationDTO> returnedDTO = informationService.getInformationById(ID,ID);

        assertTrue(returnedDTO.isPresent());
        assertEquals(END_1, returnedDTO.get().getEndingPeriod());
        assertEquals(START_1, returnedDTO.get().getStartingPeriod());
        assertEquals(PRICE, returnedDTO.get().getPrice());
    }

    @Test
    void getInformationByIdNotFoundTest() {

        when(informationRepository.getInformationByLocationIdAndSportId(anyLong(),anyLong())).thenReturn(Optional.empty());

        Optional<InformationDTO> returnedDTO = informationService.getInformationById(anyLong(),anyLong());

        assertTrue(returnedDTO.isEmpty());

    }
}