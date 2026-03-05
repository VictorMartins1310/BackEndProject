package com.victor.bootcampproject.mappers;


import com.victor.bootcampproject.dto.ShoppingListDTO;
import com.victor.bootcampproject.dto.ShoppingListProductsDTO;
import com.victor.bootcampproject.dto.ToDoListDTO;
import com.victor.bootcampproject.model.ShoppingList;
import com.victor.bootcampproject.model.TodoItem;
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