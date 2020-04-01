import { Rol } from '../../role/shared/role.model';

export class User {
    constructor(
        public id: number,
        public name: string,
        public firstName: string,
        public lastName: string,
        public email: string,
        public username: string,
        public password: string,
        public passwordConfirm: string,
        public role: Rol | null,
        public allRoles: Rol[] | null,
        public agreeWithTerms: boolean,
    ) { }
}
