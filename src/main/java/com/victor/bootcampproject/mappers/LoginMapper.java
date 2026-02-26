package com.victor.bootcampproject.mappers;

import com.victor.bootcampproject.dto.LoginDTO;
import com.victor.bootcampproject.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LoginMapper {
    LoginDTO toDto (AppUser dto);
    @Mapping(target = "userID", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "birthDate", ignore = true)
    AppUser toEntity (LoginDTO entity);
}