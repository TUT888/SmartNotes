package com.be08.smart_notes.model;

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
@Table(name = "questions")
public class Question {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false, name = "quiz_id")
	private int quizId;

	@Column(nullable = false, name = "question_text")
	private String questionText;

	@Column(nullable = false, name = "option_a")
	private String optionA;

	@Column(nullable = false, name = "option_b")
	private String optionB;

	@Column(nullable = false, name = "option_c")
	private String optionC;

	@Column(nullable = false, name = "option_d")
	private String optionD;

	@Column(nullable = false, name = "correct_answer")
	private char correctAnswer;

	@Column(nullable = false, name = "source_document_id")
	private int sourceDocumentId;

	@Column(nullable = false, name = "created_at")
	private LocalDateTime createdAt;
}
