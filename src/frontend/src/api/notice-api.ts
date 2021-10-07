import { AxiosResponse } from 'axios';
import http from './http';
import {Page, Notice, NoticeListCondition} from "@/types";

export async function addOrUpdate(notice: Notice, mode: 'add' | 'update'): Promise<AxiosResponse> {
    const formData = new FormData();
    formData.append('title', notice.title);
    formData.append('content', notice.content);
    if (notice.attachment) {
        formData.append('attachment', notice.attachment);
    }

    return http.post('/api/notice/' + mode, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
}

export async function remove(id: number): Promise<AxiosResponse> {
    return http.post('/api/notice/delete', null, {params: {id}});
}

export async function list(condition: NoticeListCondition): Promise<AxiosResponse<Page<Notice>>> {
    return http.post('/api/notice/list', condition);
}

export async function get(id: number): Promise<AxiosResponse<Notice>> {
    return http.get(`/api/notice/${id}`);
}
