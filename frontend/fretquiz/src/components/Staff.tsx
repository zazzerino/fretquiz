import * as React from "react";
import {Vex} from "vexflow";
import {removeChildren} from "../util";
import {Accidental, Note} from "../types";

const vf = Vex.Flow;

function formatAccidental(accidental: Accidental): string {
  switch (accidental) {
    case "DOUBLE_SHARP": return "##";
    case 'SHARP': return "#";
    case 'FLAT': return "b";
    case "DOUBLE_FLAT": return "bb";
    default: return "";
  }
}

function formatNote(note: Note): [string, string | null] {
  const accidental = formatAccidental(note.accidental);
  return [note.whiteKey + accidental + '/' + note.octave, accidental];
}

export function Staff(props: {id: string; width: number; height: number; note?: Note}) {
  React.useEffect(() => {
    const div = document.getElementById(props.id)! as HTMLDivElement;

    const renderer = new vf.Renderer(div, vf.Renderer.Backends.SVG);
    renderer.resize(props.width, props.height);
    const context = renderer.getContext();

    const stave = new vf.Stave(0, 0, props.width - 1);
    stave.addClef("treble")
      .setContext(context)
      .draw();

    if (props.note) {
      const [note, accidental] = formatNote(props.note);

      const staveNote = new vf.StaveNote({
        keys: [note],
        duration: "w",
        align_center: true,
      });

      if (accidental) {
        staveNote.addModifier(new vf.Accidental(accidental));
      }

      vf.Formatter.FormatAndDraw(context, stave, [staveNote]);
    }

    return () => removeChildren(div);
  });

  return (
    <div id={props.id}>
    </div>
  );
}