import * as React from "react";
import {Dot, makeFretboardDiagram} from "../fretboard_diagram";
import {removeChildren} from "../util";
import {Guess} from "../types";
import {sendGuess} from "../websocket";

interface FretboardProps {
  elemId: string;
  gameId?: number;
  dots?: Dot[];
  drawDotOnHover: boolean;
  guess?: Guess;
}

export function Fretboard(props: FretboardProps) {
  React.useEffect(() => {
    const dots = props.dots || [];
    if (props.guess) {
      dots.push({...props.guess.correctCoord, color: "limegreen"});
      if (!props.guess.isCorrect) {
        dots.push({...props.guess.clickedCoord, color: "salmon"});
      }
    }

     const diagram = makeFretboardDiagram({
       drawDotOnHover: props.drawDotOnHover,
       dots,
       showFretNums: true,
       onClick: coord => props.gameId && sendGuess(props.gameId, coord),
     });

     const elem = document.getElementById(props.elemId)!;
     elem.appendChild(diagram);

     return () => removeChildren(elem);
  });

  return (
    <div id={props.elemId}></div>
  );
}
