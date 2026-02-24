package com.bootcamp.project.service;

import com.bootcamp.project.model.TodoItem;
import com.bootcamp.project.model.AppUser;
import com.bootcamp.project.repos.TodoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoItemService {
    // Repository Section
    private final TodoListRepository toDoListRepository;

    public List<TodoItem> getAllItems(AppUser user){
        return toDoListRepository.findAllByUser(user);
    }
}