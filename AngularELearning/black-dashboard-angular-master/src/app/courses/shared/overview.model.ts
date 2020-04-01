import { Feature } from './feature.model';
import { Requirement } from './requirement.model';

export class Overview {
    constructor(
        public id: number,
        public requirements: Requirement[] | null,
        public features: Feature[] | null,
    ) { }
}
