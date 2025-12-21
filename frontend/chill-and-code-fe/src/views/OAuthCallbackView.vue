<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import axios from '@/api/axios'

const router = useRouter()
const userStore = useUserStore()

onMounted(async () => {
  // Step A: URL에서 errorCode 확인
  const urlParams = new URLSearchParams(window.location.search)
  const errorCode = urlParams.get('errorCode')
  
  if (errorCode) {
    // 에러 코드별 메시지 매핑
    let errorMessage = 'OAuth 로그인에 실패했습니다.'
    if (errorCode === 'OAUTH_EMAIL_DUPLICATED') {
      errorMessage = '이미 가입된 이메일입니다. 일반 로그인으로 진행해주세요.'
    } else if (errorCode === 'OAUTH_USER_CREATE_FAILED') {
      errorMessage = 'OAuth 가입 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.'
    } else if (errorCode === 'UNAUTHORIZED') {
      errorMessage = 'OAuth 로그인이 취소되었거나 실패했습니다.'
    }
    
    alert(errorMessage)
    router.push('/login')
    return
  }
  
  // Step B: errorCode가 없으면 refresh 호출
  try {
    const response = await axios.post('/auth/refresh', null, { 
      withCredentials: true 
    })
    
    const accessToken = response.data.data.accessToken
    
    if (!accessToken) {
      throw new Error('Access Token을 받지 못했습니다.')
    }
    
    // sessionStorage에 Access Token 저장
    sessionStorage.setItem('accessToken', accessToken)
    userStore.accessToken = accessToken
    
    // 프로필 조회
    await userStore.fetchProfile()
    userStore.isLoggedIn = true
    
    // 홈으로 이동
    router.push('/')
    
  } catch (e) {
    console.error('OAuth 로그인 실패:', e)
    
    // refresh 실패 시 sessionStorage 정리
    sessionStorage.removeItem('accessToken')
    userStore.accessToken = null
    userStore.isLoggedIn = false
    
    alert('OAuth 로그인 처리 중 오류가 발생했습니다. 다시 시도해주세요.')
    router.push('/login')
  }
})
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50">
    <div class="text-center">
      <div class="inline-block animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-slate-800 mb-4"></div>
      <p class="text-gray-600">Google 로그인 처리 중...</p>
    </div>
  </div>
</template>

