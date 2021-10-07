import { defineStore } from 'pinia';
import { User } from '@/types';

export const useAuthStore = defineStore({
    id: 'auth',
    state: () => ({
        // _user: {} as User
        _user: {
            number: 0,
            id: 'admin',
            name: '관리자',
            department: '관리자',
            type: '내부',
            password: '',
            role: 'ADMIN',
            suspended: false,
            deleted: false,
            created: ''
        }
    }),
    getters: {
        user(): User {
            return this._user;
        },
        isAdmin(): boolean {
            return this._user.role === 'ADMIN';
        }
    },
    actions: {
        setUser(user: User) {
            this._user = Object.assign([], user);
        }
    }
});
