package com.andaro.jobstracker.mapper;

import com.andaro.jobstracker.dto.CreateJobItemDTO;
import com.andaro.jobstracker.dto.JobItemDTO;
import com.andaro.jobstracker.model.JobItem;
import com.andaro.jobstracker.model.JobItemRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobItemMapper {

    JobItemMapper INSTANCE = Mappers.getMapper(JobItemMapper.class);

    //@Mapping(target = "active", constant = "true") //default value
    JobItem toModel(JobItemDTO dto);

    JobItem toModel(CreateJobItemDTO dto);

    JobItemDTO toDTO(JobItem model);


    List<JobItemDTO> toDTOs(List<JobItem> models);

    List<JobItem> toModels(List<JobItemDTO> dtos);
}
