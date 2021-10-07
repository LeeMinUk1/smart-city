export class User {
    constructor(
        public number: number = 0,
        public id: string = '',
        public name: string = '',
        public department: string = '',
        public type: string = '',
        public password: string = '',
        public role: string = '',
        public suspended: boolean = false,
        public deleted: boolean = false,
        public created: string = '',
) {
}
}
