package com.mihaineagu.data.api.v1.mappers;

import com.mihaineagu.data.api.v1.models.SportDTO;
import com.mihaineagu.data.domain.Sport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SportMapper extends GenericMapper{
    SportMapper INSTANCE = Mappers.getMapper(SportMapper.class);


    @Mapping(target = "uri", source = "id", qualifiedByName = "idToUri")
    @Mapping(target = "sportType", source = "sportType")
    @Mapping(target = "information", expression = "java(null)")
    SportDTO sportToDTO(Sport sport);

    @Mapping(target = "id", source = "uri", qualifiedByName = "uriToId")
    @Mapping(target = "sportType", source = "sportType")
    Sport DTOToSport(SportDTO sportDTO);


}
