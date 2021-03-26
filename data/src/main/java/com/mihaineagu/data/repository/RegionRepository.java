package com.mihaineagu.data.repository;

import com.mihaineagu.data.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {

    List<Region> findByCountryId(Long id);
}
