import { Video } from './video.model';

export class Syllabus {
    constructor(
        public id: number,
        public chapterTitle: string,
        public chapterTuts: Video[] | null,
    ) { }
}
