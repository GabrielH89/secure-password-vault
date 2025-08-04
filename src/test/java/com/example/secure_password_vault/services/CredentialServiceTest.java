package com.example.secure_password_vault.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.springframework.data.domain.Pageable;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.example.secure_password_vault.dtos.credential.ShowCredentialDto;
import com.example.secure_password_vault.entities.Credential;
import com.example.secure_password_vault.exceptions.EmptyListException;
import com.example.secure_password_vault.repositories.CredentialRepository;
import com.example.secure_password_vault.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
public class CredentialServiceTest {
	
	@Mock
	private CredentialRepository credentialRepository;
	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	CredentialService credentialService;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	//Tests for getPaginatedCredentials
	@Test
	@DisplayName("Should return paginated credentials with success") 
	void shouldReturnPaginatedCredentials() {
		 HttpServletRequest request = mock(HttpServletRequest.class);
		    when(request.getAttribute("userId")).thenReturn(1L);

		    Credential cred1 = new Credential("Gmail", "12345");
		    cred1.setId_password(1L);
		    Credential cred2 = new Credential("Facebook", "abcde");
		    cred2.setId_password(2L);

		    Page<Credential> page = new PageImpl<>(List.of(cred1, cred2), PageRequest.of(0, 12), 2);
		    when(credentialRepository.findByUserIdOrderByPosition(1L, PageRequest.of(0, 12))).thenReturn(page);

		    List<ShowCredentialDto> result = credentialService.getPaginatedCredentials(request, 0);

		    assertEquals(2, result.size());
		    assertEquals("Gmail", result.get(0).systemName());
		    assertEquals("Facebook", result.get(1).systemName());
	}
	
	@Test
	@DisplayName("shoud return throw EmptyListException when credentials are not found")
	void shouldThrowWhenCredentialsNotFound() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getAttribute("userId")).thenReturn(1L);
		
		Page<Credential> emptyPage = Page.empty();
		when(credentialRepository.findByUserIdOrderByPosition(eq(1L), any(Pageable.class)))
        .thenReturn(emptyPage);

		
		 assertThrows(EmptyListException.class, () -> {
		        credentialService.getPaginatedCredentials(request, 0);
		 });
	}
	
	@Test
	@DisplayName("Should throw NullPointerException when userId is not set in request")
	void shouldThrowWhenUserIdIsNull() {
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    when(request.getAttribute("userId")).thenReturn(null);

	    assertThrows(NullPointerException.class, () -> {
	        credentialService.getPaginatedCredentials(request, 0);
	    });
	}
	
	
}





