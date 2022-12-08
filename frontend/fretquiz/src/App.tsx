import React from 'react';
import {UsernameForm} from "./components/UsernameForm";
import {Footer} from "./components/Footer";
import {appReducer, initState} from "./reducer";
import {openWebSocket} from "./websocket";

export default function App() {
  const [state, dispatch] = React.useReducer(appReducer, initState);

  React.useEffect(() => {
    openWebSocket(dispatch);
  });

  return (
    <div className="m-0 text-center flex flex-col items-center h-screen">
      <h1 className="m-4 text-3xl font-bold underline">
        FretQuiz
      </h1>
      <UsernameForm dispatch={dispatch} />
      <Footer username={state.user.name} />
    </div>
  );
}
