package com.bootcamp.project.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.Date;

@Data
public class ToDoListDTO {
    private Long todoID;
    protected String type;
    private Date creationDate;
    private Boolean completed;
}