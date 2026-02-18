package com.bootcamp.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Task extends TodoItem {
    @NotNull
    private String task;
    @NotNull
    private Frequency frequency;

    public void markTaskCompleted(){ super.setCompleted(true); }
}