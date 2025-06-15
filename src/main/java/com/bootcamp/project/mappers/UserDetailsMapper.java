package com.bootcamp.project.mappers;

import com.bootcamp.project.dto.UserDetailsDTO;
import com.bootcamp.project.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserDetailsMapper {
    UserDetailsDTO toDto(User dto);
}