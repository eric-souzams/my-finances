import { useState } from "react";
import { useHistory } from 'react-router-dom';

import { Card } from "../components/Card";
import { FormGroup } from "../components/FormGroup";
import { errorMessage } from '../components/Toastr';

import { LocalStorageService } from "../services/localStorageService";
import { UserService } from "../services/usuarioService";

export function Login() {
  const history = useHistory();
  const api = new UserService();

  const[userEmail, setUserEmail] = useState('');
  const[userPassword, setUserPassword] = useState('');

  async function handlerJoin() {
    await api.auth({
      email: userEmail,
      senha: userPassword
    }).then(response => {
      LocalStorageService.addItem('user_data', response.data);
      history.push('/home');
    }).catch(erro => {
      errorMessage(erro.response.data)
    })
  }

  function handlerSignup() {
    history.push('/signup');
  }

  return (
    <div className="container">
      <div className="row">
        <div className="col-md-6" style={{position: 'relative', left: '300px'}}>

          <div className="bs-docs-section">
            <Card title="Login">
              <div className="row">
                <div className="col-lg-12">

                  <div className="bs-component">
                    <fieldset>
                      <FormGroup label="Email: *" htmlFor="email">
                        <input
                          value={userEmail}
                          onChange={event => setUserEmail(event.target.value)}
                          type="email" 
                          className="form-control"
                          id="email"
                          aria-describedby="emailHelp"
                          placeholder="Digite o email"
                        />
                      </FormGroup>
                      
                      <FormGroup label="Senha: *" htmlFor="password">
                        <input
                          value={userPassword}
                          onChange={event => setUserPassword(event.target.value)}
                          type="password" 
                          className="form-control"
                          id="password"
                          aria-describedby="passwordHelp"
                          placeholder="Digite a senha"
                        />
                      </FormGroup>

                      <button 
                        className="btn btn-light mr-2"
                        onClick={handlerJoin}
                      >
                        Entrar
                      </button>
                      <button 
                        className="btn btn-success"
                        onClick={handlerSignup}
                      >
                        Cadastrar
                      </button>
                    </fieldset>
                  </div>

                </div>
              </div>
            </Card>
          </div>

        </div>
      </div>
    </div>
  );
}
