package com.mihaineagu.data.api.v1.mappers;

import com.mihaineagu.data.api.v1.models.InformationDTO;
import com.mihaineagu.data.domain.Location;
import com.mihaineagu.data.domain.Information;
import org.junit.jupiter.api.Test;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class InformationMapperTest {

    public static final int PRICE = 120;
    public static final String START_PERIOD = "12-Aug";
    public static final String END_PERIOD = "20-Dec";
    public static final String SPORT_TYPE = "sportType";
    public static final String LOCATION_NAME = "locationName";
    public static final String URI = "1";
    InformationMapper informationMapper = InformationMapper.INSTANCE;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy");

    @Test
    void locationSportToInformation() throws ParseException {
        Information information = new Information();
        information.setLocation(new Location());
        information.setPrice(PRICE);
        information.setStartPeriod(sdf.parse("10-10-2000"));
        information.setEndPeriod(sdf.parse("10-10-2001"));

        InformationDTO informationDTO = informationMapper.informationToDTO(information);

        assertEquals(information.getStartPeriod(), informationDTO.getStartingPeriod());
        assertEquals(information.getEndPeriod(), informationDTO.getEndingPeriod());
        assertEquals(information.getPrice(), informationDTO.getPrice());

    }

    @Test
    void informationToLocationSport() throws ParseException {
        InformationDTO informationDTO = InformationDTO
                .builder()
                .endingPeriod(sdf.parse("10-10-2000"))
                .startingPeriod(sdf.parse("10-10-2000"))
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

}