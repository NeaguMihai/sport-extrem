package com.mihaineagu.service.interfaces;

import com.mihaineagu.data.api.v1.models.InformationDTO;
import com.mihaineagu.data.api.v1.models.LocationDTO;
import com.mihaineagu.data.api.v1.models.SportDTO;
import com.mihaineagu.data.domain.Information;

import java.util.List;
import java.util.Optional;

public interface InformationService {

    Optional<InformationDTO> getInformationById(Long locationId, Long sportId);

    List<Information> getInformationByLocationId(Long id);

    List<Information> getInformationBySportId(Long id);

    Optional<InformationDTO> saveInformation(InformationDTO informationDTO, LocationDTO locationDTO, SportDTO sportDTO);
}
