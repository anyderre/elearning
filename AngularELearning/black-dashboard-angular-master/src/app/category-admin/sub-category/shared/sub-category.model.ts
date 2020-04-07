import { Category } from '../../category/shared/category.model';

export class SubCategory {
    constructor(
        public id: number,
        public name: string,
        public description: string,
        public selected: boolean | false,
        public subCategories: SubCategory[] | null,
        public category: Category | null,
        public allCategories: Category[] | null,
        public allSubCategories: SubCategory[] | null,
    ) { }
}
