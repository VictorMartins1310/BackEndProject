package com.bootcamp.project.controller;

import com.bootcamp.project.dto.TaskDTO;

public interface TaskController {
    Object addTask(TaskDTO taskDTO);
    void markTaskCompleted(Long id);
    Object updateTask(Long idTask, String taskName);
}