package com.mihaineagu.web.controllers;

import com.mihaineagu.data.api.v1.models.SportDTO;
import com.mihaineagu.data.api.v1.models.SportListDTO;
import com.mihaineagu.service.interfaces.SportService;
import com.mihaineagu.web.exceptions.DuplicateEntityExceptions;
import com.mihaineagu.web.exceptions.FailSaveException;
import com.mihaineagu.web.exceptions.RessourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/v1")
public class SportController {

    private final String URI = "/api/v1/sports/";
    private final SportService sportService;

    public SportController(SportService sportService) {
        this.sportService = sportService;
        sportService.setUri(URI);
    }

    @GetMapping(path = "/sports", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public SportListDTO getAllSports() {
        return new SportListDTO(sportService.findAllSports());
    }

    @GetMapping(path = "/sports/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public SportDTO getById(@PathVariable(value = "id") Long id) {
        Optional<SportDTO> sportDTO = sportService.findById(id);
        if(sportDTO.isPresent()) {
            return sportDTO.get();
        }else {
              throw new RessourceNotFoundException();
            }
    }

    @PostMapping(path = "/sports", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public SportDTO addNewSport(@RequestBody @NonNull SportDTO sportDTO) {
        sportDTO.setUri(null);
        Optional<SportDTO> returned = sportService.addNewSport(sportDTO);

        returned.orElseThrow(DuplicateEntityExceptions::new);

        return returned.get();
    }

    @PutMapping(path = "/sports/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SportDTO updateSport(
            @PathVariable(name = "id") Long id,
            @RequestBody SportDTO sportDTO) {
        Optional<SportDTO> returned = sportService.findById(id);

        returned.orElseThrow(RessourceNotFoundException::new);

        Optional<SportDTO> saved = returned.flatMap(toBeSaved -> {
            toBeSaved.setSportType(sportDTO.getSportType());
            return sportService.saveSport(toBeSaved);
        });

        saved.orElseThrow(FailSaveException::new);

        return saved.map(sportDTO1 -> {
            sportDTO1.setUri(URI + sportDTO1.getUri());
            return sportDTO1;
        }).get();
    }
}
