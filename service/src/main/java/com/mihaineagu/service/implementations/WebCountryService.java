package com.mihaineagu.service.implementations;

import com.mihaineagu.data.api.v1.mappers.CountryMapper;
import com.mihaineagu.data.api.v1.models.CountryDTO;
import com.mihaineagu.data.repository.CountryRepository;
import com.mihaineagu.service.interfaces.CountryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WebCountryService implements CountryService {

    private final String URI;
    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    public WebCountryService(String uri, CountryRepository countryRepository, CountryMapper countryMapper) {
        URI = uri;
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }


    @Override
    public List<CountryDTO> getAllCountries() {
        return countryRepository
                    .findAll()
                    .stream()
                    .map(countryMapper::countryToDTO)
                    .map(countryDTO -> {
                        countryDTO.setUri(URI + countryDTO.getUri());
                        return countryDTO;
                    })
                    .collect(Collectors.toList());
    }
}
