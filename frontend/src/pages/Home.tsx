import { useState } from "react";
import {Link} from 'react-router-dom';

export function Home() {

    const[balance, setBalance] = useState('0,00');

    return (
        <div className="container">
            <div className="jumbotron">
                <h1 className="display-3">Bem vindo!</h1>
                <p className="lead">Esse é seu sistema de finanças.</p>
                <p className="lead">Seu saldo para o mês atual é de R$ {balance}</p>
                <hr className="my-4" />
                <p>E essa é sua área administrativa, utilize um dos menus ou botões abaixo para navegar pelo sistema.</p>
                <p className="lead">
                    <Link 
                        className="btn btn-primary btn-lg" 
                        to="/releases" 
                        role="button">
                    <i className="pi pi-users"></i>  
                    Consultar Lançamentos
                    </Link>
                    <Link 
                        className="btn btn-danger btn-lg" 
                        to="/new-release" 
                        role="button">
                    <i className="pi pi-money-bill"></i>  
                    Cadastrar Novo Lançamento
                    </Link>
                </p>
            </div>
        </div>
    );
}