package com.bootcamp.project.controller;

import com.bootcamp.project.dto.LoginDTO;
import com.bootcamp.project.model.User;

public interface UserController {
    Object newUser(LoginDTO loginData);
    Object updateDetailsOnRegister(User userDetails);
    Object updateDetails(User userDetails);
    Object showDetails();
    Object getMine();
}