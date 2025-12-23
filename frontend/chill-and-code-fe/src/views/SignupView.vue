<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const email = ref('')
const password = ref('')
const nickname = ref('')
const region = ref('')
const loading = ref(false)
const error = ref(null)
const randomImage = ref('')

const userStore = useUserStore()
const router = useRouter()

// 랜덤 이미지 선택
const images = [
  'login_signup.jpg',
  'login_signup_1.jpg',
  'login_signup_2.jpg',
  'login_signup_3.jpg',
  'login_signup_4.jpg',
  'login_signup_5.jpg',
  'login_signup_6.jpg',
  'login_signup_7.jpg',
  'login_signup_8.jpg',
  'login_signup_9.jpg',
  'login_signup_10.jpg',
  'login_signup_11.jpg',
  'login_signup_12.jpg'
]

onMounted(() => {
  const randomIndex = Math.floor(Math.random() * images.length)
  randomImage.value = images[randomIndex]
})

// 유효성 검증
const validateEmail = (email) => {
  const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return re.test(email)
}

const validatePassword = (password) => {
  return password.length >= 8 && !/\s/.test(password)
}

const validateNickname = (nickname) => {
  return nickname.length >= 2 && nickname.length <= 10 && !/\s/.test(nickname)
}

// 회원가입
const onSignup = async () => {
  error.value = null

  // 유효성 검증
  if (!email.value || !password.value || !nickname.value || !region.value) {
    error.value = '모든 항목을 입력해주세요.'
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

  if (!validateNickname(nickname.value)) {
    error.value = '닉네임은 2~10자이며 공백이 없어야 합니다.'
    return
  }

  loading.value = true

  try {
    await userStore.signup({
      email: email.value,
      password: password.value,
      nickname: nickname.value,
      region: region.value,
    })
    alert('회원가입이 완료되었습니다. 로그인해주세요.')
    router.push('/login')
  } catch (e) {
    error.value = e.response?.data?.message || '회원가입에 실패했습니다. 입력 정보를 확인해주세요.'
  } finally {
    loading.value = false
  }
}

// 지역 목록
const regions = [
  '서울', '부산', '대구', '인천', '광주', '대전', '울산', '세종',
  '경기', '강원', '충북', '충남', '전북', '전남', '경북', '경남', '제주'
]
</script>

<template>
  <div class="min-h-screen flex bg-[#EEF3F5]">
    <!-- 좌측 영역: 이미지 영역 (PC만 표시) -->
    <div class="hidden lg:flex lg:w-1/2 p-8 flex-col justify-center items-center">
      <div v-if="randomImage" class="w-full h-full rounded-3xl overflow-hidden shadow-2xl">
        <img 
          :src="`/src/assets/login/${randomImage}`"
          alt="Workation" 
          class="w-full h-full object-cover"
        />
      </div>
    </div>

    <!-- 우측 영역: 폼 영역 -->
    <div class="w-full lg:w-1/2 flex items-center justify-center p-8 lg:p-12">
      <div class="w-full max-w-md">
        <!-- 뒤로가기 버튼 -->
        <RouterLink 
          to="/" 
          class="inline-flex items-center gap-2 text-gray-600 hover:text-slate-900 transition-colors mb-8 group"
        >
          <img src="@/assets/arrow/back_arrow.png" alt="뒤로 가기" class="w-6 h-6 transition-transform group-hover:-translate-x-1" />
        </RouterLink>

        <!-- 모바일 헤더 (모바일만 표시) -->
        <div class="lg:hidden text-center mb-8">
          <h1 class="text-3xl font-bold text-gray-800 mb-2">Chill & Code</h1>
          <p class="text-sm text-gray-600">워케이션 추천 플랫폼</p>
        </div>

        <!-- 회원가입 폼 카드 -->
        <div class="bg-white rounded-2xl shadow-2xl border border-gray-100 p-10 lg:p-12">
          <h2 class="text-3xl font-bold text-gray-900 mb-8">회원가입</h2>

          <!-- 에러 메시지 -->
          <div v-if="error" class="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">
            {{ error }}
          </div>

          <!-- 회원가입 폼 -->
          <form @submit.prevent="onSignup" class="space-y-6">
            <!-- 계정 정보 섹션 -->
            <div class="pb-6 border-b border-gray-200">
              <h3 class="text-sm font-bold text-gray-900 mb-4 uppercase tracking-wide">계정 정보</h3>
              
              <div class="space-y-5">
                <div>
                  <label for="email" class="block text-sm font-semibold text-gray-700 mb-2">
                    이메일 <span class="text-red-500">*</span>
                  </label>
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
                  <label for="password" class="block text-sm font-semibold text-gray-700 mb-2">
                    비밀번호 <span class="text-red-500">*</span>
                  </label>
                  <input
                    id="password"
                    v-model="password"
                    type="password"
                    placeholder="8자 이상, 공백 없음"
                    :disabled="loading"
                    class="w-full px-5 py-4 border border-gray-300 rounded-xl focus:ring-2 focus:ring-slate-500 focus:border-transparent disabled:bg-gray-50 disabled:cursor-not-allowed transition-all"
                  />
                  <p class="mt-2 text-xs text-gray-500">비밀번호는 8자 이상이며 공백이 없어야 합니다.</p>
                </div>
              </div>
            </div>

            <!-- 프로필 정보 섹션 -->
            <div class="pt-2">
              <h3 class="text-sm font-bold text-gray-900 mb-4 uppercase tracking-wide">프로필 정보</h3>
              
              <div class="space-y-5">
                <div>
                  <label for="nickname" class="block text-sm font-semibold text-gray-700 mb-2">
                    닉네임 <span class="text-red-500">*</span>
                  </label>
                  <input
                    id="nickname"
                    v-model="nickname"
                    type="text"
                    placeholder="2~10자, 공백 없음"
                    :disabled="loading"
                    class="w-full px-5 py-4 border border-gray-300 rounded-xl focus:ring-2 focus:ring-slate-500 focus:border-transparent disabled:bg-gray-50 disabled:cursor-not-allowed transition-all"
                  />
                </div>

                <div>
                  <label for="region" class="block text-sm font-semibold text-gray-700 mb-2">
                    지역 <span class="text-red-500">*</span>
                  </label>
                  <select
                    id="region"
                    v-model="region"
                    :disabled="loading"
                    class="w-full px-5 py-4 border border-gray-300 rounded-xl focus:ring-2 focus:ring-slate-500 focus:border-transparent disabled:bg-gray-50 disabled:cursor-not-allowed transition-all"
                  >
                    <option value="" disabled>지역을 선택하세요</option>
                    <option v-for="r in regions" :key="r" :value="r">{{ r }}</option>
                  </select>
                </div>
              </div>
            </div>

            <!-- 회원가입 버튼 -->
            <button
              type="submit"
              :disabled="loading"
              class="w-full bg-slate-800 text-white rounded-xl px-5 py-4 font-semibold hover:bg-slate-900 hover:shadow-lg disabled:bg-slate-300 disabled:cursor-not-allowed transition-all mt-8"
            >
              <span v-if="loading" class="flex items-center justify-center gap-2">
                <svg class="animate-spin h-5 w-5" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                가입 중...
              </span>
              <span v-else>회원가입</span>
            </button>
          </form>

          <!-- 로그인 링크 -->
          <div class="mt-8 text-center">
            <RouterLink to="/login" class="text-sm text-gray-600 hover:text-slate-900 font-medium transition-colors">
              이미 계정이 있으신가요? <span class="text-slate-800 font-semibold">로그인</span>
            </RouterLink>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
