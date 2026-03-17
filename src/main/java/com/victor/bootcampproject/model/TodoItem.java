package com.victor.bootcampproject.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(schema = "AppTodo")
public abstract class TodoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoID;
    private Date creationDate = new Date();
    private Date doOnDay = new Date();
    /** True means Done, False to be Do */
    private Boolean completed = false;

    protected String type = this.getClass().getSimpleName();

    @NotNull
    @ManyToOne(optional = false)
    private AppUser user;

    // Todo Review if this function is necessary
    public String Type(){
        return this.getClass().getSimpleName();
    }
}