package com.bootcamp.project.mappers;

import com.bootcamp.project.dto.UserDetailsDTO;
import com.bootcamp.project.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserDetailsMapper {
    UserDetailsDTO toDto(AppUser dto);
}