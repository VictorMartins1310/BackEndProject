package com.bootcamp.project.dto;

import jakarta.validation.constraints.Max;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
public class TaskDTO extends ToDoListDTO{
    @Max(message = "Reached Maximum of (64) Characters", value = 64)
    private String task;
    private Boolean done = false;
}
