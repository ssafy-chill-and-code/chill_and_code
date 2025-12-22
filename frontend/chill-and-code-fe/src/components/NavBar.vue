<template>
  <nav class="fixed top-0 left-0 right-0 bg-white border-b border-gray-200 z-50">
    <div class="w-full px-4 lg:px-8">
      <div class="flex items-center justify-between h-16">
        <!-- 좌측: 로고 -->
        <RouterLink 
          to="/" 
          class="logo-link flex items-center gap-2 text-xl font-bold text-slate-800 hover:text-slate-900 transition-all duration-300"
        >
          <span class="tracking-tight">Chill & Code</span>
        </RouterLink>

        <!-- 중앙: 전역 메뉴 (데스크톱) -->
        <div class="hidden md:flex items-center gap-2">
          <RouterLink 
            to="/" 
            class="nav-link relative text-gray-700 hover:text-slate-900 font-medium transition-all duration-300 px-4 py-2 rounded-lg hover:bg-slate-50"
            active-class="text-slate-800 font-semibold"
          >
            홈
          </RouterLink>
          <RouterLink 
            to="/recommend" 
            class="nav-link relative text-gray-700 hover:text-slate-900 font-medium transition-all duration-300 px-4 py-2 rounded-lg hover:bg-slate-50"
            active-class="text-slate-800 font-semibold"
          >
            추천
          </RouterLink>
          <RouterLink 
            to="/schedule" 
            class="nav-link relative text-gray-700 hover:text-slate-900 font-medium transition-all duration-300 px-4 py-2 rounded-lg hover:bg-slate-50"
            active-class="text-slate-800 font-semibold"
          >
            일정
          </RouterLink>
          <RouterLink 
            to="/posts" 
            class="nav-link relative text-gray-700 hover:text-slate-900 font-medium transition-all duration-300 px-4 py-2 rounded-lg hover:bg-slate-50"
            active-class="text-slate-800 font-semibold"
          >
            커뮤니티
          </RouterLink>
        </div>

        <!-- 우측: 닉네임 + 로그아웃 (데스크톱) -->
        <div class="hidden md:flex items-center gap-3">
          <div v-if="isLoggedIn" class="flex items-center gap-3">
            <RouterLink 
              to="/mypage"
              class="profile-link flex items-center gap-2 text-gray-700 hover:text-slate-900 hover:bg-slate-50 transition-all duration-300 px-3 py-2 rounded-lg"
            >
              <div class="avatar w-8 h-8 rounded-full flex items-center justify-center text-white text-sm font-bold transition-transform duration-300 overflow-hidden flex-shrink-0">
                <img
                  v-if="profileImageUrl"
                  :src="profileImageUrl"
                  :alt="nickname"
                  class="w-full h-full object-cover"
                />
                <div
                  v-else
                  class="w-full h-full bg-gradient-to-br from-slate-700 to-slate-900 flex items-center justify-center"
                >
                  {{ nickname?.charAt(0) || 'U' }}
                </div>
              </div>
              <span class="text-sm font-medium">{{ nickname }}님</span>
            </RouterLink>
            <button
              @click="onLogout"
              class="logout-btn px-4 py-2 text-sm font-medium text-gray-700 hover:text-slate-900 border border-gray-300 rounded-lg hover:bg-gray-50 hover:border-gray-400 active:bg-gray-100 transition-all duration-300"
            >
              로그아웃
            </button>
          </div>
          <div v-else class="flex items-center gap-2">
            <RouterLink
              to="/login"
              class="login-btn px-4 py-2 text-sm font-medium text-gray-700 hover:text-slate-900 border border-gray-300 rounded-lg hover:bg-gray-50 hover:border-gray-400 active:bg-gray-100 transition-all duration-300 hover:scale-105"
            >
              로그인
            </RouterLink>
            <RouterLink
              to="/signup"
              class="signup-btn px-4 py-2 text-sm font-medium text-white bg-slate-800 rounded-lg hover:bg-slate-900 hover:shadow-lg transition-all duration-300 hover:scale-105 hover:-translate-y-0.5"
            >
              회원가입
            </RouterLink>
          </div>
        </div>

        <!-- 모바일: 햄버거 메뉴 버튼 -->
        <button
          @click="mobileMenuOpen = !mobileMenuOpen"
          class="hamburger-btn md:hidden p-2 rounded-lg hover:bg-slate-100 active:bg-slate-200 transition-all duration-300"
        >
          <svg class="w-6 h-6 text-gray-700 transition-transform duration-300" :class="{ 'rotate-90': mobileMenuOpen }" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"/>
          </svg>
        </button>
      </div>
    </div>

    <!-- 모바일 메뉴 -->
    <div 
      v-if="mobileMenuOpen"
      class="mobile-menu md:hidden border-t border-gray-200 bg-white animate-slideDown"
    >
      <div class="px-4 py-4 space-y-3">
        <!-- 로그인 상태 -->
        <div v-if="isLoggedIn" class="pb-3 border-b border-gray-200">
          <RouterLink 
            to="/mypage"
            class="flex items-center gap-3 py-2"
            @click="mobileMenuOpen = false"
          >
            <div class="w-10 h-10 rounded-full flex items-center justify-center text-white font-bold overflow-hidden flex-shrink-0">
              <img
                v-if="profileImageUrl"
                :src="profileImageUrl"
                :alt="nickname"
                class="w-full h-full object-cover"
              />
              <div
                v-else
                class="w-full h-full bg-gradient-to-br from-slate-700 to-slate-900 flex items-center justify-center"
              >
                {{ nickname?.charAt(0) || 'U' }}
              </div>
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
          class="mobile-nav-link block px-4 py-3 text-gray-700 hover:text-slate-900 hover:bg-slate-50 hover:pl-6 font-medium transition-all duration-300 rounded-lg"
          active-class="text-slate-800 font-semibold bg-slate-50"
          @click="mobileMenuOpen = false"
        >
          홈
        </RouterLink>
        <RouterLink 
          to="/recommend" 
          class="mobile-nav-link block px-4 py-3 text-gray-700 hover:text-slate-900 hover:bg-slate-50 hover:pl-6 font-medium transition-all duration-300 rounded-lg"
          active-class="text-slate-800 font-semibold bg-slate-50"
          @click="mobileMenuOpen = false"
        >
          추천
        </RouterLink>
        <RouterLink 
          to="/schedule" 
          class="mobile-nav-link block px-4 py-3 text-gray-700 hover:text-slate-900 hover:bg-slate-50 hover:pl-6 font-medium transition-all duration-300 rounded-lg"
          active-class="text-slate-800 font-semibold bg-slate-50"
          @click="mobileMenuOpen = false"
        >
          일정
        </RouterLink>
        <RouterLink 
          to="/posts" 
          class="mobile-nav-link block px-4 py-3 text-gray-700 hover:text-slate-900 hover:bg-slate-50 hover:pl-6 font-medium transition-all duration-300 rounded-lg"
          active-class="text-slate-800 font-semibold bg-slate-50"
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
              class="block w-full px-4 py-3 text-center text-white bg-slate-800 rounded-lg hover:bg-slate-900 hover:shadow-lg font-medium transition-all duration-300 hover:scale-105"
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
    class="fixed inset-0 bg-black bg-opacity-25 z-40 md:hidden transition-opacity duration-300"
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
const profileImageUrl = computed(() => userStore?.user?.profileImageUrl)

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
nav {
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(10px);
  box-shadow: 0 1px 3px 0 rgb(0 0 0 / 0.05);
  transition: box-shadow 0.3s ease;
}

