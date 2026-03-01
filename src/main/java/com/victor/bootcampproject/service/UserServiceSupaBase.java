package com.victor.bootcampproject.service;

import com.victor.bootcampproject.model.AppUser;
import com.victor.bootcampproject.model.Role;
import com.victor.bootcampproject.repos.RoleRepository;
import com.victor.bootcampproject.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceSupaBase{
    private final UserRepository userRepo;
    private final RoleRepository roleRepository;

    public Role addRole(String name){
        if (roleRepository.findByRole(name).isEmpty()) {
            return roleRepository.save(new Role(name));
        }else
            return roleRepository.findByRole(name).get();
    }

    public AppUser syncUserFromSupabase(UUID uuid, String email) {
        Optional<AppUser> user = userRepo.getUserByUserID(uuid);
        if (user.isPresent())
            return user.get();
        AppUser newUser = new AppUser(email, null);
        newUser.setUserID(uuid);
        newUser.addRole(addRole("ROLE_USER"));
        return userRepo.save(newUser);
    }
}
