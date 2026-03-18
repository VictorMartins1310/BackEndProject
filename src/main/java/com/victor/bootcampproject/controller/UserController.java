package com.victor.bootcampproject.controller;

import com.victor.bootcampproject.model.AppUser;

public interface UserController {
    Object updateDetailsOnRegister(AppUser userDetails);
    Object updateDetails(AppUser userDetails);
    Object showDetails();
    Object getMine();
}