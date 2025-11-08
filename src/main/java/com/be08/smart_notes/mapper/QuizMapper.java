package com.be08.smart_notes.mapper;

import com.be08.smart_notes.dto.QuizUpsertDTO;
import com.be08.smart_notes.dto.request.QuizSetUpsertRequest;
import com.be08.smart_notes.dto.response.QuizResponse;
import com.be08.smart_notes.dto.response.QuizSetResponse;
import com.be08.smart_notes.model.Question;
import com.be08.smart_notes.model.Quiz;
import com.be08.smart_notes.model.QuizSet;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = QuestionMapper.class)
public interface QuizMapper {
    // For update requests
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "sourceDocumentId", source = "sourceDocumentId")
    @Mapping(target = "quizSet", ignore = true)
    @Mapping(target = "questions", ignore = true)
    void updateQuiz(@MappingTarget Quiz quiz, QuizUpsertDTO request);

    // Quiz Entity <--> QuizResponse dto
    @Mapping(target = "quizSetId", source = "entity.quizSet.id")
    QuizResponse toQuizResponse(Quiz entity);
    Quiz toQuiz(QuizResponse dto);

    // Quiz Entity <--> QuizUpsertDTO
    Quiz toQuiz(QuizUpsertDTO dto);
    List<Quiz> toQuizList(List<QuizUpsertDTO> dtoList);
}
