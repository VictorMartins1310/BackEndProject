package com.bootcamp.project.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper=false)
@Data
public class TaskListTasksDTO extends ToDoListDTO{
    private List<TaskDTO> tasks = new ArrayList<>();
}
