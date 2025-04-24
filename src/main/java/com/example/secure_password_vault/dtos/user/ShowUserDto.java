package com.example.secure_password_vault.dtos.user;

import org.springframework.web.multipart.MultipartFile;

public record ShowUserDto(String username, String email, String imageUser) {

}
