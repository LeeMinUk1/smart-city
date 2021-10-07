export interface Page<T> {
    totalRecords: number;
    data: T[];
}

export interface PageEvent {
    page: number;
    rows: number;
}
