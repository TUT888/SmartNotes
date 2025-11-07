package com.be08.smart_notes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "question")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Question {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@Column(nullable = false, name = "question_text")
	String questionText;

	@Column(nullable = false, name = "option_a")
	String optionA;

	@Column(nullable = false, name = "option_b")
	String optionB;

	@Column(nullable = false, name = "option_c")
	String optionC;

	@Column(nullable = false, name = "option_d")
	String optionD;

	@Column(nullable = false, name = "correct_answer")
	Character correctAnswer;

    // Relationship
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    Quiz quiz;
}
