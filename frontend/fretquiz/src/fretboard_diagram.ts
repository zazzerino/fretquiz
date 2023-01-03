export interface Point {
  x: number;
  y: number;
}

/**
 * A string and fret on the fretboard.
 */
export interface FretCoord {
  string: number;
  fret: number;
}

/**
 * A FretCoord with an optional color.
 * Represents a dot drawn on the fretboard diagram.
 */
export interface Dot extends FretCoord {
  color?: string;
}

/**
 * The function called when the diagram is clicked.
 */
export type OnClick = (coord: FretCoord, elem: SVGSVGElement, state: FretboardState) => any;

/**
 * The settings used by a fretboard diagram.
 */
export interface Opts {
  width: number;
  height: number;
  startFret: number;
  endFret: number;
  showFretNums: boolean;
  stringNames: string[]
  showStringNames: boolean;
  dots: Dot[];
  dotColor: string;
  drawDotOnHover: boolean;
  hoverDotColor: string;
  label?: string;
  onClick?: OnClick;
}

/**
 * Fretboard data that will be calculated from the given Opts.
 */
export type FretboardData = {
  xMargin: number;
  yMargin: number;
  neckWidth: number;
  neckHeight: number;
  stringCount: number;
  stringMargin: number;
  fretCount: number;
  fretHeight: number;
  fretNumOffset: number;
  dotRadius: number;
}

/**
 * An object combining the keys of Opts and FretboardData.
 */
export type FretboardState = Opts & FretboardData;

/**
 * The svg namespace. Needed to create svg elements.
 */
const SVG_NS = 'http://www.w3.org/2000/svg';

/**
 * Create an svg element with the given width and height.
 */
export function makeSvgElement(width: number, height: number): SVGSVGElement {
  const elem = document.createElementNS(SVG_NS, 'svg');
  elem.setAttribute('width', width.toString());
  elem.setAttribute('height', height.toString());
  elem.setAttribute('viewBox', `0 0 ${width} ${height}`);
  return elem;
}

/**
 * Create an svg line element from (x1,y1) to (x2,y2).
 */
export function makeLine(x1: number, y1: number, x2: number, y2: number, color = 'black'): SVGLineElement {
  const line = document.createElementNS(SVG_NS, 'line');
  line.setAttribute('x1', x1.toString());
  line.setAttribute('y1', y1.toString());
  line.setAttribute('x2', x2.toString());
  line.setAttribute('y2', y2.toString());
  line.setAttribute('stroke', color);
  return line;
}

/**
 * Create an svg circle element.
 * @param cx The center x coordinate.
 * @param cy The center y coordinate.
 * @param r The radius.
 * @param color The color that the circle will be filled in with.
 */
export function makeCircle(cx: number, cy: number, r: number, color = 'white'): SVGCircleElement {
  const circle = document.createElementNS(SVG_NS, 'circle');
  circle.setAttribute('cx', cx.toString());
  circle.setAttribute('cy', cy.toString());
  circle.setAttribute('r', r.toString());
  circle.setAttribute('stroke', 'black');
  circle.setAttribute('fill', color);
  return circle;
}

/**
 * Create an svg text element.
 * (x,y) is the starting point of the text baseline.
 */
export function makeText(x: number, y: number, text: string, fontSize = 16): SVGTextElement {
  const textElem = document.createElementNS(SVG_NS, 'text');
  textElem.setAttribute('x', x.toString());
  textElem.setAttribute('y', y.toString());
  textElem.setAttribute('text-anchor', 'middle');
  textElem.setAttribute('font-size', fontSize.toString());

  const textNode = document.createTextNode(text);
  textElem.appendChild(textNode);

  return textElem;
}

/**
 * Options for a 6-string guitar in standard tuning.
 */
const DEFAULT_OPTS: Opts = {
  width: 200,
  height: 300,
  startFret: 1,
  endFret: 4,
  showFretNums: false,
  stringNames: ['E', 'B', 'G', 'D', 'A', 'E'],
  showStringNames: false,
  dots: [],
  dotColor: 'white',
  drawDotOnHover: false,
  hoverDotColor: 'white',
}

/**
 * The main exported function.
 * Will return an svg element with a depiction of a fretboard described by the given userOpts.
 */
export function makeFretboardDiagram(userOpts: Partial<Opts>, defaultOpts = DEFAULT_OPTS): SVGSVGElement {
  const opts: Opts = {...defaultOpts, ...userOpts};
  const state: FretboardState = {...opts, ...fretboardData(opts)};

  const {width, height, dots, label, showFretNums, onClick, drawDotOnHover} = opts;
  const elem = makeSvgElement(width, height);

  appendStrings(elem, state);
  appendFrets(elem, state);

  if (showFretNums) appendFretNums(elem, state);
  if (label) appendLabel(elem, state, label);
  if (dots.length) appendDots(elem, state, dots);
  if (drawDotOnHover) addHoverListener(elem, state);

  if (onClick) {
    elem.onclick = (event: MouseEvent) => {
      const coord = closestFretCoord(elem, state, event);
      onClick(coord, elem, state);
    }
  }

  return elem;
}

/**
 * Calculate fretboard data from the given opts.
 */
function fretboardData(opts: Opts): FretboardData {
  const {width, height, stringNames, label, startFret, endFret} = opts;

  const xMargin = width / 6;
  const yMarginOffset = label === '' ? 1 : 1.5;
  const yMargin = (height / 8) * yMarginOffset;

  const neckWidth = width - (xMargin * 2);
  const neckHeight = height - (yMargin * 2);

  const stringCount = stringNames.length;
  const stringMargin = neckWidth / (stringCount - 1);

  const fretCountOffset = startFret === 0 ? 0 : 1;
  const fretCount = (endFret - startFret) + fretCountOffset;

  const fretHeight = neckHeight / fretCount;
  const fretNumOffset = neckWidth / 6;

  const dotRadius = fretHeight / 6;

  return {
    xMargin,
    yMargin,
    neckWidth,
    neckHeight,
    stringCount,
    stringMargin,
    fretCount,
    fretHeight,
    fretNumOffset,
    dotRadius,
  };
}

