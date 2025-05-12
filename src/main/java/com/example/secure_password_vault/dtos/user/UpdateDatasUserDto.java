package com.example.secure_password_vault.dtos.user;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateDatasUserDto(
		 @NotBlank(message = "Username cannot be empty")
	     @Size(max = 255, message = "Username cannot be longer than 255 characters")
		 String username,
		 
		 @NotBlank(message = "Email cannot be empty")
	     @Email(message = "Invalid email format")
		 @Size(max = 600, message = "Email cannot be longer than 600 characters")
		 String email,
		 
		 
		 MultipartFile imageUser,
		 
        @Size(max = 300, message = "Password cannot be longer than 300 characters")
		 String password) {

}
