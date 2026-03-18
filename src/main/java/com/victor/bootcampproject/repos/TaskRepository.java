package com.victor.bootcampproject.repos;

import com.victor.bootcampproject.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByUser(AppUser user);
    Optional<Task> getTaskByTodoID(Long id);
    Optional<Task> getTasksByFrequencyAndUser(Frequency frequency, AppUser user);
}
