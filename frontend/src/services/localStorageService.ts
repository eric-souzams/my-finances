export class LocalStorageService {

	static addItem(key:string, value:string) {
		localStorage.setItem(key, JSON.stringify(value));
	}

	static getItem(key:string) {
		const localdata = localStorage.getItem(key);
		return JSON.parse(localdata || '{}');
	}

}