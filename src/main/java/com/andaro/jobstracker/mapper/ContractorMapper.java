package com.andaro.jobstracker.mapper;

import com.andaro.jobstracker.dto.ContractorRequest;
import com.andaro.jobstracker.dto.ContractorResponse;
import com.andaro.jobstracker.dto.TradeTypeDTO;
import com.andaro.jobstracker.model.Contractor;
import com.andaro.jobstracker.model.TradeType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContractorMapper {

    ContractorMapper INSTANCE = Mappers.getMapper(ContractorMapper.class);

    Contractor toModel(ContractorResponse contractorResponse);

    Contractor toModel(ContractorRequest contractorRequest);

    ContractorResponse toDTO(Contractor contractor);

    // Enum conversions
    default TradeTypeDTO toDTO(TradeType tradeType) {
        if (tradeType == null) return null;
        return TradeTypeDTO.valueOf(tradeType.name());
    }

    default TradeType toModel(TradeTypeDTO tradeTypeDTO) {
        if (tradeTypeDTO == null) return null;
        return TradeType.valueOf(tradeTypeDTO.name());
    }

    List<ContractorResponse> toDTOs(List<Contractor> contractorList);

    List<Contractor> toModels(List<ContractorResponse> contractorDTOList);
}
