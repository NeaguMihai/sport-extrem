package com.mihaineagu.service.implementations;

import com.mihaineagu.data.api.v1.mappers.InformationMapper;
import com.mihaineagu.data.api.v1.models.InformationDTO;
import com.mihaineagu.data.domain.Information;
import com.mihaineagu.data.repository.InformationRepository;
import com.mihaineagu.service.interfaces.InformationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("webInformationService")
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

    @Override
    public List<Information> getInformationByLocationId(Long id) {
        return informationRepository.getInformationByLocationId(id);
    }

    @Override
    public List<Information> getInformationBySportId(Long id) {
        return informationRepository.getInformationBySportId(id);
    }
}
