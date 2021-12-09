import { ApiService } from "./api";

type UserProps = {
	nome: string;
	email: string;
	senha: string;
	confirmarSenha: string;
}

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

	saveNewUser(user:object) {
		return this.post('', user);
	}

	

}