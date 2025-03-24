package com.example.secure_password_vault.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.secure_password_vault.dtos.user.CreateUserDto;
import com.example.secure_password_vault.dtos.user.LoginResponseDto;
import com.example.secure_password_vault.dtos.user.LoginUserDto;
import com.example.secure_password_vault.entities.User;
import com.example.secure_password_vault.repositories.UserRepository;
import com.example.secure_password_vault.security.TokenService;
import com.example.secure_password_vault.services.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	TokenService tokenService;
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody CreateUserDto createUserDto) {
		if(userRepository.findByEmail(createUserDto.email()) != null) {
			return ResponseEntity.badRequest().body("Email já cadastrado");
		}
		
		String encryptedPassword = passwordEncoder.encode(createUserDto.password());
		User newUser = new User(createUserDto.username(), createUserDto.email(), encryptedPassword);
		userRepository.save(newUser);
		return ResponseEntity.status(HttpStatus.CREATED).body("User created with success");
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody LoginUserDto loginUserDto) {
	    var user = userRepository.findByEmail(loginUserDto.email());

	    if (user == null) {
	        return ResponseEntity.status(404).body(new LoginResponseDto("Invalid email or password"));
	    }

	    try {
	        var authenticationToken = new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password());
	        var authentication = authenticationManager.authenticate(authenticationToken);

	        var userAuthenticated = (User) authentication.getPrincipal(); // Converte para User
	        var token = tokenService.generateToken(userAuthenticated);

	        return ResponseEntity.ok(new LoginResponseDto(token, userAuthenticated.getId()));
	    } catch (Exception e) {
	        return ResponseEntity.status(401).body(new LoginResponseDto("Invalid email or password"));
	    }
	}



	
}




