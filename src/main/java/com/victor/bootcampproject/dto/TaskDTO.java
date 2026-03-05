package com.victor.bootcampproject.dto;

import com.victor.bootcampproject.model.Frequency;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
public class TaskDTO extends ToDoListDTO{
    @Max(message = "Reached Maximum of (64) Characters", value = 64)
    private String task;
    @NotNull
    private Frequency frequency;
    private Boolean done = false;
}
