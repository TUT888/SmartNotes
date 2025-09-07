package com.be08.smart_notes.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String name;

	@Column(nullable = true)
	private String avatarUrl;

	@Column(nullable = false, name = "created_at")
	private LocalDateTime createdAt;

	@Column(nullable = true, name = "updated_at")
	private LocalDateTime updatedAt;

	public UserEntity() {
	}

	public UserEntity(Long id, String email, String password, String name, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
		this.createdAt = createdAt;
	}
}
