<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const email = ref('')
const password = ref('')
const loading = ref(false)
const error = ref(null)

const userStore = useUserStore()
const router = useRouter()

// 유효성 검증
const validateEmail = (email) => {
  const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return re.test(email)
}

const validatePassword = (password) => {
  return password.length >= 8 && !/\s/.test(password)
}

// Google 로그인
const onGoogleLogin = () => {
  // 백엔드 OAuth2 엔드포인트로 리다이렉트
  window.location.href = 'http://localhost:8080/oauth2/authorization/google'
}

// 이메일/비밀번호 로그인
const onLogin = async () => {
  error.value = null

  // 유효성 검증
  if (!email.value || !password.value) {
    error.value = '이메일과 비밀번호를 입력해주세요.'
    return
  }

  if (!validateEmail(email.value)) {
    error.value = '올바른 이메일 형식이 아닙니다.'
    return
  }

  if (!validatePassword(password.value)) {
    error.value = '비밀번호는 8자 이상이며 공백이 없어야 합니다.'
    return
  }

  loading.value = true

  try {
    await userStore.login({ email: email.value, password: password.value })
    // 로그인 성공 시 홈으로 이동
    router.push('/')
  } catch (e) {
    error.value = e.response?.data?.message || '로그인에 실패했습니다. 이메일과 비밀번호를 확인해주세요.'
    console.error('로그인 에러:', e)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="min-h-screen flex">
    <!-- 좌측 영역: 브랜딩 영역 (PC만 표시) -->
    <div class="hidden lg:flex lg:w-1/2 bg-gradient-to-br from-slate-800 via-slate-700 to-slate-900 text-white p-12 flex-col justify-center items-center">
      <div class="text-center space-y-4 opacity-90">
        <h1 class="text-6xl font-light tracking-tight">Chill & Code</h1>
        <p class="text-lg text-slate-300 font-light">Work. Rest. Balance.</p>
      </div>
    </div>

    <!-- 우측 영역: 폼 영역 -->
    <div class="w-full lg:w-1/2 flex items-center justify-center p-8 lg:p-12 bg-white">
      <div class="w-full max-w-md">
        <!-- 뒤로가기 버튼 -->
        <RouterLink 
          to="/" 
          class="inline-flex items-center gap-2 text-gray-600 hover:text-slate-900 transition-colors mb-8 group"
        >
          <svg class="w-5 h-5 transition-transform group-hover:-translate-x-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"/>
          </svg>
          <span class="text-sm font-medium">홈으로 돌아가기</span>
        </RouterLink>

        <!-- 모바일 헤더 (모바일만 표시) -->
        <div class="lg:hidden text-center mb-8">
          <h1 class="text-3xl font-bold text-gray-800 mb-2">Chill & Code</h1>
          <p class="text-sm text-gray-600">워케이션 추천 플랫폼</p>
        </div>

        <!-- 로그인 폼 카드 -->
        <div class="bg-white rounded-2xl shadow-2xl border border-gray-100 p-10 lg:p-12">
          <h2 class="text-3xl font-bold text-gray-900 mb-8">로그인</h2>

          <!-- Google 로그인 버튼 -->
          <button
            @click="onGoogleLogin"
            class="w-full flex items-center justify-center gap-3 bg-white border-2 border-gray-300 rounded-xl px-5 py-4 font-medium text-gray-700 hover:bg-gray-50 hover:border-gray-400 hover:shadow-md transition-all mb-8"
          >
            <svg class="w-5 h-5" viewBox="0 0 24 24">
              <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
              <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
              <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"/>
              <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"/>
            </svg>
            Google로 로그인
          </button>

          <!-- 구분선 -->
          <div class="relative mb-8">
            <div class="absolute inset-0 flex items-center">
              <div class="w-full border-t border-gray-200"></div>
            </div>
            <div class="relative flex justify-center text-sm">
              <span class="px-6 bg-white text-gray-400 font-light">또는</span>
            </div>
          </div>

          <!-- 에러 메시지 -->
          <div v-if="error" class="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">
            {{ error }}
          </div>

          <!-- 이메일/비밀번호 폼 -->
          <form @submit.prevent="onLogin" class="space-y-5">
            <div>
              <label for="email" class="block text-sm font-semibold text-gray-700 mb-2">이메일</label>
              <input
                id="email"
                v-model="email"
                type="email"
                placeholder="you@example.com"
                :disabled="loading"
                class="w-full px-5 py-4 border border-gray-300 rounded-xl focus:ring-2 focus:ring-slate-500 focus:border-transparent disabled:bg-gray-50 disabled:cursor-not-allowed transition-all"
              />
            </div>

            <div>
              <label for="password" class="block text-sm font-semibold text-gray-700 mb-2">비밀번호</label>
              <input
                id="password"
                v-model="password"
                type="password"
                placeholder="••••••••"
                :disabled="loading"
                class="w-full px-5 py-4 border border-gray-300 rounded-xl focus:ring-2 focus:ring-slate-500 focus:border-transparent disabled:bg-gray-50 disabled:cursor-not-allowed transition-all"
              />
            </div>

            <button
              type="submit"
              :disabled="loading"
              class="w-full bg-slate-800 text-white rounded-xl px-5 py-4 font-semibold hover:bg-slate-900 hover:shadow-lg disabled:bg-slate-300 disabled:cursor-not-allowed transition-all mt-6"
            >
              <span v-if="loading" class="flex items-center justify-center gap-2">
                <svg class="animate-spin h-5 w-5" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                로그인 중...
              </span>
              <span v-else>로그인</span>
            </button>
          </form>

          <!-- 회원가입 링크 -->
          <div class="mt-8 text-center">
            <RouterLink to="/signup" class="text-sm text-gray-600 hover:text-slate-900 font-medium transition-colors">
              아직 계정이 없으신가요? <span class="text-slate-800 font-semibold">회원가입</span>
            </RouterLink>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
