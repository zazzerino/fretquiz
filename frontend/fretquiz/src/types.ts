export interface User {
  id: number;
  name: string;
}

export interface Settings {
  roundCount: number;
  stringsToUse: number[];
  accidentalsToUse: string[];
  startFret: number;
  endFret: number;
}

export interface FretCoord {
  string: number;
  fret: number;
}

export interface Guess {
  playerId: number;
  clickedCoord: FretCoord;
  correctCoord: FretCoord;
  isCorrect: boolean;
}

export interface Note {
  whiteKey: string;
  accidental: string;
  octave: number;
}

export interface Round {
  noteToGuess: Note;
  guesses: Guess[];
  secondsElapsed: number;
}

interface Player extends User {
  score: number;
}

export interface Game {
  id: number;
  status: string;
  settings: Settings;
  rounds: Round[];
  hostId: number;
  players: Player[];
  fretCoordToGuess: FretCoord;
}

export interface AppState {
  user: User;
  game?: Game;
}

export type AppAction =
  | {type: "set_user", user: User}
  | {type: "set_game", game: Game}
  ;
