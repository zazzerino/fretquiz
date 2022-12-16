import React from 'react';
import {UsernameForm} from "./components/UsernameForm";
import {Footer} from "./components/Footer";
import {appReducer, initState} from "./reducer";
import {openWebSocket} from "./websocket";
import {CreateGameButton} from "./components/CreateGameButton";
import {Fretboard} from "./components/Fretboard";
import {StartGameButton} from "./components/StartGameButton";
import {Staff} from "./components/Staff";

export default function App() {
  const [state, dispatch] = React.useReducer(appReducer, initState);
  React.useEffect(() => openWebSocket(dispatch));

  return (
    <main className="m-0 text-center flex flex-col items-center h-screen relative">
      <h1 className="m-4 text-3xl font-bold underline">
        FretQuiz
      </h1>
      <Staff id="stave-elem" width={250} height={110} note={state.game?.noteToGuess} />
      <Fretboard id="fretboard-elem" />
      <CreateGameButton />
      {state.game && <StartGameButton gameId={state.game.id} />}
      <p>{JSON.stringify(state.game)}</p>
      <UsernameForm />
      <Footer username={state.user.name} />
    </main>
  );
}
