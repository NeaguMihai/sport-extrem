package com.mihaineagu.web.controllers;

import com.mihaineagu.data.api.v1.mappers.LocationMapper;
import com.mihaineagu.data.api.v1.models.InformationDTO;
import com.mihaineagu.data.api.v1.models.LocationDTO;
import com.mihaineagu.data.api.v1.models.SportDTO;
import com.mihaineagu.data.domain.Information;
import com.mihaineagu.data.domain.Location;
import com.mihaineagu.service.interfaces.InformationService;
import com.mihaineagu.service.interfaces.LocationService;
import com.mihaineagu.service.interfaces.SportService;
import com.mihaineagu.web.exceptions.DuplicateEntityExceptions;
import com.mihaineagu.web.exceptions.FailSaveException;
import com.mihaineagu.web.exceptions.RessourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/api/v1")
public class InformationController {

    private static final Logger logger = LogManager.getLogger(InformationController.class);
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

        Optional<Location> locationOptional = locationService.findLocation(locationId);
        Optional<SportDTO> sportOptional = sportService.findById(sportId);

        locationOptional.orElseThrow(RessourceNotFoundException::new);
        sportOptional.orElseThrow(RessourceNotFoundException::new);

        Optional<InformationDTO> toBeSaved = informationService.getInformationById(locationId, sportId);

        toBeSaved.ifPresent(e -> {throw new DuplicateEntityExceptions();});

            //TODO: ADD validation for dates
            Optional<InformationDTO> savedInfo = informationService
                    .saveInformation(informationDTO,
                            locationOptional.get(),
                            sportOptional.get());

            savedInfo.orElseThrow(FailSaveException::new);

            sportOptional.get().setInformation(savedInfo.get());
            locationOptional.map(LocationMapper.INSTANCE::locationToDTO).get().setSports(List.of(sportOptional.get()));
            return locationOptional.map(LocationMapper.INSTANCE::locationToDTO).get();

    }

    @PutMapping(path = "/locations/{location_id}/sports/{sport_id}")
    @ResponseStatus(HttpStatus.OK)
    public LocationDTO updateInformation(
        @PathVariable(name = "location_id") Long locationId,
        @PathVariable(name = "sport_id") Long sportId,
        @RequestBody InformationDTO informationDTO) {

        Optional<LocationDTO> locationOptional = locationService.findByIdWithoutSports(locationId);
        Optional<SportDTO> sportOptional = sportService.findById(sportId);

        locationOptional.orElseThrow(RessourceNotFoundException::new);
        sportOptional.orElseThrow(RessourceNotFoundException::new);

        Optional<Information> toBeSaved = informationService.getInformationByIdforUpdate(locationId, sportId);

        toBeSaved.orElseThrow(RessourceNotFoundException::new);

        Optional<InformationDTO> saved = toBeSaved.flatMap(information -> {
           information.setPrice(informationDTO.getPrice());
           information.setEndPeriod(informationDTO.getEndingPeriod());
           information.setStartPeriod(informationDTO.getStartingPeriod());
           return informationService.saveInformation(information);
        });

        saved.orElseThrow(FailSaveException::new);
        sportOptional.get().setInformation(saved.get());
        locationOptional.get().setSports(List.of(sportOptional.get()));
        return locationOptional.get();

    }

    @DeleteMapping(path = "/locations/{location_id}/sports/{sport_id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteInformation(
            @PathVariable(name = "location_id") Long locationId,
            @PathVariable(name = "sport_id") Long sportId) {

        informationService.deleteInformationById(locationId, sportId);

    }
}
