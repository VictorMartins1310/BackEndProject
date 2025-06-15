package com.bootcamp.project.mappers;

import com.bootcamp.project.dto.LoginDTO;
import com.bootcamp.project.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LoginMapper {
    LoginDTO toDto (User dto);
    @Mapping(target = "userID", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "birthDate", ignore = true)
    User toEntity (LoginDTO entity);
}