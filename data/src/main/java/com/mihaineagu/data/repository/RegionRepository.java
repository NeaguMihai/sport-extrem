package com.mihaineagu.data.repository;

import com.mihaineagu.data.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
}
