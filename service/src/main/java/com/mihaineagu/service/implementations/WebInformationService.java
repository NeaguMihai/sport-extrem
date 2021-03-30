package com.mihaineagu.service.implementations;

import com.mihaineagu.data.api.v1.mappers.InformationMapper;
import com.mihaineagu.data.api.v1.mappers.LocationMapper;
import com.mihaineagu.data.api.v1.mappers.SportMapper;
import com.mihaineagu.data.api.v1.models.InformationDTO;
import com.mihaineagu.data.api.v1.models.LocationDTO;
import com.mihaineagu.data.api.v1.models.SportDTO;
import com.mihaineagu.data.domain.Information;
import com.mihaineagu.data.domain.Location;
import com.mihaineagu.data.domain.Sport;
import com.mihaineagu.data.repository.InformationRepository;
import com.mihaineagu.service.interfaces.InformationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service("webInformationService")
@Transactional
public class WebInformationService implements InformationService {

    private static final Logger logger = LogManager.getLogger(WebInformationService.class);
    private final InformationRepository informationRepository;
    private final InformationMapper informationMapper;
    private final SportMapper sportMapper;
    private final LocationMapper locationMapper;


    public WebInformationService(InformationRepository informationRepository, InformationMapper informationMapper, SportMapper sportMapper, LocationMapper locationMapper) {
        this.informationRepository = informationRepository;
        this.informationMapper = informationMapper;
        this.sportMapper = sportMapper;
        this.locationMapper = locationMapper;
    }

    @Override
    public Optional<InformationDTO> getInformationById(Long locationId, Long sportId) {
        return informationRepository
                .getInformationByLocationIdAndSportId(locationId, sportId)
                .map(informationMapper::informationToDTO);
    }

    @Override
    public Optional<Information> getInformationByIdforUpdate(Long locationId, Long sportId) {
        return informationRepository.getInformationByLocationIdAndSportId(locationId, sportId);
    }

    @Override
    public List<Information> getInformationByLocationId(Long id) {
        return informationRepository.getInformationByLocationId(id);
    }

    @Override
    public Optional<InformationDTO> saveInformation(InformationDTO informationDTO, Location location, SportDTO sportDTO) {
        Information toBeSaved = informationMapper.DTOToInformation(informationDTO);
        Sport sport = sportMapper.DTOToSport(sportDTO);

        toBeSaved.setSportId(sport.getId());
        toBeSaved.setSport(sport);

        toBeSaved.setLocationId(location.getId());
        toBeSaved.setLocation(location);

        return Optional.of(informationRepository.save(toBeSaved)).map(informationMapper::informationToDTO);
    }

    @Override
    public Optional<InformationDTO> saveInformation(Information information) {
        return Optional.of(informationRepository.save(information)).map(informationMapper::informationToDTO);
    }

    @Override
    public void deleteInformationById(Long locationId, Long sportId) {

        logger.info(informationRepository.deleteByLocationIdAndSportId(locationId, sportId));

    }

    @Override
    public Set<LocationDTO> getResultFromAndroidRequest(List<String> sport_names, LocalDate startDate, LocalDate endDate) {
        List<Object[]> result = informationRepository.getInformationBySportsNamesAndDate(sport_names, startDate, endDate);
        if (result.isEmpty())
            return new HashSet<>();
        Map<Long, LocationDTO> locationMap = new HashMap<>();
        result.forEach( record -> {
            Long locationId = Long.valueOf(record[0].toString());
            LocationDTO mapLocationDTO;
            if(!locationMap.containsKey(locationId)){
                mapLocationDTO = LocationDTO
                        .builder()
                        .uri("/api/v1/locations/" + locationId.toString())
                        .locationName(record[1].toString())
                        .sports(new LinkedList<>())
                        .build();
                locationMap.put(locationId, mapLocationDTO);
            }else {
                mapLocationDTO = locationMap.get(locationId);

            }
            InformationDTO informationDTO = InformationDTO
                    .builder()
                    .price(Integer.parseInt(record[2].toString()))
                    .build();

            SportDTO sportDTO = SportDTO
                    .builder()
                    .sportType(record[4].toString())
                    .uri("/api/v1/locations/" + record[3].toString())
                    .information(informationDTO)
                    .build();

            mapLocationDTO.getSports().add(sportDTO);

        });
        return new HashSet<>(locationMap.values());
    }

}
