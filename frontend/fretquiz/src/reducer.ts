import {AppAction, AppState} from "./types";

export function appReducer(state: AppState, action: AppAction) {
  switch (action.type) {
    case "set_user": {
      return {...state, user: action.user};
    }
    case "set_game": {
      return {...state, game: action.game};
    }
  }
}

export const initState: AppState = {
  user: {id: -1, name: "anon"},
}
