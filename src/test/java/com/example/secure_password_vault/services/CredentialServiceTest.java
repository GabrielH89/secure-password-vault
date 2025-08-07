package com.example.secure_password_vault.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.springframework.data.domain.Pageable;
import java.util.List;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.example.secure_password_vault.dtos.credential.CreateCredentialDto;
import com.example.secure_password_vault.dtos.credential.ShowCredentialDto;
import com.example.secure_password_vault.entities.Credential;
import com.example.secure_password_vault.entities.User;
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
	
	//Tests for getCredentialById
	@Test 
	@DisplayName("Should return a credential by its id with success")
	void shouldReturnCredentialById() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getAttribute("userId")).thenReturn(1L);
		
		Credential credential = new Credential("Password 1", "gat279@L");
		credential.setId_password(10L);
		
		 var mockUser = mock(com.example.secure_password_vault.entities.User.class);
		 when(mockUser.getId()).thenReturn(1L);
		 credential.setUser(mockUser);

		 when(credentialRepository.findById(10L)).thenReturn(java.util.Optional.of(credential));

		 ShowCredentialDto result = credentialService.getCredentialById(request, 10L);		
		assertEquals(10L, result.id_password());
		assertEquals("Password 1", result.systemName());
		assertEquals("gat279@L", result.passwordBody());
	}
	
	@Test
	@DisplayName("Should throw NoSuchElementException when credential is not found")
	void shouldThrowWhenCredentialNotFound() {
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    when(request.getAttribute("userId")).thenReturn(1L);

	    when(credentialRepository.findById(99L)).thenReturn(java.util.Optional.empty());

	    assertThrows(NoSuchElementException.class, () -> {
	        credentialService.getCredentialById(request, 99L);
	    });
	}

	@Test
	@DisplayName("Should throw SecurityException when user is not the owner of the credential")
	void shouldThrowSecurityExceptionWhenUserIsNotOwner() {
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    when(request.getAttribute("userId")).thenReturn(1L); // usuário logado é o 1

	    Credential credential = new Credential("Facebook", "senha456");
	    credential.setId_password(20L);

	    var otherUser = mock(com.example.secure_password_vault.entities.User.class);
	    when(otherUser.getId()).thenReturn(2L); // outro usuário é o dono
	    credential.setUser(otherUser);

	    when(credentialRepository.findById(20L)).thenReturn(java.util.Optional.of(credential));

	    assertThrows(SecurityException.class, () -> {
	        credentialService.getCredentialById(request, 20L);
	    });
	}

	//Tests for createCredential
	@Test
	@DisplayName("Should create a credential with success")
	void shouldCreateCredential() {
		 HttpServletRequest request = mock(HttpServletRequest.class);
		 when(request.getAttribute("userId")).thenReturn(1L);
		 
		CreateCredentialDto credentialDto = new CreateCredentialDto("New password", "ga90@P");
		
		User user = new User();
		user.setId(1L);
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		
		Credential savedCredential = new Credential("New password", "ga90@P");
		
		savedCredential.setId_password(100L);
	    savedCredential.setUser(user);

	    when(credentialRepository.save(any(Credential.class))).thenReturn(savedCredential);

	    ShowCredentialDto result = credentialService.createCredential(credentialDto, request);
	    
	    assertEquals(100L, result.id_password());
	    assertEquals("New password", result.systemName());
	    assertEquals("ga90@P", result.passwordBody());
	}
	
	@Test
	@DisplayName("Should throw RuntimeException when user is not found")
	void shouldThrowWhenUserNotFound() {
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    when(request.getAttribute("userId")).thenReturn(1L);

	    CreateCredentialDto dto = new CreateCredentialDto("GitHub", "senha123");

	    when(userRepository.findById(1L)).thenReturn(Optional.empty());

	    assertThrows(RuntimeException.class, () -> {
	        credentialService.createCredential(dto, request);
	    });
	}
	
	@Test
	@DisplayName("Should throw NullPointerException when userId is missing in request")
	void shouldThrowWhenUserIdIsMissing() {
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    when(request.getAttribute("userId")).thenReturn(null);

	    CreateCredentialDto dto = new CreateCredentialDto("GitHub", "senha123");

	    assertThrows(NullPointerException.class, () -> {
	        credentialService.createCredential(dto, request);
	    });
	}
	
	@Test
	@DisplayName("Should throw exception when save fails")
	void shouldThrowWhenSaveFails() {
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    when(request.getAttribute("userId")).thenReturn(1L);

	    CreateCredentialDto dto = new CreateCredentialDto("GitHub", "senha123");

	    User user = new User();
	    user.setId(1L);
	    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

	    when(credentialRepository.save(any(Credential.class)))
	        .thenThrow(new RuntimeException("Database error"));

	    assertThrows(RuntimeException.class, () -> {
	        credentialService.createCredential(dto, request);
	    });
	}

}





