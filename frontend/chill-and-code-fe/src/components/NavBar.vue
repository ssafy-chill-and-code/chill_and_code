<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-light border-bottom py-3">
    <div class="container">
      <RouterLink class="navbar-brand d-flex align-items-center" to="/">
        <span class="me-2">🌴</span> Chill & Code
      </RouterLink>
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarSupportedContent"
        aria-controls="navbarSupportedContent"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <RouterLink class="nav-link" to="/schedule">스케줄</RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink class="nav-link" to="/posts">커뮤니티</RouterLink>
          </li>
          <li class="nav-item" v-if="isLoggedIn">
            <RouterLink class="nav-link" to="/mypage">마이페이지</RouterLink>
          </li>
        </ul>
        <div class="d-flex align-items-center gap-3">
          <span v-if="isLoggedIn" class="text-muted small d-flex align-items-center gap-1">
            <i class="bi bi-person-circle"></i>
            안녕하세요, {{ nickname }}님
          </span>
          <div v-if="!isLoggedIn" class="d-flex gap-2">
            <RouterLink class="btn btn-primary" to="/login">로그인</RouterLink>
            <RouterLink class="btn btn-primary" to="/signup">회원가입</RouterLink>
          </div>
          <div v-else class="d-flex gap-2">
            <button class="btn btn-outline-secondary" @click="onLogout">로그아웃</button>
          </div>
        </div>
      </div>
    </div>
  </nav>
  
</template>

<script setup>
import { computed } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const isLoggedIn = computed(() => userStore.isLoggedIn)
const nickname = computed(() => userStore?.user?.nickname || '회원')

const onLogout = async () => {
  try {
    await userStore.logout()
  } catch (e) {
    // noop
  }
}
</script>

<style scoped>
</style>
