<script setup>
import { RouterLink, RouterView, useRouter } from 'vue-router'
import { computed } from 'vue'
import { useUserStore } from './stores/user'

const userStore = useUserStore()
const isLoggedIn = computed(() => userStore.isLoggedIn)
const nickname = computed(() => userStore.user?.nickname || '')
const router = useRouter()

const onLogout = async () => {
  try {
    await userStore.logout()
    router.push('/login')
  } catch (e) {
    // no-op
  }
}
</script>

<template>
  <header style="padding: 8px 12px; border-bottom: 1px solid #ddd;">
    <nav style="display:flex; gap:12px; align-items:center; flex-wrap:wrap;">
      <RouterLink to="/schedule">스케줄</RouterLink>
      <RouterLink to="/schedule/create">생성</RouterLink>
      <RouterLink to="/mypage">마이페이지</RouterLink>
      <span style="margin-left:auto"></span>
      <template v-if="isLoggedIn">
        <span>안녕하세요, {{ nickname }}</span>
        <button @click="onLogout" style="padding:4px 8px;">로그아웃</button>
      </template>
      <template v-else>
        <RouterLink to="/login">로그인</RouterLink>
        <RouterLink to="/signup">회원가입</RouterLink>
      </template>
    </nav>
  </header>
  <main style="padding: 12px;">
    <RouterView />
  </main>
  
</template>

<style scoped>
/* 테스트 편의를 위한 최소 스타일만 유지 */
</style>
