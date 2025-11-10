package com.be08.smart_notes.mapper;

import com.be08.smart_notes.dto.response.NoteResponse;
import com.be08.smart_notes.model.Document;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    NoteResponse toNoteResponse(Document note);
    List<NoteResponse> toNoteResponseList(List<Document> noteList);
}
