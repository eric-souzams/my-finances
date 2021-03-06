import { ApiService } from "./api";

import ErroValidation from '../exceptions/ErroValidation';

type FilterProps = {
  year: string;
  month?: string;
  type?: string;
  status?: string;
  description?: string;
  userId: number;
}

type SaveReleaseProps = {
  id: null,
  descricao: string,
  valor: string,
  mes: string,
  ano: string,
  tipo: string,
  status: null;
  usuario: string;
}

type UpdateReleaseProps = {
  id: null,
  descricao: string,
  valor: string,
  mes: string,
  ano: string,
  tipo: string,
  status: string;
  usuario: string;
}

export class ReleaseService extends ApiService {

  constructor() {
    super('/lancamentos');
  }

  valid(release: any) {
    const error = [] as any;

    if (!release.descricao) {
      error.push("Informe a Descrição.")
    }
    
    if (!release.ano) {
      error.push("Informe o Ano.")
    }

    if (!release.mes) {
      error.push("Informe o Mês.")
    }

    if (!release.valor) {
      error.push("Informe o Valor.")
    }

    if (!release.tipo) {
      error.push("Informe o Tipo.")
    }

    if (error && error.length > 0) {
      throw new ErroValidation(error);
    }
  }

  getMonthList() {
    return [
      { label: 'Selecione...', value: '' },
      { label: 'Janeiro', value: '1' },
      { label: 'Fevereiro', value: '2' },
      { label: 'Março', value: '3' },
      { label: 'Abril', value: '4' },
      { label: 'Maio', value: '5' },
      { label: 'Junho', value: '6' },
      { label: 'Julho', value: '7' },
      { label: 'Agosto', value: '8' },
      { label: 'Setembro', value: '9' },
      { label: 'Outubro', value: '10' },
      { label: 'Novembro', value: '11' },
      { label: 'Dezembro', value: '12' },
    ]
  }

  getTypeList() {
    return [
      { label: 'Selecione...', value: '' },
      { label: 'Despesa' , value : 'DESPESA' },
      { label: 'Receita' , value : 'RECEITA' }
    ]
  }

  getUserReleases(filter: FilterProps) {
    let params = `?ano=${filter.year}`;

    if (filter.month) {
      params = `${params}&mes=${filter.month}`
    }

    if (filter.type) {
      params = `${params}&tipo=${filter.type}`
    }

    if (filter.status) {
      params = `${params}&status=${filter.status}`
    }

    if (filter.description) {
      params = `${params}&descricao=${filter.description}`
    }

    if (filter.userId) {
      params = `${params}&usuario=${filter.userId}`
    }

    return this.get(params);
  }

  deleteRelease(id: number) {
    return this.delete(`/${id}`);
  }

  saveRelease(release: SaveReleaseProps) {
    return this.post('/', release);
  }

  getById(id:number) {
    return this.get(`/${id}`);
  }

  updateRelease(release: UpdateReleaseProps) {
    return this.put(`/${release.id}`, release);
  }

  updateStatus(id:number, status: string) {
    return this.put(`/${id}/atualizar-status`, { status });
  }

}