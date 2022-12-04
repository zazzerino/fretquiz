package com.kdp.fretquiz.user;

import org.springframework.data.annotation.Id;

public record User(@Id String sessionId,
                   String name) {

    public static final String DEFAULT_NAME = "anon";
}
