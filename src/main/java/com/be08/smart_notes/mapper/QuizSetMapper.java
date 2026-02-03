package com.be08.smart_notes.mapper;

import com.be08.smart_notes.dto.request.QuizSetUpsertRequest;
import com.be08.smart_notes.dto.response.QuizSetResponse;
import com.be08.smart_notes.model.QuizSet;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = QuizMapper.class)
public interface QuizSetMapper {
    // For update request
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "title", source = "title")
    void updateQuizSet(@MappingTarget QuizSet quizSet, QuizSetUpsertRequest request);

    // QuizSet entity <--> QuizSetResponse dto
    @Named("basic")
    @Mapping(target = "quizzes", ignore = true)
    QuizSetResponse toQuizSetResponse(QuizSet quizSet);

    @IterableMapping(qualifiedByName = "basic")
    List<QuizSetResponse> toQuizSetResponseList(List<QuizSet> quizSet);

    // QuizSet with Quizzes
    @Named("detail")
    QuizSetResponse toQuizSetResponseWithQuizzes(QuizSet quizSet);
}
