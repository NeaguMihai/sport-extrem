package com.mihaineagu.service.implementations;

import com.mihaineagu.data.api.v1.mappers.CountryMapper;
import com.mihaineagu.data.api.v1.mappers.RegionMapper;
import com.mihaineagu.data.api.v1.models.CountryDTO;
import com.mihaineagu.data.domain.Country;
import com.mihaineagu.data.repository.CountryRepository;
import com.mihaineagu.service.interfaces.CountryService;
import com.mihaineagu.service.interfaces.RegionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class WebCountryService implements CountryService {

    private String uri;
    private final CountryRepository countryRepository;
    private final RegionService regionService;
    private final CountryMapper countryMapper;
    private final RegionMapper regionMapper;

    public WebCountryService(CountryRepository countryRepository, RegionService regionService, CountryMapper countryMapper, RegionMapper regionMapper) {
        this.regionMapper = regionMapper;
        this.uri = "";
        this.countryRepository = countryRepository;
        this.regionService = regionService;
        this.countryMapper = countryMapper;
    }

    @Override
    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public List<CountryDTO> findAllCountriesNoRegion() {
        return countryRepository
                    .findAll()
                    .stream()
                    .map(countryMapper::countryToDTO)
                    .map(countryDTO -> {
                        countryDTO.setUri(uri + countryDTO.getUri());
                        return countryDTO;
                    })
                    .collect(Collectors.toList());
    }

    @Override
    public List<CountryDTO> findAllCountriesWithRegion() {
        List<CountryDTO> countryDTOSList = new LinkedList<>();
        countryRepository.findAll().forEach(country -> {
            CountryDTO countryDTO = countryMapper.countryToDTO(country);
            countryDTO.setUri(uri + countryDTO.getUri());
            countryDTO.setRegions(
                            regionService
                                    .findByCountryIdWithoutLocation(country.getId())
            );
            countryDTOSList.add(countryDTO);

        });
        return countryDTOSList;
    }

    @Override
    public Optional<CountryDTO> findCountryByIdWithRegion(Long id) {
        return countryRepository.findById(id)
                .map(country -> {
                    CountryDTO countryDTO = countryMapper.countryToDTO(country);
                    countryDTO.setRegions(regionService.findByCountryIdWithoutLocation(id));
                    countryDTO.setUri(uri + countryDTO.getUri());
                    return countryDTO;
                });
    }

    @Override
    public Optional<CountryDTO> findCountryByIdWithoutRegion(Long id) {
        return countryRepository.findById(id)
                .map(country -> {
                    CountryDTO countryDTO = countryMapper.countryToDTO(country);
                    countryDTO.setUri(uri + countryDTO.getUri());
                    return countryDTO;
                });
    }

    @Override
    public Optional<CountryDTO> addNewCountry(CountryDTO countryDTO) {
        Optional<Country> existing = countryRepository.findCountryByCountryName(countryDTO.getCountryName());

        if(existing.isEmpty()) {
            Country saved = countryRepository.save(countryMapper.DTOTOCountry(countryDTO));
            return Optional.of(countryMapper.countryToDTO(saved));
        }else
            return Optional.empty();
    }

    @Override
    public CountryDTO saveCountry(CountryDTO countryDTO) {
        Country toBeSaved = countryMapper.DTOTOCountry(countryDTO);
        Country country = countryRepository.save(toBeSaved);

        return countryMapper.countryToDTO(country);
    }

    @Override
    public void deleteCountry(Long id) {

        countryRepository.deleteById(id);

    }


}
