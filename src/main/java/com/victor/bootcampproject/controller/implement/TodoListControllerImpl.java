package com.victor.bootcampproject.controller.implement;

import com.victor.bootcampproject.controller.TodoListController;
import com.victor.bootcampproject.dto.ToDoListDTO;
import com.victor.bootcampproject.mappers.TodoListMapper;
import com.victor.bootcampproject.model.AppUser;
import com.victor.bootcampproject.service.TodoItemService;
import com.victor.bootcampproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** This controller only have one function: Show All Lists (Task Lists & Shopping Lists) */
@RequiredArgsConstructor
@RestController
@RequestMapping(name = "todoList", value = "api/todolist")
public class TodoListControllerImpl implements TodoListController {

    // On this Controller is not so much to do
    private final TodoItemService toDoListService;
    //private final UserService userService;
    private final TodoListMapper todoListMapper;

    private final UserService userService;

    /** Method that show all Lists that a User haves */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ToDoListDTO> getTodoList(@AuthenticationPrincipal AppUser loggedUser){
        return todoListMapper.toDto(toDoListService.getAllItems(loggedUser));
    }
}