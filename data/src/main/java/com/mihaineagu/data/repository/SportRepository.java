package com.mihaineagu.data.repository;

import com.mihaineagu.data.domain.Sport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportRepository extends JpaRepository<Sport, Long> {
}
