package com.example.secure_password_vault.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.secure_password_vault.entities.Credential;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {
	List<Credential> findByUserId(long userId);
	
	List<Credential> findByUserIdOrderByPosition(Long userId);
	
	Page<Credential> findByUserIdOrderByPosition(Long userId, Pageable pageable);
}
