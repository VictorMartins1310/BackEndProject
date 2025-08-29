package com.bootcamp.project.controller.implement;

import com.bootcamp.project.model.ProductType;
import com.bootcamp.project.model.Role;
import com.bootcamp.project.model.User;
import com.bootcamp.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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



    @GetMapping("/types")
    public List<String> getTypeValues() {
        return Arrays.stream(ProductType.values())
                .map(Enum::name)
                .toList();
    }

    /**
     * Save a new role
     * @param role
     * @return
     */
    @PostMapping(value = admin + "/roles")
    @ResponseStatus(HttpStatus.CREATED)
    public Role saveRole(@RequestBody String role) { return userService.addRole(role); }
    @GetMapping(value = admin + "/users")
    public List<User> showAllUsers(){ return userService.showUsers(); }
    @GetMapping(value = "/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User showDetails(@PathVariable(name = "id") UUID id){
        return userService.findByUserID(id);
    }
    @DeleteMapping(value = admin + "/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserByID(@PathVariable(name = "id") UUID id){
        userService.deleteUserByID(id);
    }
}