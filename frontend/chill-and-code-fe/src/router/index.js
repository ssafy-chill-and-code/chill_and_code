import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import SignupView from '../views/SignupView.vue'
import OAuthCallbackView from '../views/OAuthCallbackView.vue'
import MyPageView from '../views/MyPageView.vue'
import ScheduleView from '../views/ScheduleView.vue'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', name: 'home', component: HomeView },
    { path: '/login', name: 'login', component: LoginView },
    { path: '/signup', name: 'signup', component: SignupView },
    { path: '/oauth/callback', name: 'oauth-callback', component: OAuthCallbackView },
    { path: '/mypage', name: 'mypage', component: MyPageView },
    { path: '/schedule', name: 'schedule', component: ScheduleView },
    { path: '/posts', name: 'posts', component: () => import('../views/PostListView.vue') },
    { path: '/posts/create', name: 'post-create', component: () => import('../views/PostCreateView.vue') },
    { path: '/posts/:postId', name: 'post-detail', component: () => import('../views/PostDetailView.vue'), props: true },
    { path: '/posts/:postId/edit', name: 'post-edit', component: () => import('../views/PostEditView.vue'), props: true },
    { path: '/:pathMatch(.*)*', redirect: '/' },
  ],
})

export default router
