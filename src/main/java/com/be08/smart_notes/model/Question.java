package com.be08.smart_notes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "question")
public class Question {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

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
	private Character correctAnswer;

    // Relationship
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    private Quiz quiz;
}
