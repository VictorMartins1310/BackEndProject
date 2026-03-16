package com.victor.bootcampproject.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@NoArgsConstructor
@Table(schema = "AppTodo")
public class Task extends TodoItem {
    @NotNull
    private String task;
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "app_todo.frequency")
    @NotNull
    private Frequency frequency = Frequency.Once;
    @OneToMany
    private List<RealizedOnDay> realizedOn = new ArrayList<>();

    public void markTaskCompleted(){ super.setCompleted(true); }
}