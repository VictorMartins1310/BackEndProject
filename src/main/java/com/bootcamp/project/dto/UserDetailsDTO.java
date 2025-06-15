package com.bootcamp.project.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserDetailsDTO {
    @NotBlank(message = "E-Mail is Mandatory")
    @Email(message = "E-Mail dont Follow rules")
    private String email;
    @NotBlank
    @Max(message = "Reached Maximum of (64) Characters", value = 64)
    private String firstName;
    @NotBlank
    @Max(message = "Reached Maximum of (128) Characters", value = 128)
    private String lastName;
    @Max(message = "Formatted Date: YYYY-MM-DD", value = 10)
    private LocalDate birthDate;
}
