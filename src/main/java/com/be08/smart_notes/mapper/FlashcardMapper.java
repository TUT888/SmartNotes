package com.be08.smart_notes.mapper;

import com.be08.smart_notes.dto.request.FlashcardCreationRequest;
import com.be08.smart_notes.dto.response.FlashcardResponse;
import com.be08.smart_notes.model.Flashcard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FlashcardMapper {
    @Mapping(target = "flashcardSet", ignore = true)
    @Mapping(target = "sourceDocument", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    Flashcard toFlashcard(FlashcardCreationRequest request);

    @Mapping(target = "flashcardSetId", source = "flashcardSet.id")
    @Mapping(target = "sourceDocumentId", source = "sourceDocument.id")
    FlashcardResponse toFlashcardResponse(Flashcard flashcard);

    @Mapping(target = "frontContent", source = "frontContent")
    @Mapping(target = "backContent", source = "backContent")
    @Mapping(target = "sourceDocument", ignore = true)
    @Mapping(target = "flashcardSet", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateFlashcard(@MappingTarget Flashcard flashcard, FlashcardCreationRequest request);
}