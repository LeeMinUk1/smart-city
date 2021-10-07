import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import Login from "@/views/login/Login.vue";
import Home from "@/views/home/Home.vue";
import PowerUsage from "@/views/building/PowerUsage.vue";
import Dashboard from "@/views/building/dashboard/Dashboard.vue";
import UserList from "@/views/management/user/UserList.vue";
import LoginHistoryList from "@/views/management/login-history/LoginHistoryList.vue";
import NoticeList from "@/views/management/notice/NoticeList.vue";
import NoticeManagement from "@/views/management/notice/NoticeManagement.vue";
import QnaManagement from "@/views/management/qna/QnaManagement.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    component: Login
  },
  {
    path: '/home',
    component: Home,
    children: [
      {
        path: '/home/building/dashboard',
        component: Dashboard
      },
      {
        path: '/power-usage',
        component: PowerUsage
      },
      {
        path: '/user',
        component: UserList
      },
      {
        path: '/login-history',
        component: LoginHistoryList
      },
      {
        path: '/notice-list',
        component: NoticeList
      },
      {
        path: '/notice-management',
        component: NoticeManagement
      },
      {
        path: '/qna',
        component: QnaManagement
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
