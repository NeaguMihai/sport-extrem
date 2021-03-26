package com.mihaineagu.web.controllers;

import com.mihaineagu.data.api.v1.models.LocationDTO;
import com.mihaineagu.data.api.v1.models.LocationListDTO;
import com.mihaineagu.service.interfaces.LocationService;
import com.mihaineagu.web.exceptions.RessourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {

    private final String SPORT_URI = "/api/v1/sports/";
    private final String LOCATION_URI = "/api/v1/location/";
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
        locationService.setUri(LOCATION_URI, SPORT_URI);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<LocationListDTO> getAllLocations(@RequestParam(name = "sports", defaultValue = "false") Boolean sports) {
        if(sports) {
            return new ResponseEntity<>(new LocationListDTO(locationService.findAllWithSports()), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new LocationListDTO(locationService.findAllWithoutSports()), HttpStatus.OK);
        }
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<LocationDTO> getLocationId(
            @RequestParam(name = "sports", defaultValue = "false") Boolean sports,
            @PathVariable(name = "id") Long id) {
        Optional<LocationDTO> locationDTO;
        if(sports) {
            locationDTO = locationService.findByIdWithSports(id);

        }else {
            locationDTO = locationService.findByIdWithoutSports(id);
        }
        if(locationDTO.isPresent())
            return new ResponseEntity<>(locationDTO.get(), HttpStatus.OK);
        else
            throw new RessourceNotFoundException();
    }
}

