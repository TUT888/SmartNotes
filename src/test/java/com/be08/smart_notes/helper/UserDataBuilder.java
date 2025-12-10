package com.be08.smart_notes.helper;

import com.be08.smart_notes.model.User;

import java.time.LocalDateTime;

public class UserDataBuilder {
    /**
     * Create a sample mock user object with given information.
     * This method should not be used with real database interaction
     * @param userId mock user ID
     * @return User.UserBuilder
     */
    public static User.UserBuilder createMockUser(int userId) {
        return User.builder().id(userId);
    }

    /**
     * Create a user object with all required fields to be saved in database.
     * Use the returned builder object to override any custom value (if needed),
     * then chain .build() to build the object
     * @param email unique email for new user
     * @return User.UserBuilder
     */
    public static User.UserBuilder createUser(String email) {
        return User.builder()
                .name("Test User").email(email)
                .password("123456").createdAt(LocalDateTime.now());
    }
}
