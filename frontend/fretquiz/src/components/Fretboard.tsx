import * as React from "react";
import {Dot, makeFretboardDiagram} from "../fretboard_diagram";
import {Guess} from "../types";
import {sendGuess} from "../websocket";

interface FretboardProps {
  gameId?: number;
  gameStatus?: string;
  dots?: Dot[];
  guess?: Guess;
}

const width = 200;
const height = 300;
const correctColor = "limegreen"
const incorrectColor = "salmon";

export function Fretboard(props: FretboardProps) {
  const ref = React.useRef<HTMLDivElement>(null);

  React.useEffect(() => {
    const dots = props.dots || [];

    if (props.guess && (props.gameStatus === "ROUND_OVER" || props.gameStatus === "GAME_OVER")) {
      dots.push({...props.guess.correctCoord, color: correctColor});
      if (!props.guess.isCorrect) {
        dots.push({...props.guess.clickedCoord, color: incorrectColor});
      }
    }

     const diagram = makeFretboardDiagram({
       drawDotOnHover: props.gameStatus === "PLAYING",
       dots,
       showFretNums: true,
       onClick: coord => props.gameId && sendGuess(props.gameId, coord),
       width,
       height,
     });

    ref.current?.firstChild?.replaceWith(diagram);
  });

  return (
    <div ref={ref}>
      <svg width={width} height={height}>
      </svg>
    </div>
  );
}
