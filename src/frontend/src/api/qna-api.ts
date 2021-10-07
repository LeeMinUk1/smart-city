import { AxiosResponse } from 'axios';
import http from './http';
import {Page, Qna, QnaListCondition} from "@/types";

export async function addOrUpdate(qna: Qna, mode: 'add' | 'update'): Promise<AxiosResponse> {
    const formData = new FormData();
    formData.append('title', qna.title);
    formData.append('content', qna.content);
    if (qna.attachment) {
        formData.append('attachment', qna.attachment);
    }

    return http.post('/api/qna/' + mode, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
}

export async function remove(id: number): Promise<AxiosResponse> {
    return http.post('/api/qna/delete', null, {params: {id}});
}

export async function list(condition: QnaListCondition): Promise<AxiosResponse<Page<Qna>>> {
    return http.post('/api/qna/list', condition);
}

export async function get(userId: string, qnaId: number): Promise<AxiosResponse<Qna>> {
    return http.get(`/api/qna/${userId}/${qnaId}`);
}

export async function getUnreadCount(userId: string): Promise<AxiosResponse<number>> {
    return http.get(`/api/qna/unreadCount`);
}
