package com.be08.smart_notes.mapper;

import com.be08.smart_notes.dto.request.UserCreationRequest;
import com.be08.smart_notes.dto.response.UserResponse;
import com.be08.smart_notes.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "avatarUrl", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);
}
