package com.victor.bootcampproject.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "AppTodo")
public class Task extends TodoItem {
    @NotNull
    private String task;
    @NotNull
    private Frequency frequency = Frequency.Once;

    public void markTaskCompleted(){ super.setCompleted(true); }
}