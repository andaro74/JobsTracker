package com.andaro.jobstracker.mapper;

import com.andaro.jobstracker.dto.CatalogDTO;
import com.andaro.jobstracker.dto.CreateCatalogDTO;
import com.andaro.jobstracker.dto.TradeTypeDTO;
import com.andaro.jobstracker.dto.PriceRateTypeDTO;
import com.andaro.jobstracker.model.Catalog;
import com.andaro.jobstracker.model.TradeType;
import com.andaro.jobstracker.model.PriceRateType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CatalogMapper {

    CatalogMapper INSTANCE = Mappers.getMapper(CatalogMapper.class);

    //@Mapping(target = "active", constant = "true") //default value
    Catalog toModel(CatalogDTO dto);

    Catalog toModel(CreateCatalogDTO dto);

    CatalogDTO toDTO(Catalog model);

    // Enum conversions
    default TradeTypeDTO toDTO(TradeType tradeType) {
        if (tradeType == null) return null;
        return TradeTypeDTO.valueOf(tradeType.name());
    }

    default TradeType toModel(TradeTypeDTO tradeTypeDTO) {
        if (tradeTypeDTO == null) return null;
        return TradeType.valueOf(tradeTypeDTO.name());
    }

    default PriceRateTypeDTO toDTO(PriceRateType priceRateType) {
        if (priceRateType == null) return null;
        return PriceRateTypeDTO.valueOf(priceRateType.name());
    }

    default PriceRateType toModel(PriceRateTypeDTO priceRateTypeDTO) {
        if (priceRateTypeDTO == null) return null;
        return PriceRateType.valueOf(priceRateTypeDTO.name());
    }

    default CatalogDTO stripKeys(Catalog model) {
        if (model == null) {
            return null;
        }
        return new CatalogDTO(
                model.getCatalogId(),
                model.getCatalogName(),
                model.getCatalogDescription(),
                model.getPrice(),
                model.getSku(),
                toDTO(model.getTradeType()),
                toDTO(model.getPriceRateType()),
                model.getCreatedOn(),
                model.getModifiedOn()
        );
    }
}
