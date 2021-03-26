package com.mihaineagu.data.repository;

import com.mihaineagu.data.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

        List<Location> findByRegionId(Long regionId);
}
