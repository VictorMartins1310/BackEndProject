package com.victor.bootcampproject.service;

import com.victor.bootcampproject.model.AppUser;
import com.victor.bootcampproject.repos.RoleRepository;
import com.victor.bootcampproject.repos.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserServiceLocal extends UserService implements UserDetailsService {
    public UserServiceLocal(UserRepository userRepo, RoleRepository roleRepository, ShoppingListService shoppingListService, PasswordEncoder passwordEncoder) {
        super(userRepo, roleRepository, shoppingListService, passwordEncoder);
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Retrieve user with the given username
        // Check if user exists
        if (userRepo.getUserByEmail(email).isEmpty()) {
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            AppUser user = userRepo.getUserByEmail(email).get();
            // Create a collection of SimpleGrantedAuthority objects from the user's roles
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getRole()));
            });
            // Return the user details, including the username, password, and authorities
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
        }
    }
}
