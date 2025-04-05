package com.example.secure_password_vault.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserDto(
        @NotBlank(message = "Username cannot be empty")
        @Size(max = 255, message = "Username cannot be longer than 255 characters")
        String username,

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Invalid email format")
        @Size(max = 600, message = "Email cannot be longer than 600 characters")
        String email,

        @NotBlank(message = "Password cannot be empty")
        @Size(max = 300, message = "Password cannot be longer than 300 characters")
        @Pattern(
    			regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*])\\S{6,}$",
    	        message = "Password must contain at least one letter, one number, and one special character and not empty spaces"
    	        )
        String password) {

}
