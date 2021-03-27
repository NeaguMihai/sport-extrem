package com.mihaineagu.web.controllers;

import com.mihaineagu.data.api.v1.models.CountryDTO;
import com.mihaineagu.data.api.v1.models.RegionDTO;
import com.mihaineagu.data.api.v1.models.RegionListDTO;
import com.mihaineagu.data.domain.Country;
import com.mihaineagu.service.interfaces.CountryService;
import com.mihaineagu.service.interfaces.RegionService;
import com.mihaineagu.web.exceptions.DuplicateEntityExceptions;
import com.mihaineagu.web.exceptions.FailSaveException;
import com.mihaineagu.web.exceptions.RessourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class RegionController {

    private String uri = "/api/v1/regions/";
    private final RegionService regionService;
    private final CountryService countryService;

    public RegionController(RegionService regionService, CountryService countryService) {
        this.regionService = regionService;
        this.countryService = countryService;
        regionService.setUri(uri);
    }

    @GetMapping(path = "/countries/{country_id}/regions", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public RegionListDTO getAllRegionsByCountry(
            @RequestParam(name = "location", defaultValue = "false") Boolean location,
            @PathVariable(name = "country_id") Long id) {

        if (location)
            return new RegionListDTO(regionService.findByCountryIdWIthLocation(id));
        else
            return new RegionListDTO(regionService.findByCountryIdWithoutLocation(id));
    }

    @GetMapping(path = "/regions/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RegionDTO getRegionById(
            @RequestParam(name = "location", defaultValue = "false") Boolean location,
            @PathVariable(name = "id") Long id
    ) {
        Optional<RegionDTO> regionDTO;
        if(location)
            regionDTO = regionService.findByIdWithLocation(id);
        else
            regionDTO = regionService.findByIdWithoutLocation(id);

        if (regionDTO.isPresent())
            return regionDTO.get();
        else
            throw new RessourceNotFoundException();
    }

    @PostMapping(path = "/countries/{country_id}/regions")
    @ResponseStatus(HttpStatus.CREATED)
    public RegionDTO addNewRegion(
            @PathVariable(name = "country_id") Long id,
            @RequestBody RegionDTO regionDTO) {

        Optional<CountryDTO> countryOptional = countryService.findCountryByIdWithoutRegion(id);

        if(countryOptional.isPresent()){
            regionDTO.setUri(null);
            if(regionService.findIfExists(regionDTO, id)){
                throw new DuplicateEntityExceptions();
            }else {
                Optional<RegionDTO> saved = regionService.saveRegion(regionDTO, countryOptional.get());

                if (saved.isPresent())
                    return saved.get();
                else
                    throw new FailSaveException();
            }

        } else
           throw new RessourceNotFoundException();
    }

}
