package com.mihaineagu.service.interfaces;

import com.mihaineagu.data.api.v1.models.CountryDTO;

import java.util.List;
import java.util.Optional;

public interface CountryService {

    List<CountryDTO> findAllCountriesNoRegion();

    List<CountryDTO> findAllCountriesWithRegion();

    Optional<CountryDTO> findCountryByIdWithRegion(Long id);

    Optional<CountryDTO> findCountryByIdWithoutRegion(Long id);

    Optional<CountryDTO> addNewCountry(CountryDTO countryDTO);

    CountryDTO saveCountry(CountryDTO countryDTO);

    void deleteCountry(Long id);

    void deleteCountryRecursive(Long id);

    void setUri(String uri);
}
