package com.mihaineagu.data.repository;

import com.mihaineagu.data.domain.Sport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SportRepository extends JpaRepository<Sport, Long> {

    Optional<Sport> findBySportType(String sportType);
}
