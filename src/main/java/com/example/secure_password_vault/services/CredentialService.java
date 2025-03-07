package com.example.secure_password_vault.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.secure_password_vault.dtos.credential.CreateCredentialDto;
import com.example.secure_password_vault.dtos.credential.UpdateCredentialDto;
import com.example.secure_password_vault.entities.Credential;
import com.example.secure_password_vault.exceptions.EmptyListException;
import com.example.secure_password_vault.repositories.CredentialRepository;

@Service
public class CredentialService {
	
	@Autowired
	CredentialRepository credentialRepository;
	
	public List<Credential> getAllCredentials() {
		var credentials = credentialRepository.findAll();
		
		if(credentials.isEmpty()) {
			throw new EmptyListException("Credentials not found");
		}
		return credentials;
	}
	
	public Credential getCredentialById(long id) {
		return credentialRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Crdential with id " + id + " not found"));
		
	}
	
	public Credential createCredential(CreateCredentialDto createCredentialDto) {
		var credential = new Credential(
				createCredentialDto.systemName(),
				createCredentialDto.passwordBody()
		);
		return credentialRepository.save(credential);
	}
	
	public Credential updateCredentialById(long id, UpdateCredentialDto updateCredentialDto) {
		var credential = credentialRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Credential with id " + id + " not found"));
		
		
		credential.setSystemName(updateCredentialDto.systemName());
		credential.setPasswordBody(updateCredentialDto.passwordBody());
		
		return credentialRepository.save(credential);
	}
}


