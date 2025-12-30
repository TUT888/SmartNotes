package com.be08.smart_notes.model;

import com.be08.smart_notes.enums.OriginType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "quiz_set")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false, name = "user_id")
    Integer userId;

    @Column(nullable = false, name = "title")
    String title;

    @Column(nullable = false, name = "origin_type")
    @Enumerated(EnumType.STRING)
    OriginType originType;

    @Column(nullable = false, name = "created_at")
    LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    LocalDateTime updatedAt;

    // Relationship
    @OneToMany(mappedBy = "quizSet", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Quiz> quizzes;

    public void addQuiz(Quiz quiz){
        if (this.quizzes == null) {
            this.quizzes = new ArrayList<>();
        }
        quizzes.add(quiz);
        quiz.setQuizSet(this);
    }

    public void addQuizzes(List<Quiz> newQuizzes){
        if (this.quizzes == null) {
            this.quizzes = new ArrayList<>();
        }
        newQuizzes.forEach(quiz -> quiz.setQuizSet(this));
        quizzes.addAll(newQuizzes);
    }

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
