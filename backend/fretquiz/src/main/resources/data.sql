DROP TABLE IF EXISTS guess;
DROP TABLE IF EXISTS note;
DROP TABLE IF EXISTS round;
DROP TABLE IF EXISTS player;
DROP TABLE IF EXISTS settings;
DROP TABLE IF EXISTS game;
DROP TABLE IF EXISTS "user";

CREATE TABLE "user" (
    id SERIAL PRIMARY KEY ,
    session_id TEXT UNIQUE,
    name TEXT
);

CREATE TABLE game (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE,
    status TEXT,
    host_id INTEGER
);

CREATE TABLE settings (
    game INTEGER,
    round_count INTEGER,
    strings_to_use INTEGER[],
    accidentals_to_use TEXT[],
    start_fret INTEGER,
    end_fret INTEGER
);

CREATE TABLE player (
    id INTEGER,
    game INTEGER,
    game_key INTEGER,
    name TEXT,
    score INTEGER
);

CREATE TABLE round (
    id SERIAL PRIMARY KEY,
    game INTEGER,
    game_key INTEGER,
    seconds_elapsed INTEGER
);

CREATE TABLE note (
    round INTEGER,
    white_key TEXT,
    accidental TEXT,
    octave INTEGER
);

CREATE TABLE guess (
    round INTEGER,
    round_key INTEGER,
    player_id INTEGER,
    clicked_coord_string INTEGER,
    clicked_coord_fret INTEGER,
    correct_coord_string INTEGER,
    correct_coord_fret INTEGER,
    is_correct BOOLEAN
);
