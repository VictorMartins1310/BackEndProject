package com.victor.bootcampproject.repos;

import com.victor.bootcampproject.model.AppUser;
import com.victor.bootcampproject.model.Frequency;
import com.victor.bootcampproject.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByUser(AppUser user);
    Optional<Task> findTaskByTodoID(Long id);
    Optional<Task> getTaskByTodoID(Long id);
    Optional<Task> getTasksByFrequencyAndUser(Frequency frequency, AppUser user);
}
