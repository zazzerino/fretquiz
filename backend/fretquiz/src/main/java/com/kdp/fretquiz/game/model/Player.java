package com.kdp.fretquiz.game.model;

public record Player(Long id,
                     String name,
                     int score) {

    public Player(Long id, String name) {
        this(id, name, 0);
    }
}
