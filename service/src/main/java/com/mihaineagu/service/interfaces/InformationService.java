package com.mihaineagu.service.interfaces;

import com.mihaineagu.data.api.v1.models.InformationDTO;
import com.mihaineagu.data.api.v1.models.LocationDTO;
import com.mihaineagu.data.api.v1.models.SportDTO;
import com.mihaineagu.data.domain.Information;
import com.mihaineagu.data.domain.Location;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface InformationService {

    Optional<InformationDTO> getInformationById(Long locationId, Long sportId);

    Optional<Information> getInformationByIdforUpdate(Long locationId, Long sportId);

    List<Information> getInformationByLocationId(Long id);

    Optional<InformationDTO> saveInformation(InformationDTO informationDTO, Location location, SportDTO sportDTO);

    Optional<InformationDTO> saveInformation(Information information);

    void deleteInformationById(Long locationId, Long sportId);

    Set<LocationDTO> getResultFromAndroidRequest(List<String> sport_names, LocalDate startDate, LocalDate endDate);
}
