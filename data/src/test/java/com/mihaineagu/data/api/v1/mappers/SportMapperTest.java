package com.mihaineagu.data.api.v1.mappers;

import com.mihaineagu.data.api.v1.models.InformationDTO;
import com.mihaineagu.data.api.v1.models.SportDTO;
import com.mihaineagu.data.domain.Sport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SportMapperTest {

    public static final String SPORT_1 = "Sport1";
    public static final long ID = 1L;
    SportMapper sportMapper = SportMapper.INSTANCE;

    @Test
    void sportToDTO() {
        Sport sport = new Sport();
        sport.setSportType(SPORT_1);
        sport.setId(ID);

        SportDTO sportDTO = sportMapper.sportToDTO(sport);

        assertEquals(sport.getSportType(), sportDTO.getSportType());
        assertEquals(String.valueOf(sport.getId()), sportDTO.getUri());
        assertNull(sportDTO.getInformation());
    }

    @Test
    void DTOToSport() {
        SportDTO sportDTO = SportDTO
                .builder()
                .sportType(SPORT_1)
                .uri(String.valueOf(ID))
                .information(InformationDTO.builder().build())
                .build();

        Sport sport = sportMapper.DTOToSport(sportDTO);


        assertEquals(sportDTO.getSportType(), sport.getSportType());
        assertEquals(sportDTO.getUri(), String.valueOf(sport.getId()));

    }
}