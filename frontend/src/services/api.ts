import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL;

export const api = axios.create({
  baseURL: API_URL
});

export class ApiService {
  apiurl: string;

  constructor(apiurl: string) {
    this.apiurl = apiurl;
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