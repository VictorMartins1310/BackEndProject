package com.victor.bootcampproject.controller;

import com.victor.bootcampproject.dto.TaskDTO;
import com.victor.bootcampproject.model.*;

import java.util.Optional;

public interface TaskController {
    Object addTask(AppUser optionalUser, TaskDTO taskDTO);
    void markTaskCompleted(Long id);
    Object updateTask(Long idTask, String taskName);
    Optional<Task> getDailyTasks(AppUser user, Frequency freq);
}