import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import SignupView from '../views/SignupView.vue'
import OAuthCallbackView from '../views/OAuthCallbackView.vue'
import MyPageView from '../views/MyPageView.vue'
import ScheduleView from '../views/ScheduleView.vue'
import ScheduleCreateView from '../views/ScheduleCreateView.vue'
import ScheduleEditView from '../views/ScheduleEditView.vue'
import ScheduleDetailView from '../views/ScheduleDetailView.vue'
import HomeView from '../views/HomeView.vue'
import RecommendationSelect from '../views/RecommendationSelect.vue'
import RecommendationResult from '../views/RecommendationResult.vue'
import RecommendationPlaceSelect from '../views/RecommendationPlaceSelect.vue'
import RecommendationPlaceResult from '../views/RecommendationPlaceResult.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', name: 'home', component: HomeView },
    { path: '/login', name: 'login', component: LoginView },
    { path: '/signup', name: 'signup', component: SignupView },
    { path: '/oauth/callback', name: 'oauth-callback', component: OAuthCallbackView },
    { path: '/mypage', name: 'mypage', component: MyPageView },
    { path: '/schedule', name: 'schedule', component: ScheduleView },
    { path: '/schedule/create', name: 'schedule-create', component: ScheduleCreateView },
    { path: '/schedule/:id', name: 'schedule-detail', component: ScheduleDetailView },
    { path: '/schedule/:id/edit', name: 'schedule-edit', component: ScheduleEditView },
    { path: '/posts', name: 'posts', component: () => import('../views/PostListView.vue') },
    { path: '/posts/create', name: 'post-create', component: () => import('../views/PostCreateView.vue') },
    { path: '/posts/:postId', name: 'post-detail', component: () => import('../views/PostDetailView.vue'), props: true },
    { path: '/posts/:postId/edit', name: 'post-edit', component: () => import('../views/PostEditView.vue'), props: true },
    { path: '/recommend', name: 'recommend-period-select', component: RecommendationSelect },
    { path: '/recommend/result', name: 'recommend-period-result', component: RecommendationResult },
    { path: '/recommend/place', name: 'recommend-place-select', component: RecommendationPlaceSelect },
    { path: '/recommend/place/result', name: 'recommend-place-result', component: RecommendationPlaceResult },
    { path: '/:pathMatch(.*)*', redirect: '/' },
  ],
})

export default router
