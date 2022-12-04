import * as React from "react";
import {sendUpdateUsername} from "../websocket";

export function NameInput() {
  const [name, setName] = React.useState("");

  const handleSubmit: React.FormEventHandler<HTMLFormElement> = event => {
    event.preventDefault();
    console.log("submitting " + name);

    if (name) {
      sendUpdateUsername(name);
    }
  }

  return (
    <form className="border px-8 py-4 rounded shadow-md"
          onSubmit={handleSubmit}
    >
      <input className="text-center block m-auto shadow mb-2 border"
             type="text"
             name="username"
             placeholder="Enter name"
             value={name}
             onChange={e => setName(e.target.value)}
      />
      <input className="uppercase bg-blue-500 hover:bg-blue-700 text-white rounded font-bold px-4 py-2"
             type="submit"
             value="Update name"

      />
    </form>
  );
}