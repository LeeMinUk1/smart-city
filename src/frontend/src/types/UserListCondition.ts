export interface UserListCondition {
    suspendedOrDeleted: boolean;
    type: string;
    searchType: string;
    keyword: string;
    limit: number;
    offset: number;
}