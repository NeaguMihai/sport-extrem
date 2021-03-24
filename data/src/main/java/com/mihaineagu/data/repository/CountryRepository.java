package com.mihaineagu.data.repository;

import com.mihaineagu.data.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
