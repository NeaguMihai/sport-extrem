package com.mihaineagu.web.controllers;

import com.mihaineagu.data.api.v1.models.SportDTO;
import com.mihaineagu.data.api.v1.models.SportListDTO;
import com.mihaineagu.service.interfaces.SportService;
import com.mihaineagu.web.exceptions.DuplicateEntityExceptions;
import com.mihaineagu.web.exceptions.RessourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/v1/sports")
public class SportController {

    private final String URI = "/api/v1/sports/";
    private final SportService sportService;

    public SportController(SportService sportService) {
        this.sportService = sportService;
        sportService.setUri(URI);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<SportListDTO> getAllSports() {
        return new ResponseEntity<>(new SportListDTO(sportService.findAllSports()), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<SportDTO> getById(@PathVariable(value = "id") Long id) {
        Optional<SportDTO> sportDTO = sportService.findById(id);
        if(sportDTO.isPresent()) {
            return new ResponseEntity<>(sportDTO.get(), HttpStatus.OK);
        }else {
              throw new RessourceNotFoundException();
            }
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<SportDTO> addNewSport(@RequestBody @NonNull SportDTO sportDTO) {
        sportDTO.setUri(null);
        Optional<SportDTO> returned = sportService.addNewSport(sportDTO);
        if(returned.isPresent())
            return new ResponseEntity<>(returned.get(), HttpStatus.CREATED);
        else
            throw new DuplicateEntityExceptions();
    }
}
