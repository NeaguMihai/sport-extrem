package com.mihaineagu.web.controllers;

import com.mihaineagu.data.api.v1.models.InformationDTO;
import com.mihaineagu.data.api.v1.models.LocationDTO;
import com.mihaineagu.data.api.v1.models.SportDTO;
import com.mihaineagu.service.interfaces.InformationService;
import com.mihaineagu.service.interfaces.LocationService;
import com.mihaineagu.service.interfaces.SportService;
import com.mihaineagu.web.exceptions.DuplicateEntityExceptions;
import com.mihaineagu.web.exceptions.FailSaveException;
import com.mihaineagu.web.exceptions.RessourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class InformationController {

    private final InformationService informationService;
    private final LocationService locationService;
    private final SportService sportService;

    public InformationController(InformationService informationService, LocationService locationService, SportService sportService) {
        this.informationService = informationService;
        this.locationService = locationService;
        this.sportService = sportService;
    }

    @PostMapping(path = "/locations/{location_id}/sports/{sport_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public LocationDTO addNewSportToLocation(
            @PathVariable(name = "location_id") Long locationId,
            @PathVariable(name = "sport_id") Long sportId,
            @RequestBody InformationDTO informationDTO) {

        Optional<LocationDTO> locationOptional = locationService.findByIdWithoutSports(locationId);
        Optional<SportDTO> sportOptional = sportService.findById(sportId);
        if(locationOptional.isPresent() && sportOptional.isPresent()) {
            Optional<InformationDTO> toBeSaved = informationService.getInformationById(locationId, sportId);

            if (toBeSaved.isEmpty()) {
                Optional<InformationDTO> savedInfo = informationService
                        .saveInformation(informationDTO,
                                locationOptional.get(),
                                sportOptional.get());

                if (savedInfo.isPresent()) {
                    sportOptional.get().setInformation(savedInfo.get());
                    locationOptional.get().setSports(List.of(sportOptional.get()));
                    return locationOptional.get();

                }else
                    throw new FailSaveException();
            }else {
                throw new DuplicateEntityExceptions();
            }
        } else
            throw new RessourceNotFoundException();
    }
}
