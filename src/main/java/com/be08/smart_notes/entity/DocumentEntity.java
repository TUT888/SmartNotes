package com.be08.smart_notes.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
