package com.victor.bootcampproject.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank(message = "E-Mail is Mandatory")
    @Email(message = "E-Mail don't Follow rules")
    private String email;
    @NotBlank(message = "Password is Mandatory")
    private String password;
}