package com.victor.bootcampproject.mappers;

import com.victor.bootcampproject.dto.TaskDTO;
import com.victor.bootcampproject.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper {
    TaskDTO toDto (Task dto);
    List<TaskDTO> toDto (List<Task> dto);
    Task toEntity (TaskDTO entity);

}