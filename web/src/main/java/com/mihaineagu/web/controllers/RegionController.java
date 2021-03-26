package com.mihaineagu.web.controllers;

import com.mihaineagu.data.api.v1.models.RegionListDTO;
import com.mihaineagu.service.interfaces.RegionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/regions")
public class RegionController {

    private final String URI = "/api/v1/regions/";
    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
        regionService.setUri(URI);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<RegionListDTO> getAllRegions(
            @RequestParam(name = "location", defaultValue = "false") Boolean location) {

        if (location)
            return new ResponseEntity<>(new RegionListDTO(regionService.getAllRegionsWithLocation()), HttpStatus.OK);
        else
            return new ResponseEntity<>(new RegionListDTO(regionService.getAllRegionsWithoutLocations()), HttpStatus.OK);
    }
}
