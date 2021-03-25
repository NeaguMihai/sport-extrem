package com.mihaineagu.data.api.v1.mappers;

import com.mihaineagu.data.api.v1.models.InformationDTO;
import com.mihaineagu.data.domain.Information;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InformationMapper extends GenericMapper{

    InformationMapper INSTANCE = Mappers.getMapper(InformationMapper.class);

    @Mapping(target = "startingPeriod", source = "startPeriod")
    @Mapping(target = "endingPeriod", source = "endPeriod")
    @Mapping(target = "price", source = "price")
    InformationDTO informationToDTO(Information information);


    @Mapping(target = "sport", expression = "java(null)")
    @Mapping(target = "locationId", expression = "java(null)")
    @Mapping(target = "sportId", expression = "java(null)")
    @Mapping(target = "startPeriod", source = "startingPeriod")
    @Mapping(target = "location", expression = "java(null)")
    @Mapping(target = "endPeriod", source = "endingPeriod")
    Information DTOToInformation(InformationDTO informationDTO);


}
