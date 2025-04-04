package com.example.secure_password_vault.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.secure_password_vault.dtos.user.ShowUserDto;
import com.example.secure_password_vault.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

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
}



