package com.example.secure_password_vault.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.secure_password_vault.dtos.user.ShowUserDto;
import com.example.secure_password_vault.entities.User;
import com.example.secure_password_vault.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

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

}
