package com.be08.smart_notes.mapper;

import com.be08.smart_notes.dto.QuizUpsertDTO;
import com.be08.smart_notes.dto.response.QuizResponse;
import com.be08.smart_notes.model.Quiz;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = QuestionMapper.class)
public interface QuizMapper {
    // For update requests
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "title", source = "title")
    void updateQuiz(@MappingTarget Quiz quiz, QuizUpsertDTO request);

    // Quiz Entity <--> QuizResponse dto
    @Mapping(target = "quizSetId", source = "entity.quizSet.id")
    QuizResponse toQuizResponse(Quiz entity);
    Quiz toQuiz(QuizResponse dto);

    // Quiz Entity <--> QuizUpsertDTO
    Quiz toQuiz(QuizUpsertDTO dto);
    List<Quiz> toQuizList(List<QuizUpsertDTO> dtoList);
}
