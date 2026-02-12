package com.bootcamp.project.repos;

import com.bootcamp.project.model.TodoItem;
import com.bootcamp.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoListRepository extends JpaRepository<TodoItem, Long> {
    List<TodoItem> findAllByUser(User user);
}
