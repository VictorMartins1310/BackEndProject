package com.bootcamp.project.controller;

import com.bootcamp.project.dto.LoginDTO;
import com.bootcamp.project.model.AppUser;

public interface UserController {
    Object newUser(LoginDTO loginData);
    Object updateDetailsOnRegister(AppUser userDetails);
    Object updateDetails(AppUser userDetails);
    Object showDetails();
    Object getMine();
}