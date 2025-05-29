package com.example.secure_password_vault.services;

import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.secure_password_vault.dtos.user.DeleteImageUserDto;
import com.example.secure_password_vault.dtos.user.ShowUserDto;
import com.example.secure_password_vault.dtos.user.UpdateDatasUserDto;
import com.example.secure_password_vault.entities.User;
import com.example.secure_password_vault.repositories.UserRepository;
import com.example.secure_password_vault.security.TokenService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ImageStorageService imageStorageService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	TokenService tokenService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByEmail(username);
	}
	
	public ShowUserDto getUserById(HttpServletRequest request) {
	    Long userId = (Long) request.getAttribute("userId");

	    if (userId == null) {
	        throw new IllegalArgumentException("User not found");
	    }

	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new IllegalArgumentException("User not found"));
	    
	    return new ShowUserDto(
	        user.getUsername(),
	        user.getEmail(),
	        user.getImageUser()
	    );
	}
	
	public DeleteImageUserDto deleteImageUser(HttpServletRequest request) {
		Long userId = (Long) request.getAttribute("userId");
		
		if (userId == null) {
	        throw new IllegalArgumentException("User not found");
	    }
		
		 User user = userRepository.findById(userId)
			        .orElseThrow(() -> new IllegalArgumentException("User not found"));
		 
	        try {
	            if (user.getImageUser() != null) {
	                imageStorageService.deleteImage(user.getImageUser());
	            }
	           
	        } catch (Exception e) {
	            throw new RuntimeException("Erro ao processar a imagem", e);
	        }
		 
		 user.setImageUser(null);
		 userRepository.save(user);
		 return new DeleteImageUserDto(null);
	}
	
	public ResponseEntity<Map<String, Object>> updateDatasUser(HttpServletRequest request, UpdateDatasUserDto updateDto) {
	    long userId = (Long) request.getAttribute("userId");

	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new NoSuchElementException("User not found"));

	    
	    UserDetails existingUserDetails = userRepository.findByEmail(updateDto.email());
	    if (existingUserDetails != null) {
	        User existingUser = (User) existingUserDetails; // Casting para User
	        if (existingUser.getId() != user.getId()) {
	            throw new IllegalArgumentException("Não foi possível atualizar os dados do usuário");
	        }
	    }

	    user.setUsername(updateDto.username());
	    user.setEmail(updateDto.email());

	    if (updateDto.password() != null && !updateDto.password().isBlank()) {
	        String encryptedPassword = passwordEncoder.encode(updateDto.password());
	        user.setPassword(encryptedPassword);
	    }

	    if (updateDto.imageUser() != null && !updateDto.imageUser().isEmpty()) {
	        try {
	            if (user.getImageUser() != null) {
	                imageStorageService.deleteImage(user.getImageUser());
	            }
	            String imagePath = imageStorageService.saveImage(updateDto.imageUser());
	            user.setImageUser(imagePath);
	        } catch (Exception e) {
	            throw new RuntimeException("Erro ao processar a imagem", e);
	        }
	    }

	    userRepository.save(user);

	    String newToken = tokenService.generateToken(user); // Gera novo token com novo e-mail

	    ShowUserDto userDto = new ShowUserDto(
	            user.getUsername(),
	            user.getEmail(),
	            null
	    );

	    return ResponseEntity.ok(Map.of(
	            "token", newToken,
	            "user", userDto
	    ));
	}
}
