package com.victor.bootcampproject.controller;

import com.victor.bootcampproject.model.AppUser;

public interface TodoListController {
    Object getTodoList(AppUser optionalUser);
}