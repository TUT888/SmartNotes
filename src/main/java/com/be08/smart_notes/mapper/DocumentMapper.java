package com.be08.smart_notes.mapper;

import com.be08.smart_notes.dto.response.NoteResponse;
import com.be08.smart_notes.model.Document;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    NoteResponse toNoteResponse(Document note);
}