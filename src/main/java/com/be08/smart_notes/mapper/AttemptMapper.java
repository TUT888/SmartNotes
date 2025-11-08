package com.be08.smart_notes.mapper;

import com.be08.smart_notes.dto.response.AttemptResponse;
import com.be08.smart_notes.model.Attempt;
import com.be08.smart_notes.model.AttemptDetail;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AttemptMapper {
    @Mapping(target = "quizId", source = "entity.quiz.id")
    AttemptResponse toAttemptResponse(Attempt entity);
    List<AttemptResponse> toAttemptResponseList(List<Attempt> entity);

    @Mapping(target = "questionText", source = "entity.question.questionText")
    @Mapping(target = "optionA", source = "entity.question.optionA")
    @Mapping(target = "optionB", source = "entity.question.optionB")
    @Mapping(target = "optionC", source = "entity.question.optionC")
    @Mapping(target = "optionD", source = "entity.question.optionD")
    @Mapping(target = "correctAnswer", source = "entity.question.correctAnswer")
    AttemptResponse.Detail toAttemptResponseDetail(AttemptDetail entity);
}
