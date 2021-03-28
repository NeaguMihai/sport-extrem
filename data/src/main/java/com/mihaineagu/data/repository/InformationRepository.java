package com.mihaineagu.data.repository;

import com.mihaineagu.data.domain.Information;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InformationRepository extends JpaRepository<Information, Long> {

    Optional<Information> getInformationByLocationIdAndSportId(Long locationId, Long sportId);

    List<Information> getInformationByLocationId(Long id);

    List<Information> getInformationBySportId(Long id);

    Integer deleteByLocationIdAndSportId(Long locationId, Long sportId);

    void deleteByLocationId(Long locationId);

    void deleteBySportId(Long locationId);

    @Modifying
    @Query(value = "DELETE FROM location_sport ls WHERE ls.location_id IN (SELECT id FROM location WHERE location.region_id = :id)", nativeQuery = true)
    void deleteByLocationIdIn(@Param("id") Long id);
}
