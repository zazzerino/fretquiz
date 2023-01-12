import {AppAction, AppState} from "./types";

export function appReducer(state: AppState, action: AppAction) {
  switch (action.type) {
    case "set_user": {
      return {...state, user: action.user};
    }
    case "set_game": {
      return {...state, game: action.game};
    }
    case "set_guess": {
      return {...state, guess: action.guess};
    }
  }
}

export const initState: AppState = {
  user: {id: -1, name: "anon"},
}
