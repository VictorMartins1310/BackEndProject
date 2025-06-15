package com.bootcamp.project.controller.implement;

import com.bootcamp.project.controller.UserController;
import com.bootcamp.project.dto.UserDetailsDTO;
import com.bootcamp.project.dto.LoginDTO;
import com.bootcamp.project.mappers.UserDetailsMapper;
import com.bootcamp.project.model.User;
import com.bootcamp.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


/** A controller for User, here a User can Register and Update his Information */
@RequiredArgsConstructor
@RestController
@RequestMapping(name = "users", value = "users")
public class UserControllerImpl implements UserController {
    private final UserService userService;

    private final UserDetailsMapper userDetailsMapper;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDetailsDTO newUser(@RequestBody LoginDTO loginData){
        return userDetailsMapper.toDto(userService.newUser(loginData.getEmail(), loginData.getPassword()));
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDetailsDTO showDetails(){
        return userDetailsMapper.toDto(userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
    }
    @PatchMapping(value = "/register/details")
    @ResponseStatus(HttpStatus.OK)
    public UserDetailsDTO updateDetailsOnRegister(@RequestBody User userDetails) {
        User loggedUser = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return userDetailsMapper.toDto(userService.updateDetails(loggedUser, userDetails.getFirstName(), userDetails.getLastName(), userDetails.getBirthDate().toString()));
    }
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDetailsDTO updateDetails(@RequestBody User userDetails) {
        User loggedUser = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return userDetailsMapper.toDto(userService.updateDetails(loggedUser, userDetails.getFirstName(), userDetails.getLastName(), userDetails.getBirthDate().toString()));
    }
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public User getMine(){
        return userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}