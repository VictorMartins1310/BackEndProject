package com.victor.bootcampproject.controller;

import com.victor.bootcampproject.dto.ToDoListDTO;
import com.victor.bootcampproject.model.AppUser;

import java.util.List;

public interface TodoListController {
    List<ToDoListDTO> getTodoLists(AppUser loggedUser, int completed);
}