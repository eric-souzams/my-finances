import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL;

export const api = axios.create({
  baseURL: API_URL,
  withCredentials: true
});

type TokenDTO = {
  token: string;
}
export class ApiService {
  apiurl: string;

  constructor(apiurl: string) {
    this.apiurl = apiurl;
  }

  static registerToken(token: TokenDTO) {
    if (token.token) {
      api.defaults.headers.common['Authorization'] = `Bearer ${token.token}`;
    }
  }

  post(url: string, object: object) {
    const requestUrl = `${this.apiurl}${url}`
    return api.post(requestUrl, object);
  }

  put(url: string, object: object) {
    const requestUrl = `${this.apiurl}${url}`
    return api.put(requestUrl, object);
  }

  get(url: string) {
    const requestUrl = `${this.apiurl}${url}`
    return api.get(requestUrl);
  }

  delete(url: string) {
    const requestUrl = `${this.apiurl}${url}`
    return api.delete(requestUrl);
  }
}