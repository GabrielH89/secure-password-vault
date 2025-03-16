package com.example.secure_password_vault.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.secure_password_vault.dtos.credential.CreateCredentialDto;
import com.example.secure_password_vault.dtos.credential.ShowCredentialDto;
import com.example.secure_password_vault.dtos.credential.UpdateCredentialDto;
import com.example.secure_password_vault.entities.Credential;
import com.example.secure_password_vault.entities.User;
import com.example.secure_password_vault.exceptions.EmptyListException;
import com.example.secure_password_vault.repositories.CredentialRepository;
import com.example.secure_password_vault.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class CredentialService {
	
	@Autowired
	CredentialRepository credentialRepository;
	
	@Autowired
	UserRepository userRepository;
	
	public List<ShowCredentialDto> getAllCredentials(HttpServletRequest request) {
		long userId = (Long) request.getAttribute("userId");
	        
		List<Credential> credentials = credentialRepository.findByUserId(userId);
		
		if(credentials.isEmpty()) {
			throw new EmptyListException("Credentials not found");
		}else {
			return credentials.stream()
					.map(credential -> new ShowCredentialDto(
							credential.getSystemName(), 
							credential.getPasswordBody(), 
							credential.getCreatedAt(), 
							credential.getUpdatedAt()))
					.collect(Collectors.toList());
		}	
	}
	
	public ShowCredentialDto getCredentialById(long id) {
		var credential = credentialRepository.findById(id);
		if(credential.isPresent()) {
			var credentialEntity = credential.get();
			return new ShowCredentialDto(
				credentialEntity.getSystemName(), 
				credentialEntity.getPasswordBody(), 
				credentialEntity.getCreatedAt(), 
				credentialEntity.getUpdatedAt());
		}else{
			throw new NoSuchElementException("Credential with id " + id + " not found");
		}
	}
	
	@Transactional
	 public ShowCredentialDto createCredential(CreateCredentialDto createCredentialDto, HttpServletRequest request) throws RuntimeException {
	        // Extrai o ID do usuário da requisição
	        long userId = (Long) request.getAttribute("userId");
	        
	        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
	        
	        // Cria uma nova credencial e associa ao usuário
	        var credential = new Credential(
	                createCredentialDto.systemName(),
	                createCredentialDto.passwordBody()
	        );
	        credential.setUser(user); 
	        var credentialSaved = credentialRepository.save(credential);
	        return new ShowCredentialDto(credentialSaved.getSystemName(), credentialSaved.getPasswordBody(), 
	        		credentialSaved.getCreatedAt(), credentialSaved.getUpdatedAt());
	    }

	
	public Credential updateCredentialById(long id, UpdateCredentialDto updateCredentialDto) {
		var credential = credentialRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Credential with id " + id + " not found"));
		
		credential.setSystemName(updateCredentialDto.systemName());
		credential.setPasswordBody(updateCredentialDto.passwordBody());
		
		return credentialRepository.save(credential);
	}
	
	public void deleteAllCredentials() {
		var credentials = credentialRepository.findAll();
		
		if(credentials.isEmpty()) {
			throw new EmptyListException("Credentials not found");
		}
		credentialRepository.deleteAll(credentials);
	}
	
	public Credential deleteCredentialById(long id) {
		var credential = credentialRepository.findById(id);
		if(credential.isPresent()) {
			credentialRepository.deleteById(id);
			return credential.get();
		}else {
			throw new NoSuchElementException("Element with id " + id + " not found");
		}
	}
}
