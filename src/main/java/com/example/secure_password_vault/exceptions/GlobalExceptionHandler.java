package com.example.secure_password_vault.exceptions;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleException(ConstraintViolationException ex) {
		Map<String, Object> response = Map.of(
				"timestamp", LocalDateTime.now(),
				"status", HttpStatus.BAD_REQUEST.value(),
				"error", "Validation failed",
				"violations", ex.getConstraintViolations().stream()
					.collect(Collectors.toMap(
						violation -> violation.getPropertyPath().toString(),
						violation -> violation.getMessage()
					))
			);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<Object> handleException(NoSuchElementException ex) {
		Map<String, Object> response = Map.of(
				"timestamp", LocalDateTime.now(),
				"status", HttpStatus.NOT_FOUND.value(),
				"error", "Resource not found",
				"message", ex.getMessage()
		);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);		
	}
	
	@ExceptionHandler(EmptyListException.class)
	public ResponseEntity<Object> handleEmptyException(EmptyListException ex) {
		Map<String, Object> response = Map.of(
				"timestamp", LocalDateTime.now(),
				"status", HttpStatus.NOT_FOUND.value(),
				"error", "no datas found",
				"message", ex.getMessage()
		);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
}