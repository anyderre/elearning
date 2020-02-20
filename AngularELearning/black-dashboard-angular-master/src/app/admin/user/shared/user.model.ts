import { Rol } from './rol.model';

export class User {
    constructor(
        public id: number,
        public name: string,
        public username: string,
        public password: string,
        public passwordConfirm: string,
        public professor: boolean,
        public admin: boolean,
    ) { }
}