function appendStrings(elem: SVGElement, state: FretboardState) {
  const {xMargin, yMargin, neckHeight, stringCount, stringMargin} = state;

  for (let i = 0; i < stringCount; i++) {
    const x = (i * stringMargin) + xMargin;
    const y1 = yMargin;
    const y2 = yMargin + neckHeight;
    const line = makeLine(x, y1, x, y2);
    line.setAttribute("pointer-events", "none");
    elem.appendChild(line);
  }
}

function appendFrets(elem: SVGElement, state: FretboardState) {
  const {width, xMargin, yMargin, fretCount, fretHeight} = state;

  for (let i = 0; i <= fretCount; i++) {
    const y = (i * fretHeight) + yMargin;
    const x1 = xMargin;
    const x2 = width - xMargin;
    const line = makeLine(x1, y, x2, y);
    line.setAttribute("pointer-events", "none");
    elem.appendChild(line);
  }
}

function appendLabel(elem: SVGElement, state: FretboardState, label: string) {
  const {width, yMargin} = state;
  const x = width / 2;
  const y = yMargin - (yMargin / 2);
  const textElem = makeText(x, y, label);
  elem.appendChild(textElem);
}

function appendFretNums(elem: SVGElement, state: FretboardState) {
  const {stringCount: string, startFret, endFret, fretHeight, fretNumOffset} = state;
  const fontSize = 16; // TODO: adjust this for different diagram sizes

  for (let fret = startFret; fret <= endFret; fret++) {
    const point = fretCoordPoint({fret, string}, state);
    const x = point.x - fretNumOffset;
    const y = point.y + (fretHeight / 4);
    const textElem = makeText(x, y, fret.toString(), fontSize);
    elem.appendChild(textElem);
  }
}

function makeDotElem(state: FretboardState, dot: Dot): SVGCircleElement {
  const {dotColor, dotRadius} = state;
  const {x, y} = fretCoordPoint(dot, state);

  const color = dot.color || dotColor;
  const dotScale = dot.fret === 0 ? 0.66 : 1; // open string dots will be a little smaller
  const radius = dotRadius * dotScale;
  const cy = y + (radius / 2);

  const circle = makeCircle(x, cy, radius, color);
  circle.setAttribute("pointer-events", "none");
  return circle;
}

function appendDots(elem: SVGElement, state: FretboardState, dots: Dot[]) {
  dots.forEach(dot => elem.appendChild(makeDotElem(state, dot)));
}

/**
 * Takes a FretCoord and returns the Point relative to the top left of the parent svg container.
 */
function fretCoordPoint(fretCoord: FretCoord, state: FretboardState) {
  const {string, fret} = fretCoord;
  const {xMargin, yMargin, stringCount, stringMargin, fretHeight} = state;

  const stringNum = Math.abs(string - stringCount);
  const x = (stringNum * stringMargin) + xMargin;
  const yOffset = fret === 0 ? 0 : -fretHeight / 8;
  const y = (fret * fretHeight) - (fretHeight / 2) + yMargin + yOffset;

  return {x, y};
}

/**
 * Returns the svg coordinate (x,y) of the `event`, i.e., the coordinate that was clicked.
 */
function cursorPoint(elem: SVGSVGElement, event: MouseEvent): Point {
  const point = elem.createSVGPoint();
  point.x = event.clientX;
  point.y = event.clientY;

  const screenCTM = elem.getScreenCTM();
  if (!screenCTM) throw new Error(`could not get the screen ctm of ${elem}`);

  const matrix = screenCTM.inverse();
  return point.matrixTransform(matrix);
}

/**
 * Find the closest FretCoord to the clicked point.
 */
function closestFretCoord(elem: SVGSVGElement, state: FretboardState, event: MouseEvent): FretCoord {
  const {xMargin, yMargin, stringMargin, fretHeight, stringCount} = state;
  const point = cursorPoint(elem, event);
  const x = point.x - xMargin;
  const y = point.y - yMargin + (fretHeight / 2);

  let string = Math.abs(Math.round(x / stringMargin) - (stringCount));
  if (string < 1) {
    string = 1;
  } else if (string > state.stringCount) {
    string = state.stringCount;
  }

  let fret = Math.round(y / fretHeight);
  if (fret > state.endFret) {
    fret = state.endFret;
  }

  return {string, fret};
}

function fretCoordEqual(c1: FretCoord, c2: FretCoord): boolean {
  return c1.string === c2.string && c1.fret === c2.fret;
}

function addHoverListener(elem: SVGSVGElement, state: FretboardState) {
  let hoverDot: SVGCircleElement | null = null;
  let prevCoord: FretCoord | null = null;

  elem.onmousemove = (event: MouseEvent) => {
    const coord = closestFretCoord(elem, state, event);
    if (prevCoord && fretCoordEqual(coord, prevCoord)) return; // return if we're still closest to the same string/fret

    prevCoord = coord;
    const dot: Dot = {...coord, color: state.hoverDotColor};

    if (hoverDot) hoverDot.remove(); // remove the previous one
    hoverDot = makeDotElem(state, dot);

    elem.appendChild(hoverDot);
  }

  elem.onmouseout = _ev => {
    if (hoverDot) hoverDot.remove();
  }
}
