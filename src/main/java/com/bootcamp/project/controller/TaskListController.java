package com.bootcamp.project.controller;

import com.bootcamp.project.dto.TaskListDTO;

public interface TaskListController {
    Object newTaskList(TaskListDTO taskListDTO);
    Object showTaskLists();
    Object updateTaskList(Long taskListID, String tklname);
    void deleteTaskList(Long taskListID);
}