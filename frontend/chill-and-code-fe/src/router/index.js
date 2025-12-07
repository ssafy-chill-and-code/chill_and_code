import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue'),
    },
    {
      path: '/posts',
      name: 'posts',
      component: () => import('../views/PostListView.vue'),
    },
    {
      path: '/posts/create',
      name: 'post-create',
      component: () => import('../views/PostCreateView.vue'),
    },
    {
      path: '/posts/:postId',
      name: 'post-detail',
      component: () => import('../views/PostDetailView.vue'),
      props: true,
    },
    {
      path: '/posts/:postId/edit',
      name: 'post-edit',
      component: () => import('../views/PostEditView.vue'),
      props: true,
    },
  ],
})

export default router
