import * as React from "react";
import {Dot, FretboardState, makeFretboardDiagram} from "../fretboard_diagram";
import {removeChildren} from "../util";
import {FretCoord} from "../types";
import {sendGuess} from "../websocket";

interface FretboardProps {
  elemId: string;
  dots?: Dot[];
  drawDotOnHover: boolean;
}

function onClick(coord: FretCoord, _elem: SVGSVGElement, _state: FretboardState) {
  console.log(`clicked: ${JSON.stringify(coord)}`);
  // sendGuess()
}

export function Fretboard(props: FretboardProps) {
  React.useEffect(() => {
     const diagram = makeFretboardDiagram({
       drawDotOnHover: props.drawDotOnHover,
       dots: props.dots || [],
       showFretNums: true,
       onClick,
     });

     const elem = document.getElementById(props.elemId)!;
     elem.appendChild(diagram);

     return () => removeChildren(elem);
  });

  return (
    <div id={props.elemId}></div>
  );
}
