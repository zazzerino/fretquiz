import {Client, IMessage} from "@stomp/stompjs";
import * as React from "react";
import {AppAction, User} from "./types";

let DISPATCH: React.Dispatch<AppAction> | null;

const brokerURL = "ws://localhost:8080/ws";

const stompClient = new Client({
  brokerURL,
  onConnect: _frame => {
    console.log("connected");
    stompClient.subscribe("/user/queue/user", onUserMessage);
    stompClient.subscribe("/user/queue/game", onGameMessage);
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

function onGameMessage(message: IMessage) {
  console.log(`game message: ${JSON.stringify(message.body)}`);
}

export function sendUpdateUsername(username: string) {
  stompClient.publish({
    destination: "/app/topic/user/name/update",
    body: JSON.stringify({username}),
  });
}

export function sendCreateGame() {
  stompClient.publish({
    destination: "/app/topic/game/create",
  });
}
