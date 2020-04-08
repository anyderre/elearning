import { Rol } from '../../role/shared/role.model';
import { Section } from 'src/app/section-admin/section/shared/section.model';
import { Courses } from 'src/app/courses/shared/courses.model';

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
        public agreeWithTerms: boolean,
        public bio: string,
        public country: string,
        public photoURL: string,
        public role: Rol | null,
        public section: Section | null,
        public sections: Section[] | null,
        public allRoles: Rol[] | null,
        public courses: Courses[] | null,
        public allCourses: Courses[] | null,
    ) { }
}
