package com.victor.bootcampproject.mappers;

import com.victor.bootcampproject.dto.UserDetailsDTO;
import com.victor.bootcampproject.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserDetailsMapper {
    UserDetailsDTO toDto(AppUser dto);
}