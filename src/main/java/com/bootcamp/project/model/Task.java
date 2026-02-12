package com.bootcamp.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@RequiredArgsConstructor
public class Task extends TodoItem {
    @NotNull
    private String task;
    private Frequency frequency;

    public void markTaskCompleted(){ super.setCompleted(true); }

}