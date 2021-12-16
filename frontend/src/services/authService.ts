import { ApiService } from "./api";

import LocalStorageService from './localStorageService';

type User = {
    id: number;
    nome: string;
    email: string;
    dataCadastro: Array<number>;
}

export const USER_DATA = 'user_data';

export default class AuthService extends ApiService {

    static isAuthenticatedUser():boolean {
        const user = LocalStorageService.getItem(USER_DATA);

        return user && user.id;
    }

    static logout():void {
        LocalStorageService.removeItem(USER_DATA);
    }

    static signIn(user: User) {
        LocalStorageService.addItem(USER_DATA, user);
    }

    static getAuthenticatedUser() {
        return LocalStorageService.getItem(USER_DATA);
    }

}