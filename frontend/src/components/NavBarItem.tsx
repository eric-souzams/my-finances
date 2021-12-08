type NavbarItemProps = {
    title: string;
    to: string;
}

export function NavbarItem(props: NavbarItemProps) {
    return (
        <div>
            <li className="nav-item">
                <a className="nav-link" href={props.to}>{props.title}</a>
            </li>
        </div>
    );
}