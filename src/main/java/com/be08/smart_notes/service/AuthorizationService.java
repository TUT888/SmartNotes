package com.be08.smart_notes.service;

import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class AuthorizationService {
    public int getCurrentUserId(){
        var context = SecurityContextHolder.getContext();
        return Integer.parseInt(context.getAuthentication().getName());
    }

    public void validateOwnership(int resourceOwnerId){
        int currentUserId = getCurrentUserId();
        if(resourceOwnerId != currentUserId){
            log.error("User does not own the resource");
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
    }
}
