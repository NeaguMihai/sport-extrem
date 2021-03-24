package com.mihaineagu.data.api.v1.mappers;

import com.mihaineagu.data.api.v1.models.InformationDTO;
import com.mihaineagu.data.api.v1.models.LocationDTO;
import com.mihaineagu.data.api.v1.models.SportDTO;
import com.mihaineagu.data.domain.Location;
import com.mihaineagu.data.domain.LocationSport;
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
        LocationSport locationSport = new LocationSport();
        locationSport.setLocation(new Location());
        locationSport.setPrice(PRICE);
        locationSport.setStartPeriod(START_PERIOD);
        locationSport.setEndPeriod(END_PERIOD);

        InformationDTO informationDTO = informationMapper.locationSportToInformation(locationSport);

        assertEquals(locationSport.getStartPeriod(), informationDTO.getStartingPeriod());
        assertEquals(locationSport.getEndPeriod(), informationDTO.getEndingPeriod());
        assertEquals(locationSport.getPrice(), informationDTO.getPrice());

    }

    @Test
    void informationToLocationSport() {
        InformationDTO informationDTO = InformationDTO
                .builder()
                .endingPeriod(END_PERIOD)
                .startingPeriod(START_PERIOD)
                .price(PRICE)
                .build();
        LocationSport locationSport = informationMapper.informationToLocationSport(informationDTO);

        assertEquals(informationDTO.getStartingPeriod(), locationSport.getStartPeriod());
        assertEquals(informationDTO.getEndingPeriod(), locationSport.getEndPeriod());
        assertEquals(informationDTO.getPrice(), locationSport.getPrice());
        assertNull(locationSport.getLocation());
        assertNull(locationSport.getLocationId());
        assertNull(locationSport.getSport());
        assertNull(locationSport.getSportId());


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

        LocationSport locationSport = informationMapper.locationDTOToLocationSport(locationDTO);

        assertEquals(informationDTO.getPrice(), locationSport.getPrice());
        assertEquals(informationDTO.getStartingPeriod(), locationSport.getStartPeriod());
        assertEquals(informationDTO.getEndingPeriod(), locationSport.getEndPeriod());
        assertEquals(sportDTO.getUri(), String.valueOf(locationSport.getSportId()));
        assertEquals(locationDTO.getUri(), String.valueOf(locationSport.getLocationId()));
        assertNull(locationSport.getLocation());
        assertNull(locationSport.getSport());
    }
}