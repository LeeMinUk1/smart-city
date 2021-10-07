export class Notice {
    constructor(
        public id: number = 0,
        public title: string = '',
        public registered: string = '',
        public readCount: number = 0,
        public content: string = '',
        public attachment?: File,
        public attachmentName?: string,
        public attachmentUrl?: string) {
    }
}