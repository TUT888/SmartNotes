package com.be08.smart_notes.mapper;

import com.be08.smart_notes.dto.request.FlashcardSetCreationRequest;
import com.be08.smart_notes.dto.response.FlashcardSetResponse;
import com.be08.smart_notes.model.FlashcardSet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FlashcardSetMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "flashcards", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    FlashcardSet toFlashcardSet(FlashcardSetCreationRequest request);

    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "originType", source = "originType")
    FlashcardSetResponse toFlashcardSetResponse(FlashcardSet flashcardSet);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "flashcards", ignore = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFlashcardSet(@MappingTarget FlashcardSet flashcardSet, FlashcardSetCreationRequest request);
}
