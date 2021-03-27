package com.mihaineagu.data.repository;

import com.mihaineagu.data.domain.Location;
import com.mihaineagu.data.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

        List<Location> findByRegionId(Long regionId);

        Optional<Location> findLocationByRegionIdAndLocationName(Long id, String name);

}
