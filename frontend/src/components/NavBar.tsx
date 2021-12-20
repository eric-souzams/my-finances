import { NavbarItem } from "./NavBarItem";
import { Link } from 'react-router-dom';
import { AuthContext } from "../contexts/AuthContext";

export function NavBar() {
  return (
    <AuthContext.Consumer>
      {(context) => (
        <div className="navbar navbar-expand-lg fixed-top navbar-dark bg-primary">
          <div className="container">

            <Link to="/" className="navbar-brand">Minhas Finanças</Link>

            <button className="navbar-toggler" type="button"
              data-toggle="collapse" data-target="#navbarResponsive"
              aria-controls="navbarResponsive" aria-expanded="false"
              aria-label="Toggle navigation">
              <span className="navbar-toggler-icon"></span>
            </button>

            <div className="collapse navbar-collapse" id="navbarResponsive">
              <ul className="navbar-nav">
                <NavbarItem render={context.isAuthenticated} to="/home" title="Home" />
                <NavbarItem render={context.isAuthenticated} to="/releases" title="Lançamentos" />
                <NavbarItem render={context.isAuthenticated} to="/signup" title="Cadastro de Usuário" />
                <NavbarItem render={context.isAuthenticated} onClick={context.endSession} to="/login" title="Sair" />
              </ul>
            </div>
          </div>
        </div>
      )}
    </AuthContext.Consumer>
  );
}