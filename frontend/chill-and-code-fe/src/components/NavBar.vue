<template>
  <nav class="fixed top-0 left-0 right-0 bg-white border-b border-gray-200 z-50">
    <div class="w-full px-4 lg:px-8">
      <div class="flex items-center justify-between h-16">
        <!-- 좌측: 로고 -->
        <RouterLink 
          to="/" 
          class="flex items-center gap-2 text-xl font-bold hover:opacity-80 transition-opacity logo-text"
        >
          <span>Chill & Code</span>
        </RouterLink>

        <!-- 중앙: 전역 메뉴 (데스크톱) -->
        <div class="hidden md:flex items-center gap-2">
          <RouterLink 
            to="/" 
            class="text-gray-700 hover:text-teal-600 font-medium transition-all px-4 py-2"
            active-class="text-teal-600 font-semibold"
          >
            홈
          </RouterLink>
          <RouterLink 
            to="/recommend" 
            class="text-gray-700 hover:text-teal-600 font-medium transition-all px-4 py-2"
            active-class="text-teal-600 font-semibold"
          >
            추천
          </RouterLink>
          <RouterLink 
            to="/schedule" 
            class="text-gray-700 hover:text-teal-600 font-medium transition-all px-4 py-2"
            active-class="text-teal-600 font-semibold"
          >
            일정
          </RouterLink>
          <RouterLink 
            to="/posts" 
            class="text-gray-700 hover:text-teal-600 font-medium transition-all px-4 py-2"
            active-class="text-teal-600 font-semibold"
          >
            커뮤니티
          </RouterLink>
        </div>

        <!-- 우측: 닉네임 + 로그아웃 (데스크톱) -->
        <div class="hidden md:flex items-center gap-3">
          <div v-if="isLoggedIn" class="flex items-center gap-3">
            <RouterLink 
              to="/mypage"
              class="flex items-center gap-2 text-gray-700 hover:text-slate-900 hover:bg-slate-50 transition-all px-3 py-2 rounded-lg"
            >
              <div class="w-8 h-8 bg-gradient-to-br from-slate-700 to-slate-900 rounded-full flex items-center justify-center text-white text-sm font-bold">
                {{ nickname?.charAt(0) || 'U' }}
              </div>
              <span class="text-sm font-medium">{{ nickname }}님</span>
            </RouterLink>
            <button
              @click="onLogout"
              class="px-4 py-2 text-sm font-medium text-gray-700 hover:text-slate-900 border border-gray-300 rounded-lg hover:bg-gray-50 hover:border-gray-400 active:bg-gray-100 transition-all"
            >
              로그아웃
            </button>
          </div>
          <div v-else class="flex items-center gap-2">
            <RouterLink
              to="/login"
              class="px-4 py-2 text-sm font-medium text-gray-700 hover:text-slate-900 border border-gray-300 rounded-lg hover:bg-gray-50 hover:border-gray-400 active:bg-gray-100 transition-all"
            >
              로그인
            </RouterLink>
            <RouterLink
              to="/signup"
              class="px-4 py-2 text-sm font-medium text-white rounded-lg hover:shadow-md transition-all btn-teal"
            >
              회원가입
            </RouterLink>
          </div>
        </div>

        <!-- 모바일: 햄버거 메뉴 버튼 -->
        <button
          @click="mobileMenuOpen = !mobileMenuOpen"
          class="md:hidden p-2 rounded-lg hover:bg-slate-100 active:bg-slate-200 transition-all"
        >
          <svg class="w-6 h-6 text-gray-700" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"/>
          </svg>
        </button>
      </div>
    </div>

    <!-- 모바일 메뉴 -->
    <div 
      v-if="mobileMenuOpen"
      class="md:hidden border-t border-gray-200 bg-white"
    >
      <div class="px-4 py-4 space-y-3">
        <!-- 로그인 상태 -->
        <div v-if="isLoggedIn" class="pb-3 border-b border-gray-200">
          <RouterLink 
            to="/mypage"
            class="flex items-center gap-3 py-2"
            @click="mobileMenuOpen = false"
          >
            <div class="w-10 h-10 bg-gradient-to-br from-slate-700 to-slate-900 rounded-full flex items-center justify-center text-white font-bold">
              {{ nickname?.charAt(0) || 'U' }}
            </div>
            <div>
              <div class="text-sm font-semibold text-gray-900">{{ nickname }}님</div>
              <div class="text-xs text-gray-500">내 프로필 보기</div>
            </div>
          </RouterLink>
        </div>

        <!-- 메뉴 링크 -->
        <RouterLink 
          to="/" 
          class="block px-4 py-3 text-gray-700 hover:text-teal-600 font-medium transition-all"
          active-class="text-teal-600 font-semibold"
          @click="mobileMenuOpen = false"
        >
          홈
        </RouterLink>
        <RouterLink 
          to="/recommend" 
          class="block px-4 py-3 text-gray-700 hover:text-teal-600 font-medium transition-all"
          active-class="text-teal-600 font-semibold"
          @click="mobileMenuOpen = false"
        >
          추천
        </RouterLink>
        <RouterLink 
          to="/schedule" 
          class="block px-4 py-3 text-gray-700 hover:text-teal-600 font-medium transition-all"
          active-class="text-teal-600 font-semibold"
          @click="mobileMenuOpen = false"
        >
          일정
        </RouterLink>
        <RouterLink 
          to="/posts" 
          class="block px-4 py-3 text-gray-700 hover:text-teal-600 font-medium transition-all"
          active-class="text-teal-600 font-semibold"
          @click="mobileMenuOpen = false"
        >
          커뮤니티
        </RouterLink>

        <!-- 로그인/로그아웃 버튼 -->
        <div class="pt-3 border-t border-gray-200">
          <div v-if="isLoggedIn">
            <button
              @click="onLogout"
              class="w-full px-4 py-3 text-left text-gray-700 hover:bg-slate-50 hover:text-slate-900 active:bg-slate-100 rounded-lg font-medium transition-all"
            >
              로그아웃
            </button>
          </div>
          <div v-else class="space-y-2">
            <RouterLink
              to="/login"
              class="block w-full px-4 py-3 text-center text-gray-700 border border-gray-300 rounded-lg hover:bg-gray-50 hover:border-gray-400 active:bg-gray-100 font-medium transition-all"
              @click="mobileMenuOpen = false"
            >
              로그인
            </RouterLink>
            <RouterLink
              to="/signup"
              class="block w-full px-4 py-3 text-center text-white rounded-lg hover:shadow-md font-medium transition-all btn-teal"
              @click="mobileMenuOpen = false"
            >
              회원가입
            </RouterLink>
          </div>
        </div>
      </div>
    </div>
  </nav>

  <!-- 오버레이 (모바일 메뉴 열렸을 때) -->
  <div 
    v-if="mobileMenuOpen"
    class="fixed inset-0 bg-black bg-opacity-25 z-40 md:hidden"
    @click="mobileMenuOpen = false"
  ></div>

  <!-- 상단바 높이만큼 여백 -->
  <div class="h-16"></div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const mobileMenuOpen = ref(false)

const isLoggedIn = computed(() => userStore.isLoggedIn)
const nickname = computed(() => userStore?.user?.nickname || '회원')

const onLogout = async () => {
  try {
    await userStore.logout()
    mobileMenuOpen.value = false
    router.push('/')
  } catch (e) {
    console.error('로그아웃 실패:', e)
  }
}
</script>

<style scoped>
.logo-text {
  color: #17a2b8;
  font-weight: 700;
}

nav {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
}

.btn-teal {
  background: #17a2b8;
}

.btn-teal:hover {
  background: #138496;
}
</style>
