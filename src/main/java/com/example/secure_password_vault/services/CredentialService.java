package com.example.secure_password_vault.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.secure_password_vault.dtos.credential.CreateCredentialDto;
import com.example.secure_password_vault.dtos.credential.UpdateCredentialDto;
import com.example.secure_password_vault.entities.Credential;
import com.example.secure_password_vault.repositories.CredentialRepository;

@Service
public class CredentialService {
	
	@Autowired
	CredentialRepository credentialRepository;
	
	public List<Credential> getAllCredentials() {
		var credentials = credentialRepository.findAll();
		return credentials;
	}
	
	public Credential createCredential(CreateCredentialDto createCredentialDto) {
		var credential = new Credential(
				createCredentialDto.systemName(),
				createCredentialDto.passwordBody()
		);
		return credentialRepository.save(credential);
	}
	
	public Credential updateCredentialById(long id, UpdateCredentialDto updateCredentialDto) {
		var credential = credentialRepository.findById(id).orElseThrow();
		
		credential.setSystemName(updateCredentialDto.systemName());
		credential.setPasswordBody(updateCredentialDto.passwordBody());
		
		return credentialRepository.save(credential);
	}
}


