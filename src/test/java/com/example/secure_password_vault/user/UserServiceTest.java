package com.example.secure_password_vault.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.secure_password_vault.dtos.user.DeleteImageUserDto;
import com.example.secure_password_vault.dtos.user.ShowUserDto;
import com.example.secure_password_vault.entities.User;
import com.example.secure_password_vault.repositories.UserRepository;
import com.example.secure_password_vault.security.TokenService;
import com.example.secure_password_vault.services.ImageStorageService;
import com.example.secure_password_vault.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private UserService userService;
	
	@Mock
	private ImageStorageService imageStorageService;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private TokenService tokenService;
	
	@Mock
	private HttpServletRequest request;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	//Tests for getUserById function
	@Test
	void shouldReturnUserWhenUserIdIsValid() {
		Long userId  = 1L;
		User user = new User();
	    user.setId(userId);
	    user.setUsername("Gabriel");
	    user.setEmail("gabriel@example.com");
	    user.setPassword("encodedPassword");
	    user.setImageUser("image.png");
	    
	    when(request.getAttribute("userId")).thenReturn(userId);
	    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
	    
	    ShowUserDto result = userService.getUserById(request);
	    
	    assertEquals(userId, user.getId());
	    assertEquals("Gabriel", result.username());
	    assertEquals("gabriel@example.com", result.email());
	    assertEquals("encodedPassword", result.password());
	    assertEquals("image.png", result.imageUser());
	}
	
	@Test
	void shouldThrowExceptionWhenUserIdIsMissing() {
	    when(request.getAttribute("userId")).thenReturn(null);
	    
	    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getUserById(request));
	    
	    assertEquals("User not found", exception.getMessage());
	
	}
	
	@Test
	void shouldThrowExceptionWhenUserDoesNotExist() {
		Long userId = 1L;
		when(request.getAttribute("userId")).thenReturn(userId);
		when(userRepository.findById(userId)).thenReturn(Optional.empty());
		
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getUserById(request));
		assertEquals("User not found", exception.getMessage());
	}
	
	//Tests for deleteImageUser function
	@Test 
	void shouldDeleteUserImageWithSuccess() {
		Long userId = 1L;
	    User user = new User();
	    user.setId(userId);
	   
	    user.setImageUser("image.png");
	    
	    when(request.getAttribute("userId")).thenReturn(userId);
	    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
	    
	    DeleteImageUserDto response = userService.deleteImageUser(request);
	    
	    assertEquals(userId, user.getId());
	    assertEquals(null, user.getImageUser());
	    assertEquals(null, response.imageUser());
	}
	
	@Test
	void shouldThrowExceptionWhenUserIsNotFoundOnDeleteImage() {
		when(request.getAttribute("userId")).thenReturn(null);
		
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.deleteImageUser(request));
		    
		assertEquals("User not found", exception.getMessage());
	}
	
	@Test
	void shouldThrowExceptionWhenImageIsNotFound() {
		Long userId = 1L;
		when(request.getAttribute("userId")).thenReturn(userId);
		when(userRepository.findById(userId)).thenReturn(Optional.empty());
		
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.deleteImageUser(request));
		assertEquals("User not found", exception.getMessage());
	}
	
	@Test
	void shouldThrowRuntimeExceptionWhenImageDeletionFails() {
		 Long userId = 1L;
		    User user = new User();
		    user.setId(userId);
		    user.setImageUser("image.png");

		    when(request.getAttribute("userId")).thenReturn(userId);
		    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		    // Simula erro ao deletar imagem
		    doThrow(new RuntimeException("Erro")).when(imageStorageService).deleteImage("image.png");

		    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
		        userService.deleteImageUser(request);
		    });

		    assertEquals("Erro ao processar a imagem", exception.getMessage());
		    assertEquals("image.png", user.getImageUser());
	}
}



