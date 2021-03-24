package com.mihaineagu.data.api.v1.mappers;

import com.mihaineagu.data.api.v1.models.InformationDTO;
import com.mihaineagu.data.api.v1.models.LocationDTO;
import com.mihaineagu.data.api.v1.models.SportDTO;
import com.mihaineagu.data.domain.Location;
import com.mihaineagu.data.domain.Information;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InformationMapperTest {

    public static final int PRICE = 120;
    public static final String START_PERIOD = "12-Aug";
    public static final String END_PERIOD = "20-Dec";
    public static final String SPORT_TYPE = "sportType";
    public static final String LOCATION_NAME = "locationName";
    public static final String URI = "1";
    InformationMapper informationMapper = InformationMapper.INSTANCE;

    @Test
    void locationSportToInformation() {
        Information information = new Information();
        information.setLocation(new Location());
        information.setPrice(PRICE);
        information.setStartPeriod(START_PERIOD);
        information.setEndPeriod(END_PERIOD);

        InformationDTO informationDTO = informationMapper.informationToDTO(information);

        assertEquals(information.getStartPeriod(), informationDTO.getStartingPeriod());
        assertEquals(information.getEndPeriod(), informationDTO.getEndingPeriod());
        assertEquals(information.getPrice(), informationDTO.getPrice());

    }

    @Test
    void informationToLocationSport() {
        InformationDTO informationDTO = InformationDTO
                .builder()
                .endingPeriod(END_PERIOD)
                .startingPeriod(START_PERIOD)
                .price(PRICE)
                .build();
        Information information = informationMapper.DTOToInformation(informationDTO);

        assertEquals(informationDTO.getStartingPeriod(), information.getStartPeriod());
        assertEquals(informationDTO.getEndingPeriod(), information.getEndPeriod());
        assertEquals(informationDTO.getPrice(), information.getPrice());
        assertNull(information.getLocation());
        assertNull(information.getLocationId());
        assertNull(information.getSport());
        assertNull(information.getSportId());


    }

    @Test
    void locationDTOToLocationSport() {

        InformationDTO informationDTO = InformationDTO
                .builder()
                .price(PRICE)
                .startingPeriod(START_PERIOD)
                .endingPeriod(END_PERIOD)
                .build();
        SportDTO sportDTO = SportDTO
                .builder()
                .information(informationDTO)
                .uri(URI)
                .sportType(SPORT_TYPE)
                .build();
        LocationDTO locationDTO = LocationDTO
                .builder()
                .locationName(LOCATION_NAME)
                .sport(sportDTO)
                .uri(URI)
                .build();

        Information information = informationMapper.locationDTOToInformation(locationDTO);

        assertEquals(informationDTO.getPrice(), information.getPrice());
        assertEquals(informationDTO.getStartingPeriod(), information.getStartPeriod());
        assertEquals(informationDTO.getEndingPeriod(), information.getEndPeriod());
        assertEquals(sportDTO.getUri(), String.valueOf(information.getSportId()));
        assertEquals(locationDTO.getUri(), String.valueOf(information.getLocationId()));
        assertNull(information.getLocation());
        assertNull(information.getSport());
    }
}