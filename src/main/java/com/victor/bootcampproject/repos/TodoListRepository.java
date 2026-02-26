package com.victor.bootcampproject.repos;

import com.victor.bootcampproject.model.TodoItem;
import com.victor.bootcampproject.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoListRepository extends JpaRepository<TodoItem, Long> {
    List<TodoItem> findAllByUser(AppUser user);
}
