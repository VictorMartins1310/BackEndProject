package com.bootcamp.project.repos;

import com.bootcamp.project.model.Task;
import com.bootcamp.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByUser(User user);
    Optional<Task> findTaskByTodoID(Long id);
    Optional<Task> getTaskByTodoID(Long id);
}
