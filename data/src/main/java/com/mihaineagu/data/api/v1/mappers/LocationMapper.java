package com.mihaineagu.data.api.v1.mappers;

import com.mihaineagu.data.api.v1.models.LocationDTO;
import com.mihaineagu.data.domain.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LocationMapper extends GenericMapper{

    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    @Mapping(target = "uri", source = "id", qualifiedByName = "idToUri")
    @Mapping(target = "sport", expression = "java(null)")
    @Mapping(target = "locationName", source = "locationName")
    LocationDTO locationToDTO(Location location);


    @Mapping(target = "id", source = "uri", qualifiedByName = "uriToId")
    @Mapping(target = "region", expression = "java(null)")
    @Mapping(target = "locationName", source = "locationName")
    Location DTOTOLocation(LocationDTO locationDTO);

}
