package com.mihaineagu.data.api.v1.mappers;

import com.mihaineagu.data.api.v1.models.RegionDTO;
import com.mihaineagu.data.domain.Region;
import lombok.Data;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RegionMapper extends GenericMapper{

    RegionMapper INSTANCE = Mappers.getMapper(RegionMapper.class);

    @Mapping(target = "uri", source = "id",qualifiedByName = "idToUri")
    @Mapping(target = "location", expression = "java(null)")
    @Mapping(target = "regionName", source = "regionName")
    RegionDTO regionToDTO(Region region);


    @Mapping(target = "id", source = "uri", qualifiedByName = "uriToId")
    @Mapping(target = "locations", expression = "java(new java.util.HashSet<>())")
    @Mapping(target = "country", expression = "java(null)")
    @Mapping(target = "regionName", source = "regionName")
    Region DTOToRegion(RegionDTO regionDTO);
}
