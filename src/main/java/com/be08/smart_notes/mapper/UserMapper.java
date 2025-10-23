package com.be08.smart_notes.mapper;

import com.be08.smart_notes.dto.request.UserCreationRequest;
import com.be08.smart_notes.dto.response.UserResponse;
import com.be08.smart_notes.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);
}
