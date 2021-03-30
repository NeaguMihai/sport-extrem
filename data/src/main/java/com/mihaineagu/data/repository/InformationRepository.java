package com.mihaineagu.data.repository;

import com.mihaineagu.data.domain.Information;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface InformationRepository extends JpaRepository<Information, Long> {

    Optional<Information> getInformationByLocationIdAndSportId(Long locationId, Long sportId);

    List<Information> getInformationByLocationId(Long id);

    Integer deleteByLocationIdAndSportId(Long locationId, Long sportId);

    @Query(value = "SELECT  l.id AS locationId, l.location_name AS locationName , ls.price AS price, s.id AS sportId, " +
            "    s.sport_type AS sportType FROM location as l JOIN location_sport as ls" +
            "    ON ls.location_id = l.id" +
            "    JOIN sport as s" +
            "    ON s.id = ls.sport_id" +
            "    WHERE s.sport_type IN(:sportTypes)" +
            "    AND (MONTH(:sDate)*100 + DAY(:sDate)" +
            "    AND MONTH(:eDate)*100 + DAY(:eDate)" +
            "    BETWEEN MONTH(ls.start_period)*100 + DAY(ls.start_period)" +
            "    AND MONTH(ls.end_period)*100 + DAY(ls.end_period))" +
            "    ORDER BY l.location_name, price", nativeQuery = true)
    List<Object[]> getInformationBySportsNamesAndDate(@Param("sportTypes") List<String> sportTypes, @Param("sDate") LocalDate startDate, @Param("eDate") LocalDate endDate);

}
