package com.andaro.jobstracker.mapper;

import com.andaro.jobstracker.dto.CustomerRequest;
import com.andaro.jobstracker.dto.CustomerResponse;
import com.andaro.jobstracker.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    //@Mapping(target = "active", constant = "true") //default value
    Customer toModel(CustomerResponse customerResponse);

    Customer toModel(CustomerRequest customerRequest);

    CustomerResponse toDTO(Customer customer);
}
