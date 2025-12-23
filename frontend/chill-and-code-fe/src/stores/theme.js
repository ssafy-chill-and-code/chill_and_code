import { ref, watch } from 'vue'
import { defineStore } from 'pinia'

export const useThemeStore = defineStore('theme', () => {
  // 로컬스토리지에서 초기값 가져오기
  const isDarkMode = ref(localStorage.getItem('darkMode') === 'true')

  // 다크모드 토글
  const toggleDarkMode = () => {
    isDarkMode.value = !isDarkMode.value
  }

  // 다크모드 상태 변경 시 로컬스토리지에 저장 및 HTML에 클래스 적용
  watch(isDarkMode, (newValue) => {
    localStorage.setItem('darkMode', newValue)
    if (newValue) {
      document.documentElement.classList.add('dark')
    } else {
      document.documentElement.classList.remove('dark')
    }
  }, { immediate: true })

  return {
    isDarkMode,
    toggleDarkMode
  }
})

