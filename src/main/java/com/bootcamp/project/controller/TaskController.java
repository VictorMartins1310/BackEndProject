package com.bootcamp.project.controller;

import com.bootcamp.project.dto.TaskDTO;

public interface TaskController {
    Object addTask(Long todoID, TaskDTO taskDTO);
    Object getAllTasksOfTaskList(Long id);
    void taskDone(Long id);
    Object updateTask(Long idTask, String taskName);
}