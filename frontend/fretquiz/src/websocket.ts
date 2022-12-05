import {Client, IMessage} from "@stomp/stompjs";

const brokerURL = "ws://localhost:8080/ws";

const stompClient = new Client({
  brokerURL,
  onConnect: _frame => {
    console.log("connected");
    stompClient.subscribe("/user/topic/user", onUserMessage);
  },
  onDisconnect: _frame => {
    console.log("disconnected") ;
  },
  onStompError: frame => {
    console.error(`stomp error: ${frame.headers["message"]}: ${frame.body}`);
  },
});

export function activateWebSocket() {
  stompClient.activate();
}

export function sendUpdateUsername(username: string) {
  stompClient.publish({
    destination: "/app/topic/user/name",
    body: JSON.stringify({username}),
  });
}

function onUserMessage(message: IMessage) {
  console.log(`user message: ${JSON.stringify(message)}`);
}
