package com.example.secure_password_vault.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import com.example.secure_password_vault.controllers.AuthController;
import com.example.secure_password_vault.dtos.user.CreateUserDto;
import com.example.secure_password_vault.entities.User;
import com.example.secure_password_vault.repositories.UserRepository;
import com.example.secure_password_vault.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    @DisplayName("It should register an user with success")
    void shouldRegisterUserSuccessfully() throws Exception {
        // Entrada esperada
        var input = new CreateUserDto("User", "user@gmail.com", "");

        // Usuário esperado após criptografia e salvamento
        var expectedUser = new User("User", "user@gmail.com", "encryptedPassword");

        // Criando um mock do BindingResult
        BindingResult bindingResult = new MapBindingResult(new HashMap<>(), "createUserDto");

        // Simulação: Usuário não existe no banco
        when(userRepository.findByEmail(input.email())).thenReturn(null);

        // Simulação: Senha criptografada
        when(passwordEncoder.encode(input.password())).thenReturn("encryptedPassword");

        // Simulação: Salvamento do usuário
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);

        // Executando a função com BindingResult
        var savedUser = authController.register(input, bindingResult);

        // Verificações
        assertNotNull(savedUser);
        
        // Verificando se houve erros de validação
        assertEquals(0, bindingResult.getErrorCount());

        // Verifica se o método save foi chamado corretamente
        verify(userRepository, times(1)).save(any(User.class));
    }
   
}
