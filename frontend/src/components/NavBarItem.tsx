import {Link} from 'react-router-dom';

type NavbarItemProps = {
    title: string;
    to: string;
}

export function NavbarItem(props: NavbarItemProps) {
    return (
        <div>
            <li className="nav-item">
                <Link  className="nav-link" to={props.to}>{props.title}</Link >
            </li>
        </div>
    );
}