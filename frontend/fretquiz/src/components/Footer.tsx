import * as React from "react";

export function Footer(props: {username: string}) {
  return (
    <footer className="absolute bottom-2">
      {`user: ${props.username}`}
    </footer>
  );
}