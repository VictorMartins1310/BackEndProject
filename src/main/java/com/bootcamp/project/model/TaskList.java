package com.bootcamp.project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
public class TaskList {
    @OneToMany
    private List<Task> tasks = new ArrayList<>();

    public void addTask(Task newTask){ tasks.add(newTask); }

}