package com.mihaineagu.data.repository;

import com.mihaineagu.data.domain.Information;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InformationRepository extends JpaRepository<Information, Long> {

    Optional<Information> getInformationByLocationIdAndSportId(Long locationId, Long sportId);

    List<Information> getInformationByLocationId(Long id);

    List<Information> getInformationBySportId(Long id);
}
