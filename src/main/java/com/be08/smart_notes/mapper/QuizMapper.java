package com.be08.smart_notes.mapper;

import com.be08.smart_notes.dto.ai.AIQuizResponse;
import com.be08.smart_notes.dto.ai.QuizResponse;
import com.be08.smart_notes.model.Question;
import com.be08.smart_notes.model.Quiz;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QuizMapper {
    // Quiz & Question entity <--> QuizResponse DTO
    QuizResponse fromQuizToQuizResponse(Quiz entity);
    Quiz fromQuizToQuizResponse(QuizResponse dto);

    // AIQuizResponse DTO --> Quiz & Question entity
    Quiz fromAIQuizResponseToQuiz(AIQuizResponse dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sourceDocumentId", ignore = true)
    @Mapping(target = "optionA", expression = "java(dto.getOptions()[0])")
    @Mapping(target = "optionB", expression = "java(dto.getOptions()[1])")
    @Mapping(target = "optionC", expression = "java(dto.getOptions()[2])")
    @Mapping(target = "optionD", expression = "java(dto.getOptions()[3])")
    @Mapping(target = "correctAnswer", expression = "java(indexToLetter(dto.getCorrectIndex()))")
    Question fromAIQuizResponseQuestionToQuestion(AIQuizResponse.Question dto);

    default Character indexToLetter(Integer index) {
        return (char) ('A' + index);
    }
}
