package com.mihaineagu.web.controllers;

import com.mihaineagu.data.api.v1.models.CountryDTO;
import com.mihaineagu.data.api.v1.models.CountryListDTO;
import com.mihaineagu.service.interfaces.CountryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

    private final String URI = "/api/v1/countries/";
    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
        countryService.setUri(URI);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<CountryListDTO> getAllCountries(
            @RequestParam(name = "region", defaultValue = "false") Boolean region) {

        if(region)
            return new ResponseEntity<>(new CountryListDTO(countryService.findAllCountriesWithRegion()), HttpStatus.OK);
        else
            return new ResponseEntity<>(new CountryListDTO(countryService.findAllCountriesNoRegion()), HttpStatus.OK);

    }
}
