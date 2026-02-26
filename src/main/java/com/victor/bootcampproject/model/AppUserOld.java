package com.victor.bootcampproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static jakarta.persistence.FetchType.EAGER;

/**
 * Old App User Table, used for My SQL
 * @deprecated
 */
@Data
@NoArgsConstructor
//@RequiredArgsConstructor
@AllArgsConstructor
public class AppUserOld {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userID;
    @Column(unique=true) // for dont getting doubled email adresses
    private String email;
    private String password;
    // User Details
    private String firstName;
    private String lastName;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate birthDate;

    @ManyToMany(fetch = EAGER)
    private Collection<Role> roles = new ArrayList<>();

    public AppUserOld(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public void addRole(Role role){
        roles.add(role);
    }

    public void updateDetails( String firstName, String lastName, LocalDate birthDate) {
        setFirstName(firstName);
        setLastName(lastName);
        setBirthDate(birthDate);
    }
}
