package com.be08.smart_notes.mapper;

import com.be08.smart_notes.dto.QuizUpsertDTO;
import com.be08.smart_notes.model.Question;
import com.be08.smart_notes.model.Quiz;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    // For update requests
    // Questions are not allowed to be updated, unless delete entire quiz

    // Question Entity <--> QuizUpsertDTO.Question dto
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "optionA", expression = "java(dto.getOptions()[0])")
    @Mapping(target = "optionB", expression = "java(dto.getOptions()[1])")
    @Mapping(target = "optionC", expression = "java(dto.getOptions()[2])")
    @Mapping(target = "optionD", expression = "java(dto.getOptions()[3])")
    @Mapping(target = "correctAnswer", expression = "java(indexToLetter(dto.getCorrectIndex()))")
    Question toQuestionEntity(QuizUpsertDTO.Question dto);

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
