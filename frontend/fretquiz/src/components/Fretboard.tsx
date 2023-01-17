import * as React from "react";
import {Dot, makeFretboardDiagram} from "../fretboard_diagram";
import {sendGuess} from "../websocket";

interface FretboardProps {
  gameId?: number;
  dots?: Dot[];
  drawDotOnHover: boolean;
}

export function Fretboard(props: FretboardProps) {
  const width = 200;
  const height = 300;
  const ref = React.useRef<HTMLDivElement>(null);

  React.useEffect(() => {
     const diagram = makeFretboardDiagram({
       drawDotOnHover: props.drawDotOnHover,
       dots: props.dots,
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
