package com.example.secure_password_vault.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.secure_password_vault.entities.Credential;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {
	
}
