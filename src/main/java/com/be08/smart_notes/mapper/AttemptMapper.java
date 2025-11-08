package com.be08.smart_notes.mapper;

import com.be08.smart_notes.dto.response.AttemptResponse;
import com.be08.smart_notes.model.Attempt;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = AttemptDetailMapper.class)
public interface AttemptMapper {
    @Mapping(target = "quizId", source = "entity.quiz.id")
    AttemptResponse toAttemptResponse(Attempt entity);
    List<AttemptResponse> toAttemptResponseList(List<Attempt> entity);
}
