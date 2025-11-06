package com.be08.smart_notes.mapper;

import com.be08.smart_notes.dto.QuizQuestion;
import com.be08.smart_notes.dto.response.QuizResponse;
import com.be08.smart_notes.dto.response.QuizSetResponse;
import com.be08.smart_notes.model.Question;
import com.be08.smart_notes.model.Quiz;
import com.be08.smart_notes.model.QuizSet;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuizMapper {
    // QuizSet entity <--> QuizSetResponse dto
    QuizSetResponse toQuizSetResponse(QuizSet quizSet);
    List<QuizSetResponse> toQuizSetResponseList(List<QuizSet> quizSet);

    // Quiz Entity <--> QuizResponse dto
    QuizResponse toQuizResponse(Quiz entity);
    Quiz toQuiz(QuizResponse dto);

    // Quiz Entity <--> QuizQuestion
    Quiz toQuiz(QuizQuestion dto);
    List<Quiz> toQuizList(List<QuizQuestion> dtoList);

    // Question Entity <--> QuizQuestion.Question dto
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "optionA", expression = "java(dto.getOptions()[0])")
    @Mapping(target = "optionB", expression = "java(dto.getOptions()[1])")
    @Mapping(target = "optionC", expression = "java(dto.getOptions()[2])")
    @Mapping(target = "optionD", expression = "java(dto.getOptions()[3])")
    @Mapping(target = "correctAnswer", expression = "java(indexToLetter(dto.getCorrectIndex()))")
    Question toQuestionEntity(QuizQuestion.Question dto);

    default Character indexToLetter(Integer index) {
        return (char) ('A' + index);
    }

    @AfterMapping
    default void linkQuizToQuestions(@MappingTarget Quiz quiz) {
        List<Question> questions = quiz.getQuestions();
        for (Question question : questions) {
            question.setQuiz(quiz);
        }
    }
}
