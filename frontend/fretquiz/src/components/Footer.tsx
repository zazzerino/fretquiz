import * as React from "react";

export function Footer(props: {username: string}) {
  return (
    <footer>
      {`user: ${props.username}`}
    </footer>
  );
}