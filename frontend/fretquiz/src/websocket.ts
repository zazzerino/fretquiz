import {Client, IMessage} from "@stomp/stompjs";
import * as React from "react";
import {AppAction, User} from "./reducer";

let DISPATCH: React.Dispatch<AppAction> | null;

const brokerURL = "ws://localhost:8080/ws";

const stompClient = new Client({
  brokerURL,
  onConnect: _frame => {
    console.log("connected");
    stompClient.subscribe("/user/queue/user", onUserMessage);
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

// export function closeWebSocket() {
//   stompClient.deactivate()
//     .then(() => console.log("stomp client deactivated"));
//
//   DISPATCH = null;
// }

export function sendUpdateUsername(username: string) {
  stompClient.publish({
    destination: "/app/topic/user/name",
    body: JSON.stringify({username}),
  });
}

function onUserMessage(message: IMessage) {
  console.log(`user message: ${JSON.stringify(message.body)}`);
  const user = JSON.parse(message.body) as User;
  DISPATCH && DISPATCH({type: "set_user", user});
}
