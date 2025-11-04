package com.be08.smart_notes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "quiz")
public class Quiz {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

//	@Column(nullable = false, name = "user_id")
//	private Integer userId;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

	@Column(nullable = false)
	private String title;

    // Relationship: quiz - question
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

    // Relationship: quiz - quiz set
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "quiz_set_id", referencedColumnName = "id")
    private QuizSet quizSet;

    @Column(nullable = false, name = "source_document_id")
    private Integer sourceDocumentId;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
