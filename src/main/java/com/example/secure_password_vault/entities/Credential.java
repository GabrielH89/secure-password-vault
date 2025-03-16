package com.example.secure_password_vault.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_credential")
public class Credential {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id_password;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Column(nullable = false)
	@NotBlank(message = "System name cannot be empty")
	@Size(max = 200, message = "System name cannot be longer than 200 characters")
	private String systemName;
	
	@Column(nullable = false)
	@NotBlank(message = "Password body cannot be empty")
	@Size(max = 500, message = "Password body cannot be longer than 500 characters")
	private String passwordBody;
	
	@Column(nullable = false)
	private LocalDateTime createdAt;
	
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	public Credential() {
		
	}	
	
	public Credential(String systemName, String passwordBody) {
		this.systemName = systemName;
		this.passwordBody = passwordBody;
	}

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
	
	public long getId_password() {
		return id_password;
	}

	public void setId_password(long id_password) {
		this.id_password = id_password;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getPasswordBody() {
		return passwordBody;
	}

	public void setPasswordBody(String passwordBody) {
		this.passwordBody = passwordBody;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
