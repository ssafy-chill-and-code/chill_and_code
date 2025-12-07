import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import SignupView from '../views/SignupView.vue'
import MyPageView from '../views/MyPageView.vue'
import ScheduleView from '../views/ScheduleView.vue'
import ScheduleCreateView from '../views/ScheduleCreateView.vue'
import ScheduleEditView from '../views/ScheduleEditView.vue'
import ScheduleDetailView from '../views/ScheduleDetailView.vue'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', name: 'home', component: HomeView },
    { path: '/login', name: 'login', component: LoginView },
    { path: '/signup', name: 'signup', component: SignupView },
    { path: '/mypage', name: 'mypage', component: MyPageView },
    { path: '/schedule', name: 'schedule', component: ScheduleView },
    { path: '/schedule/create', name: 'schedule-create', component: ScheduleCreateView },
    { path: '/schedule/:id', name: 'schedule-detail', component: ScheduleDetailView },
    { path: '/schedule/:id/edit', name: 'schedule-edit', component: ScheduleEditView },
    { path: '/:pathMatch(.*)*', redirect: '/' },
  ],
})

export default router
