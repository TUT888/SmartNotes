package com.be08.smart_notes.model;

import java.time.LocalDateTime;

import com.be08.smart_notes.enums.DocumentType;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "document")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Document {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private Integer userId;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private DocumentType type;

	@Column(nullable = false, name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	// Information for note document
	@Column(columnDefinition = "TEXT")
	private String content;

	// Information for pdf document
	@Column(name = "file_url")
	private String fileUrl;

	@Column(name = "file_size")
	private Integer fileSize;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
