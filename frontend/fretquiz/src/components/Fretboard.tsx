import * as React from "react";
import {makeFretboardDiagram} from "../fretboard_diagram";

function removeChildren(elem: HTMLElement) {
  while (elem.firstChild) {
    elem.removeChild(elem.firstChild);
  }
}

export function Fretboard(props: {id: string}) {
  React.useEffect(() => {
     const diagram = makeFretboardDiagram({});
     const elem = document.getElementById(props.id)!;
     elem.appendChild(diagram);
     return () => removeChildren(elem);
  });

  return (
    <div id={props.id}></div>
  );
}
