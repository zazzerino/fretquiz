import React from 'react';
import {UsernameForm} from "./components/UsernameForm";
import {Footer} from "./components/Footer";

interface AppState {
  username: string;
}

type Action =
  {type: "update_username", username: string};


function appReducer(state: AppState, action: Action) {
  switch (action.type) {
    case "update_username": {
      return {...state, username: action.username}
    }
  }
}

const initState: AppState = {
  username: "anon",
}

export default function App() {
  const [state, dispatch] = React.useReducer(appReducer, initState);

  return (
    <div className="m-0 text-center flex flex-col items-center h-screen">
      <h1 className="m-4 text-3xl font-bold underline">
        FretQuiz
      </h1>
      <UsernameForm />
      <Footer username={state.username} />
    </div>
  );
}
