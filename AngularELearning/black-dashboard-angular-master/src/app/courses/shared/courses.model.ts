import { Category } from '../../category/shared/category.model';
import { Section } from '../../section/shared/section.model';
import { Syllabus } from './syllabus.model';
import { User } from 'src/app/login/shared/user.model';

export class Courses {
    constructor(
        public id: number,
        public title: string,
        public price: number,
        public description: string,
        public premium: boolean,
        public startDate: Date,
        public endDate: Date,
        public syllabus: Syllabus[] | null,
        public category: Category | null,
        public section: Section | null,
        public user: User | null,
        public users: User[] | null,
        public sections: Section[] | null,
        public categories: Category[] | null,
    ) { }
}
