export default class LocalStorageService {

	static addItem(key:string, value:object) {
		localStorage.setItem(key, JSON.stringify(value));
	}

	static getItem(key:string) {
		const localdata = localStorage.getItem(key);
		return JSON.parse(localdata || '{}');
	}

	static removeItem(key:string) {
		localStorage.removeItem(key);
	}

}