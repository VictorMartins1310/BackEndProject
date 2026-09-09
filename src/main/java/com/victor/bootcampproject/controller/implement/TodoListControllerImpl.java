package com.victor.bootcampproject.controller.implement;

import com.victor.bootcampproject.controller.TodoListController;
import com.victor.bootcampproject.dto.ToDoListDTO;
import com.victor.bootcampproject.mappers.TodoListMapper;
import com.victor.bootcampproject.model.AppUser;
import com.victor.bootcampproject.service.*;
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
    private final TodoListMapper todoListMapper;

    private final UserService userService;

    /**
     * Return All TodoListItems by the Authenticated User
     * @param loggedUser Authenticated user
     * @return DTO TodoList
     */
    public List<ToDoListDTO> getAllTodoItems(AppUser loggedUser){
        return todoListMapper.toDto(toDoListService.getAllItems(loggedUser));
    }

    /**
     * Return All undone TodoListItems by the Authenticated User
     * @param loggedUser Authenticated user
     * @return DTO TodoList
     */
    public List<ToDoListDTO> getAllUndoneTodoItems(AppUser loggedUser){
        return todoListMapper.toDto(toDoListService.getItemsNotDone(loggedUser));
    }

    /**
     * Return All completed Tasks by the Authenticated User
     * @param loggedUser Authenticated user
     * @return DTO TodoList
     */
    public List<ToDoListDTO> getAllDoneTodoItems(AppUser loggedUser){
        return todoListMapper.toDto(toDoListService.getItemsDone(loggedUser));
    }

    public List<ToDoListDTO> getAllOnDate(AppUser loggedUser, String date){
        return todoListMapper.toDto(toDoListService.getAllOnDate(loggedUser, date));
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ToDoListDTO> getTodoLists(@AuthenticationPrincipal AppUser loggedUser, @RequestParam(required = true, value = "completed", defaultValue = "2") int completed) {
        if (completed == 0)
            return getAllDoneTodoItems(loggedUser);
        if (completed == 1)
            return getAllUndoneTodoItems(loggedUser);
        return getAllTodoItems(loggedUser);
    }
}