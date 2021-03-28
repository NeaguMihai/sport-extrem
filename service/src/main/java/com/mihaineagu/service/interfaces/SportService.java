package com.mihaineagu.service.interfaces;

import com.mihaineagu.data.api.v1.models.SportDTO;

import java.util.List;
import java.util.Optional;

public interface SportService {

    List<SportDTO> findAllSports();

    Optional<SportDTO> findBySportType(String sportType);
    Optional<SportDTO> findById(Long id);

    List<SportDTO> getSportAndInformationDTO(Long locationId);

    Optional<SportDTO> addNewSport(SportDTO sportDTO);

    Optional<SportDTO> saveSport(SportDTO sportDTO);

    void deleteSport(Long id);

    void setUri(String uri);
}
