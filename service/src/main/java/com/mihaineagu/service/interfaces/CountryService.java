package com.mihaineagu.service.interfaces;

import com.mihaineagu.data.api.v1.models.CountryDTO;

import java.util.List;

public interface CountryService {

    List<CountryDTO> getAllCountries();
}
