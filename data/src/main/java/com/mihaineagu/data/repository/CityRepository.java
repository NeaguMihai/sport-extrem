package com.mihaineagu.data.repository;

import com.mihaineagu.data.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<Location, Long> {
}
