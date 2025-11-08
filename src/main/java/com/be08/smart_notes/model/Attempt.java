package com.be08.smart_notes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Entity
@Table(name = "attempt")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Attempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false)
    LocalDateTime attemptAt;

    @Column(nullable = false, name = "total_question")
    Integer totalQuestion;

    @Column
    Integer score;

    // Relationship: attempt - quiz
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    Quiz quiz;

    // Relationship: attempt - attempt detail
    @OneToMany(mappedBy = "attempt", cascade = CascadeType.ALL, orphanRemoval = true)
    List<AttemptDetail> attemptDetails;

    @PrePersist
    protected void onCreate() {
        this.attemptAt = LocalDateTime.now();
    }
}
