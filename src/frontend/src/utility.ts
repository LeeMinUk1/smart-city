import {Page} from "@/types";

export function setRowNumbers<T>(page: Page<T>, limit: number, offset: number, key = 'number'): void {
    if (page && page.data) {
        let startNumber = page.totalRecords - offset;
        page.data.forEach((record: any) => {
            record[key] = startNumber--;
        });
    }
}
