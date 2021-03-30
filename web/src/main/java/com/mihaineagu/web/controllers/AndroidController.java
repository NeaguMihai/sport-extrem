package com.mihaineagu.web.controllers;

import com.mihaineagu.data.api.v1.models.AndroidRequestDTO;
import com.mihaineagu.data.api.v1.models.LocationDTO;
import com.mihaineagu.data.api.v1.models.LocationListDTO;
import com.mihaineagu.service.interfaces.InformationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class AndroidController {

    private static final Logger logger = LogManager.getLogger(InformationController.class);
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private final InformationService informationService;


    public AndroidController(InformationService informationService) {
        this.informationService = informationService;

    }

    @GetMapping("/android")
    @ResponseStatus(HttpStatus.OK)
    public LocationListDTO getLocationList(
            @RequestBody @Valid AndroidRequestDTO androidRequestDTO) {

        if(androidRequestDTO.getStartDate().compareTo(androidRequestDTO.getEndDate()) >=0)
            throw new DataIntegrityViolationException("Bad date format");

        return new LocationListDTO(new ArrayList<>(
                informationService.getResultFromAndroidRequest(androidRequestDTO.getSportNames(),
                androidRequestDTO.getStartDate(),
                androidRequestDTO.getEndDate()
                ))
        );

    }

}
