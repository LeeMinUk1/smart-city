import { AxiosResponse } from 'axios';
import http from './http';
import {Page, LoginHistory, LoginHistoryListCondition} from "@/types";

export async function list(condition: LoginHistoryListCondition): Promise<AxiosResponse<Page<LoginHistory>>> {
    return http.post('/api/login-history/list', condition);
}
