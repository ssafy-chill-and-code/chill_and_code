<script setup>
import { onMounted, computed } from 'vue'
import { RouterView, useRoute } from 'vue-router'
import NavBar from '@/components/NavBar.vue'
import { useUserStore } from '@/stores/user'
import { useThemeStore } from '@/stores/theme'

const route = useRoute()
const userStore = useUserStore()
const themeStore = useThemeStore() // 테마 스토어 초기화

// 로그인/회원가입 페이지에서는 NavBar 숨김
const showNavBar = computed(() => {
  return route.path !== '/login' && route.path !== '/signup'
})

onMounted(async () => {
  try {
    await userStore.fetchProfile()
  } catch (e) {
    // 미인증 등은 무시하고 진행
  }
})
</script>

<template>
  <div>
    <NavBar v-if="showNavBar" />
    <RouterView />
  </div>
</template>
