package com.be08.smart_notes.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "documents")
public class DocumentEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, name = "user_id")
	private Long userId;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String type;

	@Column(nullable = false, name = "created_at")
	private LocalDateTime createdAt;
	
	@Column(nullable = true, name = "updated_at")
	private LocalDateTime updatedAt;

	public DocumentEntity() {}
	
	public DocumentEntity(Long id, Long userId, String title, String type, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.userId = userId;
		this.title = title;
		this.type = type;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
}
