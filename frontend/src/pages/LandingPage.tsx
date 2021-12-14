import { Link } from "react-router-dom";

export function LandingPage() {
  return (
    <div className="container text-center" >
      <h2>Bem vindo ao sistema Minhas Finanças</h2>
      Este é seu sistema para controle de finanças pessoais, clique no botão abaixo para acessar o sistema. < br />< br />

      <div className="offset-md-4 col-md-4">
        <Link
          to="/login"
          style={{ width: '100%' }}
          className="btn btn-success">
          <i className="pi pi-sign-in"></i> Acessar
        </Link>
      </div>
    </div>
  );
}