package com.kdp.fretquiz.game;

public enum GameState {
    INIT, // the game has been created but not started
    PLAYING, // players are guessing
    ROUND_OVER, // all players have guessed
    GAME_OVER, // all rounds have been played
    NO_PLAYERS // all players have left
}
