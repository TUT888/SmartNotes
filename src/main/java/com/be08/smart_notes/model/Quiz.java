package com.be08.smart_notes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "quiz")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Quiz {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

    @Column(nullable = false, name = "created_at")
    LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    LocalDateTime updatedAt;

	@Column(nullable = false)
	String title;

    @Column(name = "source_document_id")
    Integer sourceDocumentId;

    // Relationship: quiz - quiz set
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "quiz_set_id", referencedColumnName = "id")
    QuizSet quizSet;

    // Relationship: quiz - question
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Question> questions;

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
