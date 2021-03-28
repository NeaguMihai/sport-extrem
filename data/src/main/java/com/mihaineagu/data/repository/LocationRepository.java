package com.mihaineagu.data.repository;

import com.mihaineagu.data.domain.Location;
import com.mihaineagu.data.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

        List<Location> findByRegionId(Long regionId);

        Optional<Location> findLocationByRegionIdAndLocationName(Long id, String name);

        void deleteByRegionId( Long id);

        @Modifying
        @Query(value = "DELETE FROM location WHERE region_id IN(SELECT id FROM region WHERE country_id =:id)", nativeQuery = true)
        void deleteAllByRegionIdIn(@Param("id") Long id);


}
