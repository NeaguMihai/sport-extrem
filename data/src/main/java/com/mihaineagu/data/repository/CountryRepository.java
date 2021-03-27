package com.mihaineagu.data.repository;

import com.mihaineagu.data.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {

    Optional<Country> findCountryByCountryName(String name);
}
