package com.victor.bootcampproject.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Profile;

import java.util.UUID;

/**
 * Old App User Table, used for My SQL
 //* @deprecated
 */
@NoArgsConstructor
@Getter @Setter
@Entity
@Profile({ "dev-MySQL", "dev-h2"} )
public class AppUserOld extends AppUser {
    private String password;

    public AppUserOld(@NonNull String email, String password) {
        super(UUID.randomUUID(), email);
        setPassword(password);
    }
}
