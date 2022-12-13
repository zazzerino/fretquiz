import * as React from "react";
import {sendStartGame} from "../websocket";

export function StartGameButton(props: {gameId: number}) {
  return (
    <button className="uppercase bg-blue-500 hover:bg-blue-700 text-white rounded font-bold px-4 py-2 mb-4"
            onClick={() => sendStartGame(props.gameId)}
    >
      Start Game
    </button>
  );
}