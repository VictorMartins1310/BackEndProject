package com.bootcamp.project.repos;

import com.bootcamp.project.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> getUserByUserID(UUID id);
    Optional<AppUser> getUserByEmail(String email);
}
