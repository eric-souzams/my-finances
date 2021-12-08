import { useState } from "react";
import { useHistory } from 'react-router-dom';

import { Card } from "../components/Card";
import { FormGroup } from "../components/FormGroup";

export function Signup() {

    const history = useHistory();

    const[inputName, setInputName] = useState('');
    const[inputEmail, setInputEmail] = useState('');
    const[inputPassword, setInputPassword] = useState('');
    const[inputConfirmPassword, setInputConfirmPassword] = useState('');

    function handlerCancel() {
        history.push('/login');
    }

    function handlerSignup() {

    }

    const newUser = {
        name: inputName,
        email: inputEmail,
        password: inputPassword,
        confirmPassword: inputConfirmPassword
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