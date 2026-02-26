package com.victor.bootcampproject.controller;

import com.victor.bootcampproject.dto.TaskDTO;

public interface TaskController {
    Object addTask(TaskDTO taskDTO);
    void markTaskCompleted(Long id);
    Object updateTask(Long idTask, String taskName);
}