package com.be08.smart_notes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.be08.smart_notes.model.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {
}
