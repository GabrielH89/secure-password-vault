package com.example.secure_password_vault.dtos.credential;

import java.time.LocalDateTime;

public record UpdateCredentialDto(String systemName, String passwordBody, LocalDateTime updateAt) {

}
