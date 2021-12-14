import { useState } from "react";
import { Link, useHistory } from "react-router-dom";

import { Card } from "../../components/Card";
import { FormGroup } from "../../components/FormGroup";
import { SelectMenu } from "../../components/SelectMenu";
import { ReleaseTable } from "./ReleaseTable";
import { errorMessage, successMessage } from '../../components/Toastr';
import { ConfirmModal } from '../../components/ConfirmModal';

import { ReleaseService } from "../../services/lancamentoService";
import { LocalStorageService } from "../../services/localStorageService";

type ReleaseProps = {
  id: number;
  descricao: string;
  valor: number;
  tipo: string;
  mes: number;
  status: string;
}

export function Releases() {
  const api = new ReleaseService();
  const history = useHistory();

  const monthList = api.getMonthList();
  const typeList = api.getTypeList();

  const [inputYear, setInputYear] = useState('');
  const [inputMonth, setInputMonth] = useState('');
  const [inputDescription, setInputDescription] = useState('');
  const [inputType, setInputType] = useState('');
  const [releases, setReleases] = useState<ReleaseProps[]>([]);

  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [deleteRelease, setDeleteRelease] = useState({} as ReleaseProps);


  async function handlerSearchRelease() {
    if (!inputYear) {
      errorMessage('O preenchimento do campo Ano é obrigatório.');
      return false;
    }

    const user = LocalStorageService.getItem('user_data');
    const filter = {
      year: inputYear,
      month: inputMonth,
      description: inputDescription,
      type: inputType,
      userId: user.id
    }

    await api.getUserReleases(filter)
      .then(response => {
        setReleases(response.data);
      }).catch(erro => {
        errorMessage('Ocorreu um erro ao tentar obter os Lançamento.');
      })
  }

  async function handlerDelete(release: ReleaseProps) {
    await api.deleteRelease(release.id)
      .then(response => {
        setReleases(releases.filter(rel => rel.id !== release.id));
        successMessage('Lancamento deletado com sucesso.');
      }).catch(erro => {
        errorMessage('Ocorreu um erro ao tentar deletar o Lançamento.');
      });
  }

  async function handlerEdit(id: number) {
    history.push(`/new-release/${id}`);
  }

  function handlerOpenModal(release: ReleaseProps) {
    setShowConfirmModal(true);
    setDeleteRelease(release);
  }

  async function handlerConfirm() {
    await handlerDelete(deleteRelease);
    setShowConfirmModal(false);
  }

  function handlerCancel() {
    setShowConfirmModal(false);
    setDeleteRelease({} as ReleaseProps);
  }

  return (
    <div className="container">
      <Card title="Consultar Lançamentos">
        <div className="row">
          <div className="col-md-6">
            <div className="bs-component">
              <FormGroup label="Ano: *" htmlFor="inputYear">
                <input type="text"
                  className="form-control"
                  id="inputYear"
                  value={inputYear}
                  onChange={event => { setInputYear(event.target.value) }}
                  placeholder="Digite o Ano"
                  maxLength={4}
                />
              </FormGroup>

              <FormGroup label="Mês: *" htmlFor="inputMonth">
                <SelectMenu
                  id="inputMonth"
                  value={inputMonth}
                  onChange={event => { setInputMonth(event.target.value) }}
                  className="form-control"
                  list={monthList}
                />
              </FormGroup>

              <FormGroup label="Descrição: *" htmlFor="inputDescription">
                <input type="text"
                  className="form-control"
                  id="inputDescription"
                  value={inputDescription}
                  onChange={event => { setInputDescription(event.target.value) }}
                  placeholder="Digite a Descrição"
                />
              </FormGroup>

              <FormGroup label="Tipo de Lançamento:" htmlFor="inputType">
                <SelectMenu
                  id="inputType"
                  value={inputType}
                  onChange={event => { setInputType(event.target.value) }}
                  className="form-control"
                  list={typeList}
                />
              </FormGroup>

              <button onClick={handlerSearchRelease}
                type="button"
                className="btn btn-light">
                <i className="pi pi-search"></i> Buscar
              </button>

              <Link
                to="/new-release"
                type="button"
                className="btn btn-secondary">
                <i className="pi pi-plus"></i> Cadastrar
              </Link>
            </div>
          </div>
        </div>
        <div className="row mt-4">
          <div className="col-md-12">
            <div className="bs-component">
              <ReleaseTable
                releases={releases}
                editAction={handlerEdit}
                deleteAction={handlerOpenModal}
              />
            </div>
          </div>
        </div>
        <div>
          {showConfirmModal &&
            <ConfirmModal
              header="Confirmação"
              message="Você quer deletar este lançamento?"
              handlerCancel={handlerCancel}
              handlerConfirm={handlerConfirm}
            />
          }
        </div>
      </Card>
    </div>
  );
}