package com.victor.bootcampproject.service;

import com.victor.bootcampproject.model.*;
import com.victor.bootcampproject.repos.TodoListRepository;
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

    public List<TodoItem> getItemsNotDone(AppUser user){
        return toDoListRepository.findAllByUserAndCompleted(user, false);
    }

    public List<TodoItem> getItemsDone(AppUser user){
        return toDoListRepository.findAllByUserAndCompleted(user, true);
    }
}