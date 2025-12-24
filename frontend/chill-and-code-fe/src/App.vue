<script setup>
import { onMounted, computed, watch } from 'vue'
import { RouterView, useRoute } from 'vue-router'
import NavBar from '@/components/NavBar.vue'
import { useUserStore } from '@/stores/user'
import { useThemeStore } from '@/stores/theme'

const route = useRoute()
const userStore = useUserStore()
const themeStore = useThemeStore() // 테마 스토어 초기화

// 다크모드 상태
const isDarkMode = computed(() => themeStore.isDarkMode)

// 로그인/회원가입 페이지에서는 NavBar 숨김
const showNavBar = computed(() => {
  return route.path !== '/login' && route.path !== '/signup'
})

// 다크모드 변경 시 body 클래스 업데이트
watch(isDarkMode, (newValue) => {
  if (newValue) {
    document.documentElement.classList.add('dark')
    document.body.style.backgroundColor = '#0f172a'
  } else {
    document.documentElement.classList.remove('dark')
    document.body.style.backgroundColor = '#f0f4f8'
  }
}, { immediate: true })

onMounted(async () => {
  try {
    await userStore.fetchProfile()
  } catch (e) {
    // 미인증 등은 무시하고 진행
  }
  
  // 초기 다크모드 상태 적용
  if (isDarkMode.value) {
    document.documentElement.classList.add('dark')
    document.body.style.backgroundColor = '#0f172a'
  } else {
    document.documentElement.classList.remove('dark')
    document.body.style.backgroundColor = '#f0f4f8'
  }
})
</script>

<template>
  <div 
    :class="[
      'min-h-screen transition-colors duration-300',
      isDarkMode ? 'dark' : ''
    ]"
  >
    <NavBar v-if="showNavBar" />
    <RouterView />
  </div>
</template>
