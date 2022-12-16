export function removeChildren(elem: HTMLElement) {
  while (elem.firstChild) {
    elem.removeChild(elem.firstChild);
  }
}
