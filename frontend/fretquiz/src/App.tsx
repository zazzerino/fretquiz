import React from 'react';
import {UsernameForm} from "./components/UsernameForm";
import {Footer} from "./components/Footer";
import {appReducer, initState} from "./reducer";
import {openWebSocket} from "./websocket";
import {CreateGameButton} from "./components/CreateGameButton";
import {Fretboard} from "./components/Fretboard";
import {StartGameButton} from "./components/StartGameButton";
import {Staff} from "./components/Staff";
import {Dot} from "./fretboard_diagram";

export default function App() {
  const [state, dispatch] = React.useReducer(appReducer, initState);
  const [dots, setDots] = React.useState<Dot[]>([]);

  React.useEffect(() => openWebSocket(dispatch), []);

  React.useEffect(() => {
    setDots(_prevDots => []);
    if (state.guess && (state.game?.status === "ROUND_OVER" || state.game?.status === "GAME_OVER")) {
      // @ts-ignore
      setDots(prevDots => [...prevDots, {...state.guess.correctCoord, color: "limegreen"}])
      if (!state.guess.isCorrect) {
        // @ts-ignore
        setDots(prevDots => [...prevDots, {...state.guess.clickedCoord, color: "salmon"}])
      }
    }
  }, [state.guess, state.game]);

  return (
    <main className="m-0 text-center flex flex-col items-center h-screen relative">
      <h1 className="m-4 text-3xl font-bold underline">
        FretQuiz
      </h1>
      <Staff id="staff-elem" width={250} height={120} note={state.game?.noteToGuess} />
      <Fretboard
        gameId={state.game?.id}
        dots={dots}
        drawDotOnHover={state.game?.status === "PLAYING"}
      />
      <CreateGameButton />
      {state.game && <StartGameButton gameId={state.game.id} />}
      <p>{JSON.stringify(state.game)}</p>
      <UsernameForm />
      <Footer username={state.user.name} />
    </main>
  );
}
