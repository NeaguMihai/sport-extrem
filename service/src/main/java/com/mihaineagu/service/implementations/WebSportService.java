package com.mihaineagu.service.implementations;

import com.mihaineagu.data.api.v1.mappers.InformationMapper;
import com.mihaineagu.data.api.v1.mappers.SportMapper;
import com.mihaineagu.data.api.v1.models.SportDTO;
import com.mihaineagu.data.domain.Sport;
import com.mihaineagu.data.repository.SportRepository;
import com.mihaineagu.service.interfaces.InformationService;
import com.mihaineagu.service.interfaces.SportService;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("webSportService")
public class WebSportService implements SportService {

    private String uri;
    private final SportMapper sportMapper;
    private final InformationMapper informationMapper;
    private final SportRepository sportRepository;
    private final InformationService informationService;

    public WebSportService(SportMapper sportMapper, InformationMapper informationMapper, SportRepository sportRepository, InformationService informationService) {
        this.sportMapper = sportMapper;
        this.informationMapper = informationMapper;
        this.sportRepository = sportRepository;
        this.informationService = informationService;
    }
    @Override
    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public List<SportDTO> findAllSports() {
        return sportRepository
                .findAll()
                .stream()
                .map(sportMapper::sportToDTO)
                .map(sportDTO -> {
                    sportDTO.setUri(uri + sportDTO.getUri());
                    return sportDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SportDTO> findBySportType(String sportType) {
        return sportRepository.findBySportType(sportType)
                .map(sportMapper::sportToDTO)
                .map(sportDTO -> {sportDTO.setUri(uri + sportDTO.getUri()); return sportDTO;});
    }

    @Override
    public Optional<SportDTO> findById(Long id) {
        return sportRepository.findById(id)
                .map(sportMapper::sportToDTO)
                .map(sportDTO -> {sportDTO.setUri(uri + sportDTO.getUri()); return sportDTO;});
    }

    @Override
    public List<SportDTO> getSportAndInformationDTO(Long locationId) {

        List<SportDTO> sportDTOS = new LinkedList<>();

        informationService
                .getInformationByLocationId(locationId)
                .forEach(information-> {
                    sportRepository
                            .findById(information.getSportId())
                            .ifPresent(sport -> {
                                SportDTO sportDTO = sportMapper
                                                    .sportToDTO(sport);
                                sportDTO
                                        .setInformation(informationMapper
                                                .informationToDTO(information));
                                sportDTO.setUri(uri + sport.getId());
                                sportDTOS.add(sportDTO);
                            });
                });
        return sportDTOS;
    }

    @Override
    public Optional<SportDTO> addNewSport(SportDTO sportDTO) {
        Sport sport = sportMapper.DTOToSport(sportDTO);
        if(findBySportType(sport.getSportType()).isEmpty())
            return Optional.of(sportMapper.sportToDTO(sportRepository.save(sport)));
        else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<SportDTO> saveSport(SportDTO sportDTO) {
        Sport sport = sportMapper.DTOToSport(sportDTO);
        return Optional.of(sportRepository.save(sport)).map(sportMapper::sportToDTO);
    }


}
