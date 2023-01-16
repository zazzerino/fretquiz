import {Client, IMessage} from "@stomp/stompjs";
import * as React from "react";
import {AppAction, FretCoord, Game, Guess, User} from "./types";

let DISPATCH: React.Dispatch<AppAction> | null;

const stompClient = new Client({
  brokerURL: "ws://localhost:8080/ws",
  onConnect: _frame => {
    console.log("connected");
    stompClient.subscribe("/user/queue/user", onUserMessage);
    stompClient.subscribe("/user/queue/game/join", onGameJoinMessage);
    stompClient.subscribe("/user/queue/game/guess", onGuessMessage);
  },
  onDisconnect: _frame => {
    console.log("disconnected") ;
  },
  onStompError: frame => {
    console.error(`stomp error: ${frame.headers["message"]}: ${frame.body}`);
  },
});

export function openWebSocket(dispatch: React.Dispatch<AppAction>) {
  stompClient.activate();
  DISPATCH = dispatch;
}

function onUserMessage(message: IMessage) {
  console.log(`user message: ${JSON.stringify(message.body)}`);
  const user = JSON.parse(message.body) as User;
  DISPATCH && DISPATCH({type: "set_user", user});
}

function onGameJoinMessage(message: IMessage) {
  console.log(`game join message: ${JSON.stringify(message.body)}`);
  const game = JSON.parse(message.body) as Game;
  stompClient.subscribe(`/topic/game/${game.id}`, onGameMessage);
  DISPATCH && DISPATCH({type: "set_game", game});
}

function onGameMessage(message: IMessage) {
  console.log(`game message: ${JSON.stringify(message.body)}`);
  const game = JSON.parse(message.body) as Game;
  DISPATCH && DISPATCH({type: "set_game", game});
}

function onGuessMessage(message: IMessage) {
  console.log(`guess message: ${JSON.stringify(message.body)}`);
  const guess = JSON.parse(message.body) as Guess;
  DISPATCH && DISPATCH({type: "set_guess", guess});
}

export function sendUpdateUsername(username: string) {
  stompClient.publish({
    destination: "/app/topic/user/name/update",
    body: username,
  });
}

export function sendCreateGame() {
  stompClient.publish({
    destination: "/app/topic/game/create",
  });
}

export function sendStartGame(gameId: number) {
  stompClient.publish({
    destination: `/app/topic/game/${gameId}/start`,
  });
}

export function sendGuess(gameId: number, fretCoord: FretCoord) {
  stompClient.publish({
    destination: `/app/topic/game/${gameId}/guess`,
    body: JSON.stringify(fretCoord),
  });
}

export function sendStartNextRound(gameId: number) {
  stompClient.publish({
    destination: `/app/topic/game/${gameId}/round/start`,
  });
}
