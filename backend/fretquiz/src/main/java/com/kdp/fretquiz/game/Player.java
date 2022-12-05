package com.kdp.fretquiz.game;

public record Player(Long id,
                     String name,
                     int score) {

    public Player(Long id, String name) {
        this(id, name, 0);
    }
}
