package com.victor.bootcampproject.controller.implement;

import com.victor.bootcampproject.model.AppUser;
import com.victor.bootcampproject.model.Role;
import com.victor.bootcampproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * This Controller is for Administration
 * Main Functions:
 *          - Listing all Users
 *          - Delete a specific user
 *          - Create a Role
 */
@RequiredArgsConstructor
@RestController
@RequestMapping()
public class AdminController {
    private final UserService userService;
    private final String admin = "api/admin";


    /**
     * Save a new role
     * @param role
     * @return
     */
    @PostMapping(value = admin + "/roles")
    @ResponseStatus(HttpStatus.CREATED)
    public Role saveRole(@RequestBody String role) { return userService.addRole(role); }

    @GetMapping(value = admin + "/users")
    public List<AppUser> showAllUsers(){ return userService.showUsers(); }

    @GetMapping(value = "/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AppUser showDetails(@PathVariable(name = "id") UUID id){
        return userService.getUserByUserID(id);
    }

    @DeleteMapping(value = admin + "/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserByID(@PathVariable(name = "id") UUID id){
        userService.deleteUserByID(id);
    }
}