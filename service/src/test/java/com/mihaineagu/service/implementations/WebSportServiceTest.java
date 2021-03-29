package com.mihaineagu.service.implementations;

import com.mihaineagu.data.api.v1.mappers.InformationMapper;
import com.mihaineagu.data.api.v1.mappers.SportMapper;
import com.mihaineagu.data.api.v1.models.SportDTO;
import com.mihaineagu.data.domain.Information;
import com.mihaineagu.data.domain.Sport;
import com.mihaineagu.data.repository.InformationRepository;
import com.mihaineagu.data.repository.SportRepository;
import com.mihaineagu.service.interfaces.InformationService;
import com.mihaineagu.service.interfaces.SportService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WebSportServiceTest {

    public static final String SPORT_1 = "sport1";
    public static final long ID = 1L;
    public static final int PRICE1 = 100;
    public static final long ID1 = 2L;
    public static final int PRICE2 = 200;
    public static final String SPORT_2 = "Sport2";
    public static final String DATE1 = "11-11-2000";
    public static final String DATE2 = "01-01-2000";
    final String URI = "/mock/";

    SportService sportService;
    SimpleDateFormat sdf;

    @Mock
    SportRepository sportRepository;
    @Mock
    InformationService informationService;

    SportMapper sportMapper;

    InformationMapper informationMapper;

    @BeforeEach
    void setUp() {
        sportRepository = mock(SportRepository.class);
        informationService = mock(InformationService.class);
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        sportMapper = SportMapper.INSTANCE;
        informationMapper = InformationMapper.INSTANCE;

        sportService = new WebSportService(SportMapper.INSTANCE, InformationMapper.INSTANCE, sportRepository, informationService);
        sportService.setUri(URI);
    }

    @Test
    void findAllSports() {
        List<Sport> sportList = List.of(new Sport[]{new Sport(), new Sport(), new Sport()});

        when(sportRepository.findAll()).thenReturn(sportList);

        List<SportDTO> sportDTOS = sportService.findAllSports();

        assertEquals(sportList.size(), sportDTOS.size());
    }

    @Test
    void findBySportTypeTest() {
        Sport sport = new Sport();
        sport.setSportType(SPORT_1);
        sport.setId(ID);

        when(sportRepository.findBySportType(SPORT_1)).thenReturn(Optional.of(sport));

        Optional<SportDTO> sportDTO = sportService.findBySportType(SPORT_1);

        assertTrue(sportDTO.isPresent());
        assertEquals(sport.getSportType(), sportDTO.get().getSportType());
        assertEquals(URI + sport.getId(), sportDTO.get().getUri());
    }

    @Test
    void findByIdTest() {
        Sport sport = new Sport();
        sport.setId(ID);

        when(sportRepository.findById(ID)).thenReturn(Optional.of(sport));

        Optional<SportDTO> sportDTO = sportService.findById(ID);

        assertTrue(sportDTO.isPresent());
        assertEquals(URI + sport.getId(), sportDTO.get().getUri());
    }

    @Test
    void getSportAndInformationDTOSingleInstanceTest() throws ParseException {
        Information information1 = new Information();
        information1.setSportId(ID);
        information1.setPrice(PRICE1);
        information1.setStartPeriod(sdf.parse(DATE1));
        information1.setEndPeriod(sdf.parse(DATE2));

        Sport sport = new Sport();
        sport.setId(ID);
        sport.setSportType(SPORT_1);

        List<Information> informationList = Collections.singletonList(information1);

        when(informationService.getInformationByLocationId(ID)).thenReturn(informationList);

        when(sportRepository.findById(ID)).thenReturn(Optional.of(sport));

        List<SportDTO> sportDTOList = sportService.getSportAndInformationDTO(ID);

        assertEquals(1, sportDTOList.size());
        assertEquals(sport, sportMapper.DTOToSport(sportDTOList.get(0)));
        assertEquals(informationMapper.informationToDTO(information1), sportDTOList.get(0).getInformation());
    }

    @Test
    void getSportAndInformationDTOMultiInstanceTest() throws ParseException {
        Information information1 = new Information();
        information1.setSportId(ID);
        information1.setPrice(PRICE1);
        information1.setStartPeriod(sdf.parse(DATE1));
        information1.setEndPeriod(sdf.parse(DATE2));

        Information information2 = new Information();
        information2.setSportId(ID1);
        information2.setPrice(PRICE2);
        information2.setStartPeriod(sdf.parse(DATE1));
        information2.setEndPeriod(sdf.parse(DATE2));


        Sport sport = new Sport();
        sport.setId(ID);
        sport.setSportType(SPORT_1);


        Sport sport1 = new Sport();
        sport1.setId(ID1);
        sport1.setSportType(SPORT_2);


        List<Information> informationList = List.of(information1, information2);

        when(informationService.getInformationByLocationId(ID)).thenReturn(informationList);

        when(sportRepository.findById(ID)).thenReturn(Optional.of(sport));
        when(sportRepository.findById(ID1)).thenReturn(Optional.of(sport1));

        List<SportDTO> sportDTOList = sportService.getSportAndInformationDTO(ID);

        assertEquals(2, sportDTOList.size());
        assertEquals(sport, sportMapper.DTOToSport(sportDTOList.get(0)));
        assertEquals(sport1, sportMapper.DTOToSport(sportDTOList.get(1)));
        assertEquals(informationMapper.informationToDTO(information1), sportDTOList.get(0).getInformation());
        assertEquals(informationMapper.informationToDTO(information2), sportDTOList.get(1).getInformation());
    }
}