import ErroValidation from "../exceptions/ErroValidation";
import { ApiService } from "./api";

export class UserService extends ApiService {

  constructor() {
    super('/usuarios');
  }

  auth(credentials: object) {
    return this.post('/autenticar', credentials);
  }

  getUserBalance(id: number) {
    return this.get(`/${id}/saldo`);
  }

  saveNewUser(user: object) {
    return this.post('', user);
  }

  valid(release: any) {
    const error = <any>[];

    if (!release.nome) {
      error.push('O campo Nome é obrigatório.');
    }

    if (!release.email) {
      error.push('O campo Email é obrigatório.');
    } else if (!release.email.match(/^[a-z0-9.]+@[a-z0-9]+\.[a-z]/)) {
      error.push('Informe um Email válido.')
    }

    if (!release.senha || !release.confirmarSenha) {
      error.push('Digite a senha 2 vezes.');
    } else if (release.senha !== release.confirmarSenha) {
      error.push('As senhas informadas não batem.')
    }

    if (error && error.length > 0) {
      throw new ErroValidation(error);
    }
  }

}