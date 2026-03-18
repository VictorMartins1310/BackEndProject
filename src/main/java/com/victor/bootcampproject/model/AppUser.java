package com.victor.bootcampproject.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static jakarta.persistence.FetchType.EAGER;

@Entity
@Data
@NoArgsConstructor
@Table(schema = "AppTodo")
public class AppUser {
    @Id
    private UUID userID;
    @Column(unique=true) // for avoid get doubled email Address's
    private String email;
    // User Details
    private String firstName;
    private String lastName;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate birthDate;

    @ManyToMany(fetch = EAGER)
    private Collection<Role> roles = new ArrayList<>();

    public AppUser(UUID userID, @NonNull String email) {
        setUserID(userID);
        setEmail(email);
    }

    public void setEmail(@NonNull String email) {
        this.email = email.toLowerCase();
    }

    public void addRole(Role role){
        roles.add(role);
    }

    public void updateDetails(String firstName, String lastName, LocalDate birthDate) {
        setFirstName(firstName);
        setLastName(lastName);
        setBirthDate(birthDate);
    }
}