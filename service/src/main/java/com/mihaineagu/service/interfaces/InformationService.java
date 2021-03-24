package com.mihaineagu.service.interfaces;

import com.mihaineagu.data.api.v1.models.InformationDTO;

import java.util.Optional;

public interface InformationService {

    Optional<InformationDTO> getInformationById(Long locationId, Long sportId);
}
