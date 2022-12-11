package com.kdp.fretquiz.game.model;

import com.kdp.fretquiz.user.User;

public record Player(Long id,
                     String name,
                     int score) {

    public Player(Long id, String name) {
        this(id, name, 0);
    }

    public static Player from(User user) {
        return new Player(user.id(), user.name());
    }

    public Player incrementScore() {
        return new Player(id, name, score + 1);
    }
}
