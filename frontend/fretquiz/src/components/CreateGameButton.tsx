import {sendCreateGame} from "../websocket";

export function CreateGameButton() {
  return (
    <button className="uppercase bg-blue-500 hover:bg-blue-700 text-white rounded font-bold px-4 py-2 mb-4"
            onClick={sendCreateGame}
    >
      Create Game
    </button>
  );
}
