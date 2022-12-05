package com.kdp.fretquiz.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

public record User(@Id Long id,
                   @JsonIgnore String sessionId,
                   String name) {

    public static final String DEFAULT_NAME = "anon";

    public static User of(String sessionId) {
        return new User(null, sessionId, DEFAULT_NAME);
    }

    public User withName(String name) {
        return new User(id, sessionId, name);
    }
}
