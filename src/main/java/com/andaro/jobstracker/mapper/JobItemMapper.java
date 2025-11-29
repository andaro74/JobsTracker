package com.andaro.jobstracker.mapper;

import com.andaro.jobstracker.dto.CreateJobItemDTO;
import com.andaro.jobstracker.dto.JobItemDTO;
import com.andaro.jobstracker.dto.JobStatusDTO;
import com.andaro.jobstracker.model.JobItem;
import com.andaro.jobstracker.model.JobStatus;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobItemMapper {

    JobItemMapper INSTANCE = Mappers.getMapper(JobItemMapper.class);

    JobItem toModel(JobItemDTO dto);

    JobItem toModel(CreateJobItemDTO dto);

    JobItemDTO toDTO(JobItem model);

    // Enum conversions
    default JobStatusDTO toDTO(JobStatus status) {
        if (status == null) return null;
        return JobStatusDTO.valueOf(status.name());
    }

    default JobStatus toModel(JobStatusDTO statusDTO) {
        if (statusDTO == null) return null;
        return JobStatus.valueOf(statusDTO.name());
    }

    List<JobItemDTO> toDTOs(List<JobItem> models);

    List<JobItem> toModels(List<JobItemDTO> dtos);
}
