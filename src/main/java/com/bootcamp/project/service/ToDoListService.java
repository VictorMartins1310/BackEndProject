package com.bootcamp.project.service;

import com.bootcamp.project.exception.ProjectException;
import com.bootcamp.project.model.ToDoList;
import com.bootcamp.project.model.User;
import com.bootcamp.project.repos.ToDoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ToDoListService {
    // Repository Section
    private final ToDoListRepository toDoListRepository;
    // Service Section
    private final UserService userService;
    public ToDoList newTodoList(UUID id, ToDoList todo) {
        User user = userService.findByUserID(id);
        ToDoList newTodoList = new ToDoList(todo.getTodoListName(), user);
        return toDoListRepository.save(newTodoList);
    }
    public ToDoList getTodoListByID(Long todoID){
        if (toDoListRepository.findById(todoID).isEmpty())
            throw new ProjectException("Todo List not Found");
        return toDoListRepository.findById(todoID).get();
    }
    public List<ToDoList> findAllByUser(UUID userID){
        User user = userService.findByUserID(userID);
        if (toDoListRepository.findAllByUser(user).isEmpty()) {
            throw new ProjectException("0 Todo Lists Found");
        }
        return toDoListRepository.findToDoListsByUser(user);
    }
    public List<ToDoList> findAllByUser(User user){
        if (toDoListRepository.findAllByUser(user).isEmpty()) {
            throw new ProjectException("0 Todo Lists Found");
        }
        return toDoListRepository.findToDoListsByUser(user);
    }
}