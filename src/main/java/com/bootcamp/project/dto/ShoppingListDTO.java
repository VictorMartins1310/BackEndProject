package com.bootcamp.project.dto;

import jakarta.validation.constraints.Max;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ShoppingListDTO extends ToDoListDTO{
    @Max(message = "Reached Maximum of (64) Characters", value = 64)
    private String marketName;
}