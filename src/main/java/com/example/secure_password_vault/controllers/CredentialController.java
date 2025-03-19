package com.example.secure_password_vault.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.secure_password_vault.dtos.credential.CreateCredentialDto;
import com.example.secure_password_vault.dtos.credential.ShowCredentialDto;
import com.example.secure_password_vault.dtos.credential.UpdateCredentialDto;
import com.example.secure_password_vault.entities.Credential;
import com.example.secure_password_vault.services.CredentialService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/credentials")
public class CredentialController {
	
	@Autowired
	CredentialService credentialService;
	
	@GetMapping
	public List<ShowCredentialDto> getAll(HttpServletRequest request) {
		return credentialService.getAllCredentials(request);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ShowCredentialDto> getById(HttpServletRequest request, @PathVariable long id) {
		ShowCredentialDto credential = credentialService.getCredentialById(request, id);
		return ResponseEntity.status(200).body(credential);
	}
	
	@PostMapping
    public ResponseEntity<ShowCredentialDto> create(@RequestBody CreateCredentialDto createDto, HttpServletRequest request) {
        ShowCredentialDto createdCredential = credentialService.createCredential(createDto, request);
        return ResponseEntity.status(201).body(createdCredential);  
    }
	
	@PutMapping("/{id}")
	public ResponseEntity<ShowCredentialDto> updateById(HttpServletRequest request, 
			@PathVariable long id, @RequestBody UpdateCredentialDto updateCredentialDto) {
		ShowCredentialDto updatedCredential = credentialService.updateCredentialById(request, id, updateCredentialDto);
		return ResponseEntity.status(200).body(updatedCredential);
	}
	
	@DeleteMapping
	public ResponseEntity<Void> deleteAll(HttpServletRequest request) {
		credentialService.deleteAllCredentials(request);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(HttpServletRequest request, @PathVariable long id) {
		credentialService.deleteCredentialById(request, id);
		return ResponseEntity.noContent().build();
	}
}


