package com.be08.smart_notes.mapper;

import com.be08.smart_notes.dto.QuizDTO;
import com.be08.smart_notes.dto.request.QuizSetUpsertRequest;
import com.be08.smart_notes.dto.response.QuizResponse;
import com.be08.smart_notes.dto.response.QuizSetResponse;
import com.be08.smart_notes.model.Question;
import com.be08.smart_notes.model.Quiz;
import com.be08.smart_notes.model.QuizSet;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuizMapper {
    // For update requests
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "originType", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "quizzes", ignore = true)
    void updateQuizSet(@MappingTarget QuizSet quizSet, QuizSetUpsertRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "sourceDocumentId", source = "sourceDocumentId")
    @Mapping(target = "quizSet", ignore = true)
    @Mapping(target = "questions", ignore = true)
    void updateQuiz(@MappingTarget Quiz quiz, QuizDTO request);

    // QuizSet entity <--> QuizSetResponse dto
    QuizSetResponse toQuizSetResponse(QuizSet quizSet);
    List<QuizSetResponse> toQuizSetResponseList(List<QuizSet> quizSet);

    // Quiz Entity <--> QuizResponse dto
    @Mapping(target = "quizSetId", source = "entity.quizSet.id")
    QuizResponse toQuizResponse(Quiz entity);
    Quiz toQuiz(QuizResponse dto);

    // Quiz Entity <--> QuizDTO
    Quiz toQuiz(QuizDTO dto);
    List<Quiz> toQuizList(List<QuizDTO> dtoList);

    // Question Entity <--> QuizDTO.Question dto
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "optionA", expression = "java(dto.getOptions()[0])")
    @Mapping(target = "optionB", expression = "java(dto.getOptions()[1])")
    @Mapping(target = "optionC", expression = "java(dto.getOptions()[2])")
    @Mapping(target = "optionD", expression = "java(dto.getOptions()[3])")
    @Mapping(target = "correctAnswer", expression = "java(indexToLetter(dto.getCorrectIndex()))")
    Question toQuestionEntity(QuizDTO.Question dto);

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
