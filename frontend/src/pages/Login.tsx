import { useState } from "react";
import { Card } from "../components/Card";
import { FormGroup } from "../components/FormGroup";

export function Login() {

  const[userEmail, setUserEmail] = useState('');
  const[userPassword, setUserPassword] = useState('');

  function handlerJoin() {
    
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
                        className="btn btn-success mr-2"
                        onClick={handlerJoin}
                      >
                        Entrar
                      </button>
                      <button 
                        className="btn btn-danger"
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
