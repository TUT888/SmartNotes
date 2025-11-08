package com.be08.smart_notes.mapper;

import com.be08.smart_notes.dto.request.AttemptDetailUpdateRequest;
import com.be08.smart_notes.model.AttemptDetail;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AttemptDetailMapper {
    // For update requests
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "entity.userAnswer", source = "request.userAnswer")
    void updateAttemptDetail(@MappingTarget AttemptDetail entity, AttemptDetailUpdateRequest request);

    @AfterMapping
    default void updateAttemptResult(@MappingTarget AttemptDetail attemptDetail, AttemptDetailUpdateRequest request) {
        Character correct = attemptDetail.getQuestion().getCorrectAnswer();
        Character choice = request.getUserAnswer();
        attemptDetail.setIsCorrect(choice == correct);
    }
}
