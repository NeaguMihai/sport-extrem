package com.mihaineagu.data.repository;

import com.mihaineagu.data.domain.LocationSport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitySportRepository extends JpaRepository<LocationSport, Long> {
}
