package com.mihaineagu.data.api.v1.mappers;

import com.mihaineagu.data.api.v1.models.InformationDTO;
import com.mihaineagu.data.api.v1.models.LocationDTO;
import com.mihaineagu.data.domain.LocationSport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InformationMapper extends GenericMapper{

    InformationMapper INSTANCE = Mappers.getMapper(InformationMapper.class);

    @Mapping(target = "startingPeriod", source = "startPeriod")
    @Mapping(target = "endingPeriod", source = "endPeriod")
    @Mapping(target = "price", source = "price")
    InformationDTO locationSportToInformation(LocationSport locationSport);


    @Mapping(target = "sport", expression = "java(null)")
    @Mapping(target = "locationId", expression = "java(null)")
    @Mapping(target = "sportId", expression = "java(null)")
    @Mapping(target = "startPeriod", source = "startingPeriod")
    @Mapping(target = "location", expression = "java(null)")
    @Mapping(target = "endPeriod", source = "endingPeriod")
    LocationSport informationToLocationSport(InformationDTO informationDTO);


    @Mapping(target = "startPeriod", source = "sport.information.startingPeriod")
    @Mapping(target = "endPeriod", source = "sport.information.endingPeriod")
    @Mapping(target = "price", source = "sport.information.price")
    @Mapping(target = "sportId", source = "sport.uri", qualifiedByName = "uriToId")
    @Mapping(target = "locationId", source = "uri", qualifiedByName = "uriToId")
    @Mapping(target = "location", expression = "java(null)")
    @Mapping(target = "sport", expression = "java(null)")
    LocationSport locationDTOToLocationSport(LocationDTO locationDTO);

}
