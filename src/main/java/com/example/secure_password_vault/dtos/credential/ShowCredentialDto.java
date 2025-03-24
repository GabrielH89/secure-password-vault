package com.example.secure_password_vault.dtos.credential;

import java.time.LocalDateTime;

public record ShowCredentialDto(Long id_password, String systemName, String passwordBody, LocalDateTime createAt, LocalDateTime updateAt) {

}
