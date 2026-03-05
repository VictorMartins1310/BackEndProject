package com.victor.bootcampproject.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class LoginDTO {
    private UUID userID;
    @NotBlank(message = "E-Mail is Mandatory")
    @Email(message = "E-Mail don't Follow rules")
    private String email;
    @NotBlank(message = "Password is Mandatory")
    private String password;
}