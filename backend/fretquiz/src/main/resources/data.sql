DROP TABLE IF EXISTS "user";

CREATE TABLE "user" (
    id SERIAL PRIMARY KEY ,
    session_id TEXT UNIQUE,
    name TEXT
);

DROP TABLE IF EXISTS game;

CREATE TABLE game (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE,
    status TEXT,
    host_id INTEGER
);

DROP TABLE IF EXISTS settings;

CREATE TABLE settings (
    game INTEGER,
    round_count INTEGER,
    strings_to_use INTEGER[],
    accidentals_to_use TEXT[],
    start_fret INTEGER,
    end_fret INTEGER
);

DROP TABLE IF EXISTS player;

CREATE TABLE player (
    id INTEGER,
    game INTEGER,
    game_key INTEGER,
    name TEXT,
    score INTEGER
);

DROP TABLE IF EXISTS round;

CREATE TABLE round (
    game INTEGER,
    game_key INTEGER,
    seconds_elapsed INTEGER,
    note_white_key TEXT,
    note_accidental TEXT,
    note_octave INTEGER
);

DROP TABLE IF EXISTS guess;

CREATE TABLE guess (
    game INTEGER,
    game_key INTEGER,
    round_key INTEGER,
    player_id INTEGER,
    clicked_string INTEGER,
    clicked_fret INTEGER,
    correct_string INTEGER,
    correct_fret INTEGER,
    is_correct BOOLEAN
);
