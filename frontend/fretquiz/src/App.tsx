import React from 'react';
import {NameInput} from "./components/NameInput";

export default function App() {
  return (
    <div className="m-0 text-center flex flex-col items-center">
      <h1 className="m-4 text-3xl font-bold underline">
        FretQuiz
      </h1>
      <NameInput />
    </div>
  );
}
