package com.bootcamp.project.repos;

import com.bootcamp.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> getUserByUserID(UUID id);
    Optional<User> getUserByEmail(String email);
}
