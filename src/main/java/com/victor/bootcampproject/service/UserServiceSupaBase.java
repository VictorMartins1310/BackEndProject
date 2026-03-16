package com.victor.bootcampproject.service;

import com.victor.bootcampproject.model.AppUser;
import com.victor.bootcampproject.repos.RoleRepository;
import com.victor.bootcampproject.repos.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Profile({"dev-SupaBase", "prod", "default" })
@Service
public class UserServiceSupaBase extends UserService{
    public UserServiceSupaBase(UserRepository userRepo, RoleRepository roleRepository, ShoppingListService shoppingListService, PasswordEncoder passwordEncoder) {
        super(userRepo, roleRepository, shoppingListService, passwordEncoder);
    }

    public AppUser syncUserFromSupabase(UUID uuid, String email) {
        Optional<AppUser> user = userRepo.getUserByUserID(uuid);
        if (user.isPresent())
            return user.get();
        AppUser newUser = super.newUser(uuid, email);
        return userRepo.save(newUser);
    }
}
