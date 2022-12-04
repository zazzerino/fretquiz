import {Client, IMessage} from "@stomp/stompjs";

const brokerURL = "ws://localhost:8080/ws";

const stompClient = new Client({
  brokerURL,
  onConnect: _frame => {
    console.log("connected");
    stompClient.subscribe("/user/queue/username", onUsernameUpdate);
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
    destination: "/app/queue/username",
    body: JSON.stringify({username}),
  });
}

function onUsernameUpdate(message: IMessage) {
  console.log(`username message: ${message.body}`);
}
