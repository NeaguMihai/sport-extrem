package com.mihaineagu.web.controllers;

import com.mihaineagu.data.api.v1.models.LocationDTO;
import com.mihaineagu.data.api.v1.models.LocationListDTO;
import com.mihaineagu.data.api.v1.models.RegionDTO;
import com.mihaineagu.service.interfaces.LocationService;
import com.mihaineagu.service.interfaces.RegionService;
import com.mihaineagu.web.exceptions.DuplicateEntityExceptions;
import com.mihaineagu.web.exceptions.FailSaveException;
import com.mihaineagu.web.exceptions.RessourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class LocationController {

    private final String LOCATION_URI = "/api/v1/locations/";
    private final String SPORT_URI = "/api/v1/sports/";
    private final LocationService locationService;
    private final RegionService regionService;

    public LocationController(LocationService locationService, RegionService regionService) {
        this.locationService = locationService;
        this.regionService = regionService;
        locationService.setUri(LOCATION_URI, SPORT_URI);
    }

    @GetMapping(path = "/regions/{region_id}/locations", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public LocationListDTO getLocationsByRegionId(
            @RequestParam(name = "sports", defaultValue = "false") Boolean sports,
            @PathVariable(name = "region_id") Long regionId) {
        if(sports) {
            return new LocationListDTO(locationService.findByRegionIdWithSports(regionId));
        }else {
            List<LocationDTO> locationDTOList = locationService.findByRegionIdWithoutSports(regionId);
            return new LocationListDTO(locationDTOList);
        }
    }

    @GetMapping(path = "/locations/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public LocationDTO getLocationId(
            @RequestParam(name = "sports", defaultValue = "false") Boolean sports,
            @PathVariable(name = "id") Long id) {
        Optional<LocationDTO> locationDTO;
        if(sports) {
            locationDTO = locationService.findByIdWithSports(id);

        }else {
            locationDTO = locationService.findByIdWithoutSports(id);
        }
        if(locationDTO.isPresent())
            return locationDTO.get();
        else
            throw new RessourceNotFoundException();
    }

    @PostMapping(path = "/regions/{region_id}/locations")
    @ResponseStatus(HttpStatus.CREATED)
    public LocationDTO addNewLocation(
            @RequestBody LocationDTO locationDTO,
            @PathVariable(name = "region_id") Long id) {

        Optional<RegionDTO> regionOptional = regionService.findByIdWithoutLocation(id);

        if (regionOptional.isPresent()) {

            locationDTO.setUri(null);
            if(locationService.findIfExistent(locationDTO, id))
                throw new DuplicateEntityExceptions();
            else {
                Optional<LocationDTO> saved = locationService.saveLocation(locationDTO, regionOptional.get());

                if(saved.isPresent())
                    return saved.get();
                else
                    throw new FailSaveException();
            }

        } else
            throw new RessourceNotFoundException();

    }
}

