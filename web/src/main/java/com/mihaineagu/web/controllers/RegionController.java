package com.mihaineagu.web.controllers;

import com.mihaineagu.data.api.v1.models.RegionDTO;
import com.mihaineagu.data.api.v1.models.RegionListDTO;
import com.mihaineagu.service.interfaces.RegionService;
import com.mihaineagu.web.exceptions.RessourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class RegionController {

    private String uri = "/api/v1/regions/";
    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
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

}
