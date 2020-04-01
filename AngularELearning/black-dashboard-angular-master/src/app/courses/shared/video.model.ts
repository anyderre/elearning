
export class Video {
    constructor(
        public id: number,
        public videoTitle: string,
        public videoURL: string,
        public updating: boolean,
        public selectedFile: File
    ) { }
}
