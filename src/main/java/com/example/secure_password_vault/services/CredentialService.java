package com.example.secure_password_vault.services;

import java.util.ArrayList;
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
	        
		List<Credential> credentials = credentialRepository.findByUserIdOrderByPosition(userId);
		
		if(credentials.isEmpty()) {
			throw new EmptyListException("Credentials not found");
		}else {
			return credentials.stream()
					.map(credential -> new ShowCredentialDto(
							credential.getId_password(),
							credential.getSystemName(), 
							credential.getPasswordBody(), 
							credential.getCreatedAt(), 
							credential.getUpdatedAt()))
					.collect(Collectors.toList());
		}	
	}
	
	public ShowCredentialDto getCredentialById(HttpServletRequest request, long id) {
		long userId = (Long) request.getAttribute("userId");
		
		var credential = credentialRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Credential with id \" + id + \" not found"));
		
		if(credential.getUser().getId() != userId) {
			throw new SecurityException("You are not authorized to delete this credential");
		}
		
		return new ShowCredentialDto(
			credential.getId_password(),
			credential.getSystemName(),
			credential.getPasswordBody(),
			credential.getCreatedAt(),
			credential.getUpdatedAt()
		);
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
	        return new ShowCredentialDto(credentialSaved.getId_password(), credentialSaved.getSystemName(), credentialSaved.getPasswordBody(), 
	        		credentialSaved.getCreatedAt(), credentialSaved.getUpdatedAt());
	    }

	
	public ShowCredentialDto updateCredentialById(HttpServletRequest request, long id, UpdateCredentialDto updateCredentialDto) {
		long userId = (Long) request.getAttribute("userId");
		
		var credential = credentialRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Credential with id " + id + " not found"));
	
		if (credential.getUser().getId() != userId) {
	        throw new SecurityException("You do not have permission to update this credential");
	    }
		
		
		credential.setSystemName(updateCredentialDto.systemName());
		credential.setPasswordBody(updateCredentialDto.passwordBody());
		credential.setUpdatedAt(updateCredentialDto.updateAt());
		
		credentialRepository.save(credential);
		
		
		return new ShowCredentialDto(
				credential.getId_password(),
				credential.getSystemName(),
				credential.getPasswordBody(),
				credential.getCreatedAt(),
				credential.getUpdatedAt()
		);
	}
	
	public void deleteAllCredentials(HttpServletRequest request) {
		long userId = (Long) request.getAttribute("userId");
		List<Credential> credentials = credentialRepository.findByUserId(userId);
		
		if(credentials.isEmpty()) {
			throw new EmptyListException("Credentials not found");
		}
		credentialRepository.deleteAll(credentials);
	}
	
	public void deleteCredentialById(HttpServletRequest request, long id) {
		long userId = (Long) request.getAttribute("userId");

	    // Busca a credencial pelo ID e verifica se pertence ao usuário logado
	    Credential credential = credentialRepository.findById(id)
	        .orElseThrow(() -> new NoSuchElementException("Credential with id " + id + " not found"));

	    if (credential.getUser().getId() != userId) {
	        throw new SecurityException("You do not have permission to delete this credential");
	    }

	    credentialRepository.deleteById(id);
	}
	
	public List<Credential> reorderCredential(List<Long> orderedIds, HttpServletRequest request) {
	    long userId = (Long) request.getAttribute("userId");
	    List<Credential> credentialsToSave = new ArrayList<>();

	    for (int i = 0; i < orderedIds.size(); i++) {
	        Long credentialId = orderedIds.get(i);
	        Credential credential = credentialRepository.findById(credentialId)
	                .orElseThrow(() -> new NoSuchElementException("Credential with id " + credentialId + " not found"));

	        if (credential.getUser().getId() != userId) {
	            throw new SecurityException("You do not have permission to reorder this credential");
	        }

	        credential.setPosition(i);
	        credentialsToSave.add(credential);
	    }

	    return credentialRepository.saveAll(credentialsToSave); // agora retorna a lista
	}

}
