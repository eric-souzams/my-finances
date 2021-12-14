import { useState } from "react";
import { Link, useHistory } from "react-router-dom";
import { Card } from "../../components/Card";
import { FormGroup } from "../../components/FormGroup";
import { SelectMenu } from "../../components/SelectMenu";
import { errorMessage, successMessage } from "../../components/Toastr";
import { ReleaseService } from "../../services/lancamentoService";
import { LocalStorageService } from "../../services/localStorageService";

export function NewRelease() {
  const api = new ReleaseService();
  const history = useHistory();

  const monthList = api.getMonthList();
  const typeList = api.getTypeList();

  const [isUpdate, setIsUpdate] = useState(false);

  const [releaseId, setReleaseId] = useState(null);
  const [inputDescription, setInputDescription] = useState('');
  const [inputValue, setInputValue] = useState('');
  const [inputMonth, setInputMonth] = useState('');
  const [inputYear, setInputYear] = useState('');
  const [inputType, setInputType] = useState('');
  const [inputStatus, setInputStatus] = useState('');

  function handlerUpdate() {
  }

  async function handlerSave() {
    const user = LocalStorageService.getItem('user_data');

    const data = {
      id: releaseId,
      descricao: inputDescription,
      valor: inputValue,
      mes: inputMonth,
      ano: inputYear,
      tipo: inputType,
      status: null,
      usuario: user.id
    }

    console.log(data);

    api.saveRelease(data)
    .then(response => {
      history.push('/releases');
      successMessage('Lançamento cadastrado com sucesso.')
    }).catch(erro => {
      errorMessage(erro.response.data)
    });
  }

  return (
    <div className="container">
      <Card title={isUpdate ? 'Atualização de Lançamento' : 'Cadastro de Lançamento'}>
        <div className="row">
          <div className="col-md-12">
            <FormGroup htmlFor="inputDescricao" label="Descrição: *" >
              <input
                id="inputDescricao"
                type="text"
                className="form-control"
                name="descricao"
                value={inputDescription}
                onChange={event => setInputDescription(event.target.value)}
              />
            </FormGroup>
          </div>
        </div>

        <div className="row">
          <div className="col-md-6">
            <FormGroup htmlFor="inputAno" label="Ano: *">
              <input
                id="inputAno"
                type="text"
                name="ano"
                value={inputYear}
                onChange={event => setInputYear(event.target.value)}
                className="form-control"
              />
            </FormGroup>
          </div>

          <div className="col-md-6">
            <FormGroup htmlFor="inputMes" label="Mês: *">
              <SelectMenu
                id="inputMes"
                value={inputMonth}
                onChange={event => setInputMonth(event.target.value)}
                list={monthList}
                name="mes"
                className="form-control"
              />
            </FormGroup>
          </div>
        </div>

        <div className="row">
          <div className="col-md-4">
            <FormGroup htmlFor="inputValor" label="Valor: *">
              <input
                id="inputValor"
                type="text"
                name="valor"
                value={inputValue}
                onChange={event => setInputValue(event.target.value)}
                className="form-control"
              />
            </FormGroup>
          </div>

          <div className="col-md-4">
            <FormGroup htmlFor="inputTipo" label="Tipo: *">
              <SelectMenu
                id="inputTipo"
                list={typeList}
                name="tipo"
                value={inputType}
                onChange={event => setInputType(event.target.value)}
                className="form-control"
              />
            </FormGroup>
          </div>

          <div className="col-md-4">
            <FormGroup htmlFor="inputStatus" label="Status: ">
              <input
                id="inputStatus"
                type="text"
                className="form-control"
                name="status"
                value={inputStatus}
                disabled
              />
            </FormGroup>
          </div>
        </div>

        <div className="row">
          <div className="col-md-6">
            {isUpdate ? (
              <button
                onClick={handlerUpdate}
                className="btn btn-info"
              >
                Atualizar
              </button>
            ) : (
              <button
                onClick={handlerSave}
                className="btn btn-success"
              >
                Salvar
              </button>
            )}
            <Link
              to="/releases"
              className="btn btn-warning">
              Cancelar
            </Link>
          </div>
        </div>
      </Card>
    </div>
  );
}