import { ApiService } from "./api";
import jwt from 'jsonwebtoken';

import LocalStorageService from './localStorageService';

type User = {
  id: number;
  nome: string;
}

type TokenProps = {
  sub: string;
  exp: number;
  userId: number;
  userName: string;
}

type Token = {
  token: string;
}


export const USER_DATA = 'user_data';
export const TOKEN = 'access_token';

export default class AuthService extends ApiService {

  static isAuthenticatedUser(): boolean {
    const token:Token = LocalStorageService.getItem(TOKEN);
    
    if (token.token === undefined) {
      return false;
    }

    const decoder = jwt.decode(token.token) as TokenProps;
    const expiration = decoder?.exp;
    const isInvalidToken = Date.now() >= (expiration * 1000);

    return !isInvalidToken;
  }

  static logout(): void {
    LocalStorageService.removeItem(USER_DATA);
    LocalStorageService.removeItem(TOKEN);
  }

  static signIn(user: User, token: Token) {
    LocalStorageService.addItem(USER_DATA, user);
    LocalStorageService.addItem(TOKEN, token);
    AuthService.registerToken(token);
  }

  static getAuthenticatedUser() {
    return LocalStorageService.getItem(USER_DATA);
  }

  static refreshSession() {
    const token:Token = LocalStorageService.getItem(TOKEN);
    const user = AuthService.getAuthenticatedUser();
    
    this.signIn(user, token);
    
    return user;
  }

}