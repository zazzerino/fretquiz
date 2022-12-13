import * as React from "react";
import {Dot, makeFretboardDiagram} from "../fretboard_diagram";
import {FretCoord, Note} from "../types";

function removeChildren(elem: HTMLElement) {
  while (elem.firstChild) {
    elem.removeChild(elem.firstChild);
  }
}

interface FretboardProps {
  id: string;
  fretCoordToGuess?: FretCoord;
}

export function Fretboard(props: FretboardProps) {
  const dots = props.fretCoordToGuess ? [props.fretCoordToGuess] : [];

  React.useEffect(() => {
     const diagram = makeFretboardDiagram({
       drawDotOnHover: true,
       dots,
     });

     const elem = document.getElementById(props.id)!;
     elem.appendChild(diagram);

     return () => removeChildren(elem);
  });

  return (
    <div id={props.id}></div>
  );
}
