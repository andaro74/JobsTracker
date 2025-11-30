package com.andaro.jobstracker.mapper;

import com.andaro.jobstracker.dto.CustomerDTO;
import com.andaro.jobstracker.dto.CreateCustomerDTO;
import com.andaro.jobstracker.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    //@Mapping(target = "active", constant = "true") //default value
    Customer toModel(CustomerDTO customerDTO);

    Customer toModel(CreateCustomerDTO createCustomerDTO);

    CustomerDTO toDTO(Customer customer);
}
