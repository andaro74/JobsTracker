package com.andaro.jobstracker.mapper;

import com.andaro.jobstracker.dto.CatalogDTO;
import com.andaro.jobstracker.dto.CreateCatalogDTO;
import com.andaro.jobstracker.model.Catalog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CatalogMapper {

    CatalogMapper INSTANCE = Mappers.getMapper(CatalogMapper.class);

    //@Mapping(target = "active", constant = "true") //default value
    Catalog toModel(CatalogDTO dto);

    Catalog toModel(CreateCatalogDTO dto);

    CatalogDTO toDTO(Catalog model);


    List<CatalogDTO> toDTOs(List<Catalog> models);

    List<Catalog> toModels(List<CatalogDTO> dtos);
}