nav:hover {
  box-shadow: 0 2px 6px 0 rgb(0 0 0 / 0.08);
}

/* 로고 호버 효과 */
.logo-link:hover {
  transform: translateY(-1px);
}

/* 메뉴 링크 밑줄 애니메이션 */
.nav-link::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 2px;
  background: linear-gradient(to right, #1e293b, #334155);
  transition: all 0.3s ease;
  transform: translateX(-50%);
}

.nav-link:hover::after {
  width: 70%;
}

.nav-link.router-link-active::after {
  width: 80%;
  background: linear-gradient(to right, #334155, #475569);
}

/* 프로필 링크 아바타 효과 */
.profile-link:hover .avatar {
  transform: scale(1.1) rotate(5deg);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

/* 버튼 호버 효과 */
.logout-btn:hover,
.login-btn:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.signup-btn {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.signup-btn:active {
  transform: scale(0.98) translateY(0) !important;
}

/* 햄버거 버튼 효과 */
.hamburger-btn:hover {
  transform: scale(1.05);
}

/* 모바일 메뉴 애니메이션 */
@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate-slideDown {
  animation: slideDown 0.3s ease-out forwards;
}

/* 모바일 메뉴 링크 효과 */
.mobile-nav-link {
  position: relative;
  overflow: hidden;
}

.mobile-nav-link::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  width: 3px;
  height: 0;
  background: linear-gradient(to bottom, #334155, #475569);
  transition: height 0.3s ease;
  transform: translateY(-50%);
}

.mobile-nav-link:hover::before {
  height: 60%;
}

.mobile-nav-link.router-link-active::before {
  height: 70%;
}
</style>
