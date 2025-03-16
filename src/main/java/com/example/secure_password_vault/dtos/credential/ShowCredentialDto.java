package com.example.secure_password_vault.dtos.credential;

import java.time.LocalDateTime;

public record ShowCredentialDto(String systemName, String passwordBody, LocalDateTime createAt, LocalDateTime updateAt) {

}
