package com.be08.smart_notes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.be08.smart_notes.model.Document;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {
    List<Document> findAllByUserId(Integer userId);
	List<Document> findAllByIdIn(List<Integer> ids);

    @Query("SELECT d.content FROM Document d WHERE d.id IN :ids")
    List<String> findAllContentFromIdIn(@Param(value="ids") List<Integer> ids);
}
