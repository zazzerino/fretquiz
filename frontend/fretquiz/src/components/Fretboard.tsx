import * as React from "react";
import {Dot, makeFretboardDiagram} from "../fretboard_diagram";
import {removeChildren} from "../util";

interface FretboardProps {
  id: string;
  dots?: Dot[];
}

export function Fretboard(props: FretboardProps) {
  React.useEffect(() => {
     const diagram = makeFretboardDiagram({
       drawDotOnHover: true,
       dots: props.dots || [],
     });

     const elem = document.getElementById(props.id)!;
     elem.appendChild(diagram);

     return () => removeChildren(elem);
  });

  return (
    <div id={props.id}></div>
  );
}
