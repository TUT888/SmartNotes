package com.be08.smart_notes.model;

import com.be08.smart_notes.enums.OriginType;
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
@Table(name = "flashcard_set")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlashcardSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User owner;

    @Column(nullable = false)
    String title;

    @OneToMany(mappedBy = "flashcardSet", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Flashcard> flashcards;

    @Column(nullable = false, name = "created_at")
    LocalDateTime createdAt;

    @Column(nullable = true, name = "updated_at")
    LocalDateTime updatedAt;

    @Column(nullable = false, name = "origin_type")
    @Enumerated(EnumType.STRING)
    OriginType originType = OriginType.USER;

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