package com.bootcamp.project.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.Date;

@Data
public class ToDoListDTO {
    @NotBlank
    @Max(message = "Reached Maximum of (64) Characters", value = 64)
    private String todoListName;
    @NotNull
    private Date creationDate;
    @NotNull
    protected Boolean active;
}