package com.mihaineagu.data.repository;

import com.mihaineagu.data.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {

    List<Region> findByCountryId(Long id);

    Optional<Region> findRegionByCountryIdAndRegionName(Long id, String name);
}
