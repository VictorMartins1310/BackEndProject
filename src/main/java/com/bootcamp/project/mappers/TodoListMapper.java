package com.bootcamp.project.mappers;


import com.bootcamp.project.dto.*;
import com.bootcamp.project.model.ShoppingList;
import com.bootcamp.project.model.TodoItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TodoListMapper {

    //#--------# TodoList Mapping Section #--------#//
    //########## TodoList Mapping Section ##########\\
    List<ToDoListDTO> toDto(List<TodoItem> dto);
    List<ShoppingListDTO> toDTO(List<ShoppingList> dto);
    ShoppingListProductsDTO toDTO(ShoppingList dto);
    ToDoListDTO toDto(TodoItem dto);
    ShoppingListDTO toDto(ShoppingList dto);
    // ShoppingList Mapping Section

}