package com.mihaineagu.web.controllers;

import com.mihaineagu.data.api.v1.models.CountryDTO;
import com.mihaineagu.data.api.v1.models.CountryListDTO;
import com.mihaineagu.service.interfaces.CountryService;
import com.mihaineagu.web.exceptions.DuplicateEntityExceptions;
import com.mihaineagu.web.exceptions.FailSaveException;
import com.mihaineagu.web.exceptions.RessourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class CountryController {

    private static final Logger logger = LogManager.getLogger(CountryController.class);
    private final String URI = "/api/v1/countries/";
    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
        countryService.setUri(URI);
    }

    @GetMapping(path = "/countries",produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public CountryListDTO getAllCountries(
            @RequestParam(name = "region", defaultValue = "false") Boolean region) {

        if(region)
            return new CountryListDTO(countryService.findAllCountriesWithRegion());
        else
            return new CountryListDTO(countryService.findAllCountriesNoRegion());

    }

    @GetMapping(path = "/countries/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CountryDTO getCountryById(
            @RequestParam(name = "region", defaultValue = "false") Boolean region,
            @PathVariable(name = "id") Long id) {

        Optional<CountryDTO> countryDTO;
        if(region)
            countryDTO = countryService.findCountryByIdWithRegion(id);
        else
            countryDTO = countryService.findCountryByIdWithoutRegion(id);

        countryDTO.orElseThrow(RessourceNotFoundException::new);

        return countryDTO.get();
    }

    @PostMapping(path = "/countries")
    @ResponseStatus(HttpStatus.CREATED)
    public CountryDTO addNewCountry(@Valid @RequestBody CountryDTO countryDTO) {
        countryDTO.setUri(null);

        Optional<CountryDTO> returned = countryService.addNewCountry(countryDTO);

        returned.orElseThrow(DuplicateEntityExceptions::new);
        return returned.map(countryDTO1 -> {
            countryDTO1.setUri(URI + countryDTO1.getUri());
            return countryDTO1;
        }).get();
    }

    @PutMapping("/countries/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CountryDTO updateCountry(
            @RequestBody @Valid CountryDTO countryDTO,
            @PathVariable(name = "id") Long id) {

        Optional<CountryDTO> returned = countryService.findCountryByIdWithoutRegion(id);

        returned.orElseThrow(RessourceNotFoundException::new);

        Optional<CountryDTO> saved = returned.map(country -> {
            country.setCountryName(countryDTO.getCountryName());
            return countryService.saveCountry(country);
        });
        saved.orElseThrow(FailSaveException::new);

        return saved.map(countryDTO1 -> {
            countryDTO1.setUri(URI +countryDTO1.getUri());
            return countryDTO1;
        }).get();

    }

    @DeleteMapping(path = "/countries/{id}")
    public void deleteCountry(
            @PathVariable(name = "id") Long id) {


            countryService.deleteCountry(id);

    }
}
