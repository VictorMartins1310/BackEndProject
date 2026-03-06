package com.victor.bootcampproject.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "AppTodo")
public class Task extends TodoItem {
    @NotNull
    private String task;
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "app_todo.frequency")
    @NotNull
    private Frequency frequency = Frequency.Once;

    public void markTaskCompleted(){ super.setCompleted(true); }
}