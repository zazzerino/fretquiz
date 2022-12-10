package com.kdp.fretquiz.game.model;

public record Player(Long id,
                     String name,
                     int score) {

    public Player(Long id, String name) {
        this(id, name, 0);
    }

    public Player incrementScore() {
        return new Player(id, name, score + 1);
    }
}
