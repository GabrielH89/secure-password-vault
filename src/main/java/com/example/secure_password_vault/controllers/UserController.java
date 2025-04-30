package com.example.secure_password_vault.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.secure_password_vault.dtos.user.DeleteImageUserDto;
import com.example.secure_password_vault.dtos.user.ShowUserDto;
import com.example.secure_password_vault.dtos.user.UpdateDatasUserDto;
import com.example.secure_password_vault.services.CredentialService;
import com.example.secure_password_vault.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping
	public ResponseEntity<ShowUserDto> getUserById(HttpServletRequest request) {
		ShowUserDto user = userService.getUserById(request);
		return ResponseEntity.status(200).body(user);
	}
	
	@DeleteMapping("/image")
	public ResponseEntity<DeleteImageUserDto> deleteImageUser(HttpServletRequest request) {
		DeleteImageUserDto dto = userService.deleteImageUser(request);
		return ResponseEntity.ok(dto);
	}
	
	@PutMapping
	public ResponseEntity<ShowUserDto> updateDatasUser(HttpServletRequest request, @Valid @ModelAttribute UpdateDatasUserDto updateDto) {
		ShowUserDto updatedDatasUser = userService.updateDatasUser(request, updateDto);
		return ResponseEntity.status(200).body(updatedDatasUser);
	}
}



