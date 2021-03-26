package com.mihaineagu.data.api.v1.mappers;

import com.mihaineagu.data.api.v1.models.CountryDTO;
import com.mihaineagu.data.domain.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CountryMapper extends GenericMapper{

    CountryMapper INSTANCE = Mappers.getMapper(CountryMapper.class);


    @Mapping(target = "uri", source = "id", qualifiedByName = "idToUri")
    @Mapping(target = "regionDTO", expression = "java(null)")
    @Mapping(target = "countryName", source = "countryName")
    CountryDTO countryToDTO(Country country);


    @Mapping(target = "regions", expression = "java(new java.util.HashSet<>())")
    @Mapping(target = "id", source = "uri", qualifiedByName = "uriToId")
    @Mapping(target = "countryName", source = "countryName")
    Country DTOTOCountry(CountryDTO countryDTO);
}
