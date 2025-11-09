package com.be08.smart_notes.mapper;

import com.be08.smart_notes.dto.request.QuizSetUpsertRequest;
import com.be08.smart_notes.dto.response.QuizSetResponse;
import com.be08.smart_notes.model.QuizSet;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = QuizMapper.class)
public interface QuizSetMapper {
    // For update request
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "title", source = "title")
    void updateQuizSet(@MappingTarget QuizSet quizSet, QuizSetUpsertRequest request);

    // QuizSet entity <--> QuizSetResponse dto
    QuizSetResponse toQuizSetResponse(QuizSet quizSet);
    List<QuizSetResponse> toQuizSetResponseList(List<QuizSet> quizSet);
}
