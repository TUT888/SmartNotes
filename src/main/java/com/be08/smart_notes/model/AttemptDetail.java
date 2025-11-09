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
@Table(name = "attempt_detail")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttemptDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    // Relationship: attempt detail - attempt
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "attempt_id", referencedColumnName = "id")
    Attempt attempt;

    // Relationship: attempt detail - question (Unidirectional)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    Question question;

    @Column(name = "user_answer")
    Character userAnswer;

    @Column(name = "is_correct")
    Boolean isCorrect;
}
