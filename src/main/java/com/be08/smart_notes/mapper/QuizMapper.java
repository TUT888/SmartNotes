package com.be08.smart_notes.mapper;

import com.be08.smart_notes.dto.ai.QuizQuestion;
import com.be08.smart_notes.model.Question;
import com.be08.smart_notes.model.Quiz;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QuizMapper {
    @Mapping(target = "title", source = "dto.topic")
    Quiz fromQuizResponseToQuiz(QuizQuestion dto);

    @Mapping(target = "questionText", source = "dto.question")
    Question fromQuizResponseQuestionToQuestion(QuizQuestion.Question dto);

//    @Mapping(target = "topic", source = "entity.title")
//    @Mapping(target = "questions", source = "entity.questions")
//    QuizResponse toQuizResponse(Quiz entity);
}
