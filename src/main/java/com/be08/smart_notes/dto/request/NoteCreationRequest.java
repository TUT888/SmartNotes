package com.be08.smart_notes.dto.request;

import com.be08.smart_notes.enums.DocumentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteCreationRequest {
	private int userId;
	private DocumentType type;
	private String title;
	private String content;
}
