import {sendStartNextRound} from "../websocket";

export function StartRoundButton(props: {gameId: number}) {
  return (
    <button className="uppercase bg-blue-500 hover:bg-blue-700 text-white rounded font-bold px-4 py-2 mb-4"
      onClick={() => sendStartNextRound(props.gameId)}
    >
      Start Round
    </button>
  );
}
