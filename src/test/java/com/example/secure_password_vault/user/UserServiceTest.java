package com.example.secure_password_vault.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.secure_password_vault.dtos.user.DeleteImageUserDto;
import com.example.secure_password_vault.dtos.user.ShowUserDto;
import com.example.secure_password_vault.dtos.user.UpdateDatasUserDto;
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
	@DisplayName("Should return user datas when user id is valid")
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
	    assertEquals("image.png", result.imageUser());
	}
	
	@Test
	@DisplayName("SHould return an error message if user id is not valid")
	void shouldThrowExceptionWhenUserIdIsMissing() {
	    when(request.getAttribute("userId")).thenReturn(null);
	    
	    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getUserById(request));
	    
	    assertEquals("User not found", exception.getMessage());
	
	}
	
	@Test
	@DisplayName("Should return an error message if user does not exists")
	void shouldThrowExceptionWhenUserDoesNotExist() {
		Long userId = 1L;
		when(request.getAttribute("userId")).thenReturn(userId);
		when(userRepository.findById(userId)).thenReturn(Optional.empty());
		
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getUserById(request));
		assertEquals("User not found", exception.getMessage());
	}
	
	//Tests for deleteImageUser function
	@Test 
	@DisplayName("Should delete user imahe with success")
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
	@DisplayName("Should return an error message if user is is invalid")
	void shouldThrowExceptionWhenUserIsNotFoundOnDeleteImage() {
		when(request.getAttribute("userId")).thenReturn(null);
		
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.deleteImageUser(request));
		    
		assertEquals("User not found", exception.getMessage());
	}
	
	@Test
	@DisplayName("SHould return an error if image does not exists")
	void shouldThrowExceptionWhenImageIsNotFound() {
		Long userId = 1L;
		when(request.getAttribute("userId")).thenReturn(userId);
		when(userRepository.findById(userId)).thenReturn(Optional.empty());
		
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.deleteImageUser(request));
		assertEquals("User not found", exception.getMessage());
	}
	
	@Test
	@DisplayName("Should return an error messgae after got error to try to delete a message")
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
	
	//Tests for updateDataUser function
	@Test
	@DisplayName("Should update datas user with success")
	void shouldUpdateUserDatasWithSuccess() {
		Long userId = 1L;
	    User user = new User();
	    user.setId(userId);
	    user.setUsername("Gabriel");
	    user.setEmail("gabriel@gmail.com");
	    user.setPassword("ga78$A90");
	    user.setImageUser("image.png");
	    
	    //DTO com os novos dados
	    UpdateDatasUserDto updateDto = new UpdateDatasUserDto(
    		"Jessica", "jessica@gmail.com", null, "j3R82@63A");
	    
	    when(request.getAttribute("userId")).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(updateDto.email())).thenReturn(user); // mesmo usuário
        when(passwordEncoder.encode(updateDto.password())).thenReturn("encrypted password");
        when(tokenService.generateToken(user)).thenReturn("tokenNovo");
	    
        ResponseEntity<Map<String, Object>> response = userService.updateDatasUser(request, updateDto);
        Map<String, Object> responseBody = response.getBody();
    	ShowUserDto userDto = (ShowUserDto) responseBody.get("user");
        
        assertEquals("Jessica", user.getUsername());
        assertEquals("jessica@gmail.com", user.getEmail());
        assertEquals("encrypted password", user.getPassword());
        
        assertEquals("tokenNovo", responseBody.get("token"));
        assertEquals("Jessica", userDto.username());
    	assertEquals("jessica@gmail.com", userDto.email());
	}
	
	@Test
	@DisplayName("Should update user without changing password")
	void shouldUpdateUserWithoutChangingPassword() {
	    Long userId = 1L;
	    User user = new User();
	    user.setId(userId);
	    user.setUsername("Gabriel");
	    user.setEmail("gabriel@gmail.com");
	    user.setPassword("ga78$A90");
	    user.setImageUser("image.png");

	    UpdateDatasUserDto updateDto = new UpdateDatasUserDto("Jessica", "jessica@gmail.com", null, null);

	    when(request.getAttribute("userId")).thenReturn(userId);
	    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
	    when(userRepository.findByEmail(updateDto.email())).thenReturn(user);
	    when(tokenService.generateToken(user)).thenReturn("tokenNovo");

	    ResponseEntity<Map<String, Object>> response = userService.updateDatasUser(request, updateDto);

	    assertEquals("Jessica", user.getUsername());
	    assertEquals("jessica@gmail.com", user.getEmail());
	    assertEquals("ga78$A90", user.getPassword());
	    assertEquals("tokenNovo", response.getBody().get("token"));
	}
		
	@Test
	@DisplayName("Should throw error message when user not found to update")
	void shouldThrowWhenUserNotFound() {
	  Long userId = 99L;
	  UpdateDatasUserDto updateDto = new UpdateDatasUserDto("Jessica", "jessica@gmail.com", null, null);
	  
	  when(request.getAttribute("userId")).thenReturn(userId);
	    when(userRepository.findById(userId)).thenReturn(Optional.empty());

	    NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
	        userService.updateDatasUser(request, updateDto);
	    });

	    assertEquals("User not found", exception.getMessage());
	}
	
	@Test
	@DisplayName("Should throw error message when user wmail is already used by other another user")
	void shouldThrowWhenEmailIsAlreadyUsed() {
		Long userId = 1L;
		User user = new User();
	    user.setId(userId);
	    user.setUsername("Gabriel");
	    user.setEmail("gabriel@example.com");
	    user.setPassword("encodedPassword");
	    user.setImageUser("image.png");
	    
	    User anotherUser = new User();
	    anotherUser.setId(2L);
		 
		UpdateDatasUserDto updateDto = new UpdateDatasUserDto("Jessica", "jessica@gmail.com", null, null);
		
		when(request.getAttribute("userId")).thenReturn(userId);
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(userRepository.findByEmail(updateDto.email())).thenReturn(anotherUser);
		
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
	        userService.updateDatasUser(request, updateDto);
	    });
		
		assertEquals("Não foi possível atualizar os dados do usuário", exception.getMessage());
	}
	
	@Test
	@DisplayName("Should throw exception when image processing fails")
	void shouldThrowWhenImageProcessingFails() throws IOException {
	    Long userId = 1L;
	    User user = new User();
	    user.setId(userId);
	    user.setEmail("gabriel@gmail.com");
	    user.setUsername("Gabriel");

	    MockMultipartFile image = new MockMultipartFile("image", "fail.png", "image/png", "img".getBytes());

	    UpdateDatasUserDto updateDto = new UpdateDatasUserDto("Gabriel", "gabriel@gmail.com", image, null);

	    when(request.getAttribute("userId")).thenReturn(userId);
	    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
	    when(userRepository.findByEmail(updateDto.email())).thenReturn(user);
	    doThrow(new RuntimeException("Erro interno")).when(imageStorageService).saveImage(image);

	    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
	        userService.updateDatasUser(request, updateDto);
	    });

	    assertEquals("Erro ao processar a imagem", exception.getMessage());
	}

}



