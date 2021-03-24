package com.mihaineagu.data.api.v1.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SportMapper {
    SportMapper INSTANCE = Mappers.getMapper(SportMapper.class);
}
