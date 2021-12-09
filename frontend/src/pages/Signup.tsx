import { useState } from "react";
import { useHistory } from 'react-router-dom';

import { Card } from "../components/Card";
import { FormGroup } from "../components/FormGroup";
import { errorMessage, successMessage } from '../components/Toastr';

import { UserService } from "../services/usuarioService";

type UserProps = {
	nome: string;
	email: string;
	senha: string;
	confirmarSenha: string;
}

export function Signup() {

    const history = useHistory();
    const api = new UserService();

    const[inputName, setInputName] = useState('');
    const[inputEmail, setInputEmail] = useState('');
    const[inputPassword, setInputPassword] = useState('');
    const[inputConfirmPassword, setInputConfirmPassword] = useState('');

    function handlerCancel() {
        history.push('/login');
    }

    function valid() {
		const error = [];

		if (!inputName) {
			error.push('O campo Nome é obrigatório.');
		}

		if (!inputEmail) {
			error.push('O campo Email é obrigatório.');
		} else if(!inputEmail.match(/^[a-z0-9.]+@[a-z0-9]+\.[a-z]/) ){
			error.push('Informe um Email válido.')
		}

		if (!inputPassword || !inputConfirmPassword) {
			error.push('Digite a senha 2 vezes.');
		} else if (inputPassword !== inputConfirmPassword) {
			error.push('As senhas informadas não batem.')
		}

		return error;
	}

    async function handlerSignup() {
        const erros = valid();

        if (erros && erros.length > 0) {
            erros.forEach((msg, index) => {
                errorMessage(msg);
            });

            return false;
        }

        const user = {
            nome: inputName,
            email: inputEmail,
            senha: inputPassword
        }

        await api.saveNewUser(user)
        .then(response => {
            history.push('/login');

            successMessage('Usuário cadastrado com sucesso!');
            setTimeout(function(){
                successMessage('Faça login para acessar o sistema.');
            }, 2000);
        }).catch(erro => {
            errorMessage(erro.response.data);
        });
    }

    return (
        <div className="container">
            <Card title="Cadastro de Usuário">
                <div className="row">
                        <div className="col-lg-12">
                            <div className="bs-component">
                                <FormGroup label="Nome: *" htmlFor="inputName">
                                    <input 
                                        type="text" 
                                        id="inputName" 
                                        className="form-control"
                                        name="name"
                                        onChange={event => setInputName(event.target.value)}
                                    />
                                </FormGroup>

                                <FormGroup label="Email: *" htmlFor="inputEmail">
                                    <input 
                                        type="email" 
                                        id="inputEmail"
                                        className="form-control"
                                        name="email"
                                        onChange={event => setInputEmail(event.target.value)} 
                                    />
                                </FormGroup>

                                <FormGroup label="Senha: *" htmlFor="inputPassword">
                                    <input 
                                        type="password" 
                                        id="inputPassword"
                                        className="form-control"
                                        name="password"
                                        onChange={event => setInputPassword(event.target.value)} 
                                    />
                                </FormGroup>

                                <FormGroup label="Confirme a Senha: *" htmlFor="inputConfirmPassword">
                                    <input 
                                        type="password" 
                                        id="inputConfirmPassword"
                                        className="form-control"
                                        name="confirmPassword"
                                        onChange={event => setInputConfirmPassword(event.target.value)} 
                                    />
                                </FormGroup>

                                <button 
                                    onClick={handlerSignup} 
                                    type="button" 
                                    className="btn btn-success">
                                    <i className="pi pi-save"></i> Salvar
                                </button>
                                <button 
                                    onClick={handlerCancel} 
                                    type="button" 
                                    className="btn btn-danger">
                                    <i className="pi pi-times"></i> Cancelar
                                </button>
                            </div>
                        </div>
                    </div>
            </Card>
        </div>
    );
}