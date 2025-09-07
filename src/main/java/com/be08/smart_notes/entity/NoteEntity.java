package com.be08.smart_notes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notes")
public class NoteEntity {
	@Id
	private Long id;

	@Column(nullable = false)
	private String content;

	public NoteEntity() {}
	
	public NoteEntity(Long id, String content) {
		super();
		this.id = id;
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
