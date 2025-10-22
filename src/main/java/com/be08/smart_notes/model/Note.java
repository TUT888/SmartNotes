package com.be08.smart_notes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
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
@Table(name = "note")
public class Note {
	@Id
	private int id;

	@Column(nullable = false)
	private String content;
	
	// Relationship
	// Use document.id as this entity's id
	@JsonIgnore
	@OneToOne
	@MapsId 
	@JoinColumn(name = "id")
	private Document document;
}
