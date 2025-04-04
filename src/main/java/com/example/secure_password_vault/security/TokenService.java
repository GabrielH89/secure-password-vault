package com.example.secure_password_vault.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.secure_password_vault.entities.User;

@Service
public class TokenService {
	
	@Value("${api.security.token.secret}")
	private String secret;//Essa variável contém a variável de ambiente JWT lá do application.properties
	
	public String generateToken(User user) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			String token = JWT.create()
					.withIssuer("auth-api")
					.withSubject(user.getEmail())
					.withExpiresAt(generateExpirationDate())
					.sign(algorithm);
			return token;
		}catch(JWTCreationException e) {
			throw new RuntimeException("Error: ", e);
		}
	}
	
	public String validate(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			return JWT.require(algorithm)
					.withIssuer("auth-api")
					.build()
					.verify(token)
					.getSubject();
		}catch(JWTCreationException e) {
			throw new RuntimeException("Error: ", e);
		}
	}
	
	private Instant generateExpirationDate() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}
}


