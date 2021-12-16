import { Link } from 'react-router-dom';

type NavbarItemProps = {
  render: boolean;
  onClick?: () => void;
  title: string;
  to: string;
}

export function NavbarItem(props: NavbarItemProps) {
  if (props.render) {
    return (
      <div>
        <li className="nav-item">
          <Link onClick={props.onClick} className="nav-link" to={props.to}>{props.title}</Link >
        </li>
      </div>
    );
  } else {
    return(
      <></>
    );
  }
}