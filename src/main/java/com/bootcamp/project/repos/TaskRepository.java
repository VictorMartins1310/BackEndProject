package com.bootcamp.project.repos;

import com.bootcamp.project.model.AppUser;
import com.bootcamp.project.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByUser(AppUser user);
    Optional<Task> findTaskByTodoID(Long id);
    Optional<Task> getTaskByTodoID(Long id);
}
