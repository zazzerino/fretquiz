DROP TABLE IF EXISTS "user";

CREATE TABLE "user" (
    id SERIAL PRIMARY KEY ,
    session_id TEXT UNIQUE,
    name TEXT
);
