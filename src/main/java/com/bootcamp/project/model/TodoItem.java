package com.bootcamp.project.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(schema = "AppTodo")
public abstract class TodoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoID;
    private Date creationDate = new Date();
    /** True means Done, False to be Do */
    private Boolean completed = false;

    protected String type = this.getClass().getSimpleName();

    @NotNull
    @ManyToOne(optional = false)
    private User user;
}