package com.mihaineagu.service.implementations;

import com.mihaineagu.data.api.v1.mappers.InformationMapper;
import com.mihaineagu.data.api.v1.mappers.LocationMapper;
import com.mihaineagu.data.api.v1.mappers.SportMapper;
import com.mihaineagu.data.api.v1.models.InformationDTO;
import com.mihaineagu.data.domain.Information;
import com.mihaineagu.data.repository.InformationRepository;
import com.mihaineagu.service.interfaces.InformationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WebInformationServiceTest {

    public static final String END_1 = "10-10-2000";
    public static final String START_1 = "01-01-2000";
    public static final long ID = 1L;
    public static final int PRICE = 100;
    InformationService informationService;
    SimpleDateFormat sdf;
    LocationMapper locationMapper;
    SportMapper sportMapper;

    @Mock
    InformationRepository informationRepository;

    @BeforeEach
    void setUp() {
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        informationRepository = mock(InformationRepository.class);
        locationMapper = LocationMapper.INSTANCE;
        sportMapper = SportMapper.INSTANCE;
        informationService = new WebInformationService(informationRepository, InformationMapper.INSTANCE, sportMapper, locationMapper);
    }

    @Test
    void getInformationByIdFoundTest() throws ParseException {
        Information returnedInformation = new Information();
        returnedInformation.setStartPeriod(sdf.parse(START_1));
        returnedInformation.setEndPeriod(sdf.parse(END_1));
        returnedInformation.setLocationId(ID);
        returnedInformation.setPrice(PRICE);

        when(informationRepository.getInformationByLocationIdAndSportId(ID,ID)).thenReturn(Optional.of(returnedInformation));

        Optional<InformationDTO> returnedDTO = informationService.getInformationById(ID,ID);

        assertTrue(returnedDTO.isPresent());
        assertEquals(sdf.parse(END_1), returnedDTO.get().getEndingPeriod());
        assertEquals(sdf.parse(START_1), returnedDTO.get().getStartingPeriod());
        assertEquals(PRICE, returnedDTO.get().getPrice());
    }

    @Test
    void getInformationByIdNotFoundTest() {

        when(informationRepository.getInformationByLocationIdAndSportId(anyLong(),anyLong())).thenReturn(Optional.empty());

        Optional<InformationDTO> returnedDTO = informationService.getInformationById(anyLong(),anyLong());

        assertTrue(returnedDTO.isEmpty());

    }
}