export class Category {
    constructor(
        public id: number,
        public name: string,
        public description: string,
        public parentCategory: Category | null,
        public categories: Category[] | null,
        public selected: boolean | false,
    ) { }
}
