import { AxiosResponse } from 'axios';
import http from './http';
import {User, Page, UserListCondition} from "@/types";

export async function addOrUpdate(user: User, mode: 'add' | 'update'): Promise<AxiosResponse> {
    return http.post('/api/user/' + mode, user);
}

export async function remove(id: string): Promise<AxiosResponse> {
    return http.post('/api/user/delete', null, {params: {id}});
}

export async function suspend(id: string): Promise<AxiosResponse> {
    return http.post('/api/user/suspend', null, {params: {id}});
}

export async function unsuspend(id: string): Promise<AxiosResponse> {
    return http.post('/api/user/unsuspend', null, {params: {id}});
}

export async function list(condition: UserListCondition): Promise<AxiosResponse<Page<User>>> {
    return http.post('/api/user/list', condition);
}

export async function login(id: string, password: string): Promise<AxiosResponse> {
    return http.post('/api/user/login', {id, password});
}
