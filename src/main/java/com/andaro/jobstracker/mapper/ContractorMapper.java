package com.andaro.jobstracker.mapper;

import com.andaro.jobstracker.dto.CreateContractorDTO;
import com.andaro.jobstracker.dto.ContractorDTO;
import com.andaro.jobstracker.model.Contractor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContractorMapper {

    ContractorMapper INSTANCE = Mappers.getMapper(ContractorMapper.class);

    //@Mapping(target = "active", constant = "true") //default value
    Contractor toModel(ContractorDTO contractorDTO);

    Contractor toModel(CreateContractorDTO createContractorDTO);

    ContractorDTO toDTO(Contractor contractor);


    List<ContractorDTO> toDTOs(List<Contractor> contractorList);

    List<Contractor> toModels(List<ContractorDTO> contractorDTOList);
}
