import { Section } from '../../section/shared/section.model';
import { Syllabus } from './syllabus.model';
import { User } from '../../admin/user/shared/user.model';
import { Overview } from './overview.model';
import { SubCategory } from '../../category-admin/sub-category/shared/sub-category.model';
import { Category } from '../../category-admin/category/shared/category.model';

export class Courses {
    constructor(
        public id: number,
        public title: string,
        public description: string,
        public imageUrl: string, // **
        public price: number,
        public ratings: number, // **
        public enrolled: number, // **
        public premium: boolean,
        public author: string,
        public startDate: Date,
        public endDate: Date,
        public section: Section | null,
        public syllabus: Syllabus[] | null,
        public category: Category | null,
        public subCategory: SubCategory | null,
        public user: User | null,
        public overview: Overview | null,
        public objectives: string[] | null,
        public categories: Category[] | null,
        public sections: Section[] | null,
        public users: User[] | null,
    ) { }
}
