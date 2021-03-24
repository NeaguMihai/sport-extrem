package com.mihaineagu.service.implementations;

import com.mihaineagu.data.api.v1.mappers.InformationMapper;
import com.mihaineagu.data.api.v1.models.InformationDTO;
import com.mihaineagu.data.repository.InformationRepository;
import com.mihaineagu.service.interfaces.InformationService;

import java.util.Optional;

public class WebInformationService implements InformationService {

    private final InformationRepository informationRepository;
    private final InformationMapper informationMapper;

    public WebInformationService(InformationRepository informationRepository, InformationMapper informationMapper) {
        this.informationRepository = informationRepository;
        this.informationMapper = informationMapper;
    }

    @Override
    public Optional<InformationDTO> getInformationById(Long locationId, Long sportId) {
        return informationRepository
                .getInformationByLocationIdAndSportId(locationId, sportId)
                .map(informationMapper::informationToDTO);
    }
}
