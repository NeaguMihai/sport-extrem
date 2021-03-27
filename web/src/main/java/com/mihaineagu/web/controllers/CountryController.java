package com.mihaineagu.web.controllers;

import com.mihaineagu.data.api.v1.models.CountryDTO;
import com.mihaineagu.data.api.v1.models.CountryListDTO;
import com.mihaineagu.service.interfaces.CountryService;
import com.mihaineagu.web.exceptions.RessourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class CountryController {

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
    public CountryDTO getCountryById(
            @RequestParam(name = "region", defaultValue = "false") Boolean region,
            @PathVariable(name = "id") Long id) {

        Optional<CountryDTO> countryDTO;
        if(region)
            countryDTO = countryService.findCountryByIdWithRegion(id);
        else
            countryDTO = countryService.findCountryByIdWithoutRegion(id);
        if(countryDTO.isPresent())
            return countryDTO.get();
        else
            throw new RessourceNotFoundException();
    }
}
