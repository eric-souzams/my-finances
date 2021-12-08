import { NavbarItem } from "./NavBarItem";

export function NavBar() {
    return (
        <div className="navbar navbar-expand-lg fixed-top navbar-dark bg-primary">
            <div className="container">

                <a href="/home" className="navbar-brand">Minhas Finanças</a>

                <button className="navbar-toggler" type="button" 
                        data-toggle="collapse" data-target="#navbarResponsive" 
                        aria-controls="navbarResponsive" aria-expanded="false" 
                        aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>

                <div className="collapse navbar-collapse" id="navbarResponsive">
                    <ul className="navbar-nav">
                        <NavbarItem to="/" title="Home" />
                        <NavbarItem to="/releases" title="Lançamentos" />
                        <NavbarItem to="/signup" title="Usuários" />
                        <NavbarItem to="/login" title="Login" />
                    </ul>
                </div>
            </div>
        </div>
    );
}