package com.victor.bootcampproject.controller.implement;

import com.victor.bootcampproject.controller.UserController;
import com.victor.bootcampproject.dto.UserDetailsDTO;
import com.victor.bootcampproject.dto.LoginDTO;
import com.victor.bootcampproject.mappers.UserDetailsMapper;
import com.victor.bootcampproject.model.*;
import com.victor.bootcampproject.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


/** A controller for User, here a User can Register and Update his Information */
@RequiredArgsConstructor
@RestController
@RequestMapping(name = "users", value = "api/users")
@Profile({ "dev-MySQL", "dev-h2" })
public class UserControllerImpl implements UserController {
    private final UserServiceLocal userService;
    private final UserDetailsMapper userDetailsMapper;

    // private fields
    // Todo Remove loggedUser
    private AppUser loggedUser;

    /**
     * This function get the Authenticated User
     * @return Authenticated User
     */
    private AppUser getAuthUser() {
        return userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDetailsDTO newUser(@RequestBody LoginDTO loginData){
        return userDetailsMapper.toDto(userService.newUser(loginData.getEmail(), loginData.getPassword()));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDetailsDTO showDetails(){
        loggedUser = getAuthUser();
        return userDetailsMapper.toDto(loggedUser);
    }
    @PatchMapping(value = "/register/details")
    @ResponseStatus(HttpStatus.OK)
    public UserDetailsDTO updateDetailsOnRegister(@RequestBody AppUser userDetails) {
        loggedUser = getAuthUser();
        return userDetailsMapper.toDto(userService.updateDetails(loggedUser, userDetails.getFirstName(), userDetails.getLastName(), userDetails.getBirthDate().toString()));
    }
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDetailsDTO updateDetails(@RequestBody AppUser userDetails) {
        loggedUser = getAuthUser();
        return userDetailsMapper.toDto(userService.updateDetails(loggedUser, userDetails.getFirstName(), userDetails.getLastName(), userDetails.getBirthDate().toString()));
    }
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public AppUser getMine(){
        return getAuthUser();
    }
}