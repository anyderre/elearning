import { Section } from '../../section/shared/section.model';

export class SubSection {
    constructor(
        public id: number,
        public name: string,
        public description: string,
        public selected: boolean | false,
        public section: Section | null,
        public allSections: Section[] | null,
    ) { }
}
