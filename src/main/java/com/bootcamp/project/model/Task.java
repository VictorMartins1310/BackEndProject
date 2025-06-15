package com.bootcamp.project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskID;
    private String task;
    /** True means Done, False to be Do */
    private Boolean done = false;

    public void taskDone(){ this.done = true; }
}