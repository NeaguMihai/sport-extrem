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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("webInformationService")
public class WebInformationService implements InformationService {

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
    public List<Information> getInformationByLocationId(Long id) {
        return informationRepository.getInformationByLocationId(id);
    }

    @Override
    public List<Information> getInformationBySportId(Long id) {
        return informationRepository.getInformationBySportId(id);
    }

    @Override
    public Optional<InformationDTO> saveInformation(InformationDTO informationDTO, LocationDTO locationDTO, SportDTO sportDTO) {
        Information toBeSaved = informationMapper.DTOToInformation(informationDTO);
        Sport sport = sportMapper.DTOToSport(sportDTO);
        Location location = locationMapper.DTOTOLocation(locationDTO);

        toBeSaved.setSportId(sport.getId());
        toBeSaved.setSport(sport);

        toBeSaved.setLocationId(location.getId());
        toBeSaved.setLocation(location);

        return Optional.of(informationRepository.save(toBeSaved)).map(informationMapper::informationToDTO);
    }
}
