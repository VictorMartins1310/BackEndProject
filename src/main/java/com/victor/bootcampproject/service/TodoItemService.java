package com.victor.bootcampproject.service;

import com.victor.bootcampproject.model.TodoItem;
import com.victor.bootcampproject.model.AppUser;
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
}