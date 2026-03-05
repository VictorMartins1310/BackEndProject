package com.victor.bootcampproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
//@RequiredArgsConstructor
@AllArgsConstructor
@Table(schema = "AppTodo")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleID;
    private String role;

    public Role(String role) { this.role = role; }

}