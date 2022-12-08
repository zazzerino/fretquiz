export interface User {
  id: number;
  name: string;
}

export interface AppState {
  user: User;
}

export type AppAction =
  {type: "set_user", user: User};


export function appReducer(state: AppState, action: AppAction) {
  switch (action.type) {
    case "set_user": {
      return {...state, user: action.user}
    }
  }
}

export const initState: AppState = {
  user: {id: -1, name: "anon"},
}