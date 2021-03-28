package com.mihaineagu.web.controllers;

import com.mihaineagu.data.api.v1.models.CountryDTO;
import com.mihaineagu.data.api.v1.models.RegionDTO;
import com.mihaineagu.data.api.v1.models.RegionListDTO;
import com.mihaineagu.data.domain.Region;
import com.mihaineagu.service.interfaces.CountryService;
import com.mihaineagu.service.interfaces.RegionService;
import com.mihaineagu.web.exceptions.DuplicateEntityExceptions;
import com.mihaineagu.web.exceptions.FailSaveException;
import com.mihaineagu.web.exceptions.RessourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class RegionController {

    private static final Logger logger = LogManager.getLogger(RegionController.class);
    private final String URI = "/api/v1/regions/";
    private final RegionService regionService;
    private final CountryService countryService;

    public RegionController(RegionService regionService, CountryService countryService) {
        this.regionService = regionService;
        this.countryService = countryService;
        regionService.setUri(URI);
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

        regionDTO.orElseThrow(RessourceNotFoundException::new);
        return regionDTO.get();
    }

    @PostMapping(path = "/countries/{country_id}/regions")
    @ResponseStatus(HttpStatus.CREATED)
    public RegionDTO addNewRegion(
            @PathVariable(name = "country_id") Long id,
            @RequestBody RegionDTO regionDTO) {

        Optional<CountryDTO> countryOptional = countryService.findCountryByIdWithoutRegion(id);

        countryOptional.orElseThrow(RessourceNotFoundException::new);

        regionDTO.setUri(null);

        if(regionService.findIfExists(regionDTO, id)){
            throw new DuplicateEntityExceptions();
        }else {
            Optional<RegionDTO> saved = regionService.saveRegion(regionDTO, countryOptional.get());

            saved.orElseThrow(FailSaveException::new);
            return saved.get();
        }

    }

    @PutMapping("/regions/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RegionDTO updateRegoin(
            @PathVariable(name = "id") Long id,
            @RequestBody RegionDTO regionDTO) {

        Optional<Region> returned = regionService.findRegionById(id);

        returned.orElseThrow(RessourceNotFoundException::new);

        Optional<RegionDTO> saved = returned.flatMap(region -> {
                    region.setRegionName(regionDTO.getRegionName());
                    return regionService.saveRegion(region);
                    });

        saved.orElseThrow(FailSaveException::new);

        return saved.map(regionDTO1 ->{
            regionDTO1.setUri(URI+regionDTO1.getUri());
            return regionDTO1;}).get();
    }

    @DeleteMapping(path = "/regions/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteRegion(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "recursive", defaultValue = "false") Boolean recursive) {

        logger.info("am ajuns aici");
        if (recursive) {
            regionService.deleteRegionRecursive(id);
        }else {
            regionService.deleteRegion(id);
        }
    }

}
