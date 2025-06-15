package com.bootcamp.project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@RequiredArgsConstructor
public class TaskList extends ToDoList{
    @OneToMany
    private List<Task> tasks = new ArrayList<>();

    public void addTask(Task newTask){ tasks.add(newTask); }
    public TaskList(String todoListName, User user) { super(todoListName, user); }
}