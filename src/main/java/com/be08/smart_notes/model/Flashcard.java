package com.be08.smart_notes.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "flashcard")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Flashcard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flashcard_set_id", nullable = false)
    FlashcardSet flashcardSet;

    @Column(nullable = false, name = "front_content", columnDefinition = "TEXT")
    String frontContent;

    @Column(nullable = false, name = "back_content", columnDefinition = "TEXT")
    String backContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_document_id", nullable = true)
    Document sourceDocument;

    @Column(nullable = false, name = "created_at")
    LocalDateTime createdAt;

    @Column(nullable = true, name = "updated_at")
    LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = LocalDateTime.now();
    }
}
