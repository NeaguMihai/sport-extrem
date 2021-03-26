package com.mihaineagu.web.controllers;

import com.mihaineagu.data.api.v1.models.SportListDTO;
import com.mihaineagu.service.interfaces.SportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/sports")
public class SportController {

    private final String URI = "/api/v1/sports";
    private final SportService sportService;

    public SportController(SportService sportService) {
        this.sportService = sportService;
        sportService.setUri(URI);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<SportListDTO> getAllSports() {
        return new ResponseEntity<>(new SportListDTO(sportService.findAllSports()), HttpStatus.OK);
    }
}
