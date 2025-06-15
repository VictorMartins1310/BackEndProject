package com.bootcamp.project.controller.implement;

import com.bootcamp.project.controller.TodoListController;
import com.bootcamp.project.dto.ToDoListDTO;
import com.bootcamp.project.mappers.TodoListMapper;
import com.bootcamp.project.model.User;
import com.bootcamp.project.service.ToDoListService;
import com.bootcamp.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** This controller only have one function: Show All Lists (Task Lists & Shopping Lists) */
@RequiredArgsConstructor
@RestController
@RequestMapping(name = "todoList", value = "todolist")
public class TodoListControllerImpl implements TodoListController {
    // On this Controller is not so much to do
    private final ToDoListService toDoListService;
    private final UserService userService;
    private final TodoListMapper todoListMapper;


    /** Method that show all Lists that a User haves */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ToDoListDTO> getAllTodoList(){
        User loggedUser = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return todoListMapper.toDto(toDoListService.findAllByUser(loggedUser.getUserID()));
    }
}