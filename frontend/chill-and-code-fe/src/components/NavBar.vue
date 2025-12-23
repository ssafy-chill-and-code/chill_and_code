<template>
  <nav 
    :class="[
      'fixed top-0 left-0 right-0 z-[1000] transition-colors duration-300',
      isDarkMode ? 'dark' : ''
    ]"
  >
    <div class="w-full px-4 lg:px-8">
      <div class="flex items-center justify-between h-16 relative">
        <!-- 좌측: 로고 -->
        <RouterLink 
          to="/" 
          class="logo-link flex items-center gap-2 transition-all duration-300"
        >
          <img 
            src="@/assets/logo/chillandcode_logo_.png" 
            alt="Chill & Code" 
            class="h-10 w-auto"
          />
        </RouterLink>

        <!-- 중앙: 전역 메뉴 (데스크톱) - 절대 위치로 중앙 정렬 -->
        <div class="hidden md:flex items-center gap-2 absolute left-1/2 transform -translate-x-1/2">
          <RouterLink 
            to="/" 
            :class="[
              'nav-link relative font-medium transition-all duration-300 px-4 py-2 rounded-lg',
              isHomePage 
                ? 'text-white hover:text-gray-200 hover:bg-white/10' 
                : isDarkMode
                  ? 'text-gray-200 hover:text-white hover:bg-white/10'
                  : 'text-gray-900 hover:text-gray-700 hover:bg-black/5'
            ]"
          >
            홈
          </RouterLink>
          <RouterLink 
            to="/recommend" 
            :class="[
              'nav-link relative font-medium transition-all duration-300 px-4 py-2 rounded-lg',
              isHomePage 
                ? 'text-white hover:text-gray-200 hover:bg-white/10' 
                : isDarkMode
                  ? 'text-gray-200 hover:text-white hover:bg-white/10'
                  : 'text-gray-900 hover:text-gray-700 hover:bg-black/5'
            ]"
          >
            추천
          </RouterLink>
          <RouterLink 
            to="/schedule" 
            :class="[
              'nav-link relative font-medium transition-all duration-300 px-4 py-2 rounded-lg',
              isHomePage 
                ? 'text-white hover:text-gray-200 hover:bg-white/10' 
                : isDarkMode
                  ? 'text-gray-200 hover:text-white hover:bg-white/10'
                  : 'text-gray-900 hover:text-gray-700 hover:bg-black/5'
            ]"
          >
            일정
          </RouterLink>
          <RouterLink 
            to="/posts" 
            :class="[
              'nav-link relative font-medium transition-all duration-300 px-4 py-2 rounded-lg',
              isHomePage 
                ? 'text-white hover:text-gray-200 hover:bg-white/10' 
                : isDarkMode
                  ? 'text-gray-200 hover:text-white hover:bg-white/10'
                  : 'text-gray-900 hover:text-gray-700 hover:bg-black/5'
            ]"
          >
            커뮤니티
          </RouterLink>
        </div>

        <!-- 우측: 다크모드 + 닉네임 + 로그아웃 (데스크톱) -->
        <div class="hidden md:flex items-center gap-3">
          <!-- 다크모드 토글 버튼 -->
          <button
            @click="toggleTheme"
            :class="[
              'theme-toggle-btn glass-button-icon p-2 rounded-lg transition-all duration-300',
              isHomePage 
                ? 'hover:bg-white/10 active:bg-white/20' 
                : ''
            ]"
            :title="themeStore.isDarkMode ? '라이트 모드로 전환' : '다크 모드로 전환'"
          >
            <!-- 태양 아이콘 (라이트 모드) -->
            <svg 
              v-if="!themeStore.isDarkMode" 
              :class="[
                'w-5 h-5 transition-colors',
                isHomePage 
                  ? 'text-white' 
                  : isDarkMode 
                    ? 'text-gray-200' 
                    : 'text-gray-900'
              ]" 
              fill="none" 
              stroke="currentColor" 
              viewBox="0 0 24 24"
            >
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z" />
            </svg>
            <!-- 달 아이콘 (다크 모드) -->
            <svg 
              v-else 
              :class="[
                'w-5 h-5 transition-colors',
                isHomePage 
                  ? 'text-white' 
                  : isDarkMode 
                    ? 'text-gray-200' 
                    : 'text-gray-900'
              ]" 
              fill="none" 
              stroke="currentColor" 
              viewBox="0 0 24 24"
            >
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z" />
            </svg>
          </button>
          <div v-if="isLoggedIn" class="flex items-center gap-3">
            <RouterLink 
              to="/mypage"
              :class="[
                'profile-link flex items-center gap-2 transition-all duration-300 px-3 py-2 rounded-lg',
                isHomePage 
                  ? 'text-white hover:text-gray-200 hover:bg-white/10' 
                  : isDarkMode
                    ? 'text-gray-200 hover:text-white hover:bg-white/10'
                    : 'text-gray-900 hover:text-gray-700 hover:bg-black/5'
              ]"
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
              :class="[
                'logout-btn px-4 py-2 text-sm font-medium border rounded-lg transition-all duration-300',
                isHomePage 
                  ? 'text-white hover:text-gray-200 border-white hover:bg-white/10 hover:border-gray-200 active:bg-white/20' 
                  : isDarkMode
                    ? 'text-gray-200 hover:text-white border-gray-600 hover:bg-white/10 hover:border-gray-500 active:bg-white/20'
                    : 'text-gray-900 hover:text-gray-700 border-gray-900 hover:bg-black/5 hover:border-gray-700 active:bg-black/10'
              ]"
            >
              로그아웃
            </button>
          </div>
          <div v-else class="flex items-center gap-2">
            <RouterLink
              to="/login"
              :class="[
                'login-btn px-4 py-2 text-sm font-medium border rounded-lg transition-all duration-300 hover:scale-105',
                isHomePage 
                  ? 'text-white hover:text-gray-200 border-white hover:bg-white/10 hover:border-gray-200 active:bg-white/20' 
                  : isDarkMode
                    ? 'text-gray-200 hover:text-white border-gray-600 hover:bg-white/10 hover:border-gray-500 active:bg-white/20'
                    : 'text-gray-900 hover:text-gray-700 border-gray-900 hover:bg-black/5 hover:border-gray-700 active:bg-black/10'
              ]"
            >
              로그인
            </RouterLink>
            <RouterLink
              to="/signup"
              :class="[
                'signup-btn px-4 py-2 text-sm font-medium rounded-lg hover:shadow-lg transition-all duration-300 hover:scale-105 hover:-translate-y-0.5',
                isHomePage 
                  ? 'text-gray-900 bg-white hover:bg-gray-100' 
                  : isDarkMode
                    ? 'text-white bg-slate-700 hover:bg-slate-600'
                    : 'text-white bg-gray-900 hover:bg-gray-800'
              ]"
            >
              회원가입
            </RouterLink>
          </div>
        </div>

        <!-- 모바일: 햄버거 메뉴 버튼 -->
        <button
          @click="mobileMenuOpen = !mobileMenuOpen"
          :class="[
            'hamburger-btn md:hidden p-2 rounded-lg transition-all duration-300',
            isHomePage 
              ? 'hover:bg-white/10 active:bg-white/20' 
              : isDarkMode
                ? 'hover:bg-white/10 active:bg-white/20'
                : 'hover:bg-black/5 active:bg-black/10'
          ]"
        >
          <svg 
            :class="[
              'w-6 h-6 transition-transform duration-300',
              { 'rotate-90': mobileMenuOpen },
              isHomePage 
                ? 'text-white' 
                : isDarkMode 
                  ? 'text-gray-200' 
                  : 'text-gray-900'
            ]" 
            fill="none" 
            stroke="currentColor" 
            viewBox="0 0 24 24"
          >
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"/>
          </svg>
        </button>
      </div>
    </div>

    <!-- 모바일 메뉴 -->
    <div 
      v-if="mobileMenuOpen"
      :class="[
        'mobile-menu md:hidden border-t animate-slideDown transition-colors',
        isDarkMode 
          ? 'border-gray-700 bg-slate-900/95 backdrop-blur-xl' 
          : 'border-gray-200 bg-white'
      ]"
    >
      <div class="px-4 py-4 space-y-3">
        <!-- 로그인 상태 -->
        <div 
          v-if="isLoggedIn" 
          :class="[
            'pb-3 border-b transition-colors',
            isDarkMode ? 'border-gray-700' : 'border-gray-200'
          ]"
        >
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
              <div 
                :class="[
                  'text-sm font-semibold transition-colors',
                  isDarkMode ? 'text-white' : 'text-gray-900'
                ]"
              >
                {{ nickname }}님
              </div>
              <div 
                :class="[
                  'text-xs transition-colors',
                  isDarkMode ? 'text-gray-400' : 'text-gray-500'
                ]"
              >
                내 프로필 보기
              </div>
            </div>
          </RouterLink>
        </div>

        <!-- 메뉴 링크 -->
        <RouterLink 
          to="/" 
          :class="[
            'mobile-nav-link block px-4 py-3 hover:pl-6 font-medium transition-all duration-300 rounded-lg',
            isDarkMode
              ? 'text-gray-200 hover:text-white hover:bg-white/10'
              : 'text-gray-700 hover:text-slate-900 hover:bg-slate-50'
          ]"
          :active-class="isDarkMode ? 'text-white font-semibold bg-white/10' : 'text-slate-800 font-semibold bg-slate-50'"
          @click="mobileMenuOpen = false"
        >
          홈
        </RouterLink>
        <RouterLink 
          to="/recommend" 
          :class="[
            'mobile-nav-link block px-4 py-3 hover:pl-6 font-medium transition-all duration-300 rounded-lg',
            isDarkMode
              ? 'text-gray-200 hover:text-white hover:bg-white/10'
              : 'text-gray-700 hover:text-slate-900 hover:bg-slate-50'
          ]"
          :active-class="isDarkMode ? 'text-white font-semibold bg-white/10' : 'text-slate-800 font-semibold bg-slate-50'"
          @click="mobileMenuOpen = false"
        >
          추천
        </RouterLink>
        <RouterLink 
          to="/schedule" 
          :class="[
            'mobile-nav-link block px-4 py-3 hover:pl-6 font-medium transition-all duration-300 rounded-lg',
            isDarkMode
              ? 'text-gray-200 hover:text-white hover:bg-white/10'
              : 'text-gray-700 hover:text-slate-900 hover:bg-slate-50'
          ]"
          :active-class="isDarkMode ? 'text-white font-semibold bg-white/10' : 'text-slate-800 font-semibold bg-slate-50'"
          @click="mobileMenuOpen = false"
        >
          일정
        </RouterLink>
        <RouterLink 
          to="/posts" 
          :class="[
            'mobile-nav-link block px-4 py-3 hover:pl-6 font-medium transition-all duration-300 rounded-lg',
            isDarkMode
              ? 'text-gray-200 hover:text-white hover:bg-white/10'
              : 'text-gray-700 hover:text-slate-900 hover:bg-slate-50'
          ]"
          :active-class="isDarkMode ? 'text-white font-semibold bg-white/10' : 'text-slate-800 font-semibold bg-slate-50'"
          @click="mobileMenuOpen = false"
        >
          커뮤니티
        </RouterLink>

        <!-- 다크모드 토글 (모바일) -->
        <div 
          :class="[
            'pt-3 border-t transition-colors',
            isDarkMode ? 'border-gray-700' : 'border-gray-200'
          ]"
        >
          <button
            @click="toggleTheme"
            :class="[
              'w-full flex items-center justify-between px-4 py-3 rounded-lg font-medium transition-all',
              isDarkMode
                ? 'text-gray-200 hover:text-white hover:bg-white/10 active:bg-white/20'
                : 'text-gray-700 hover:bg-slate-50 hover:text-slate-900 active:bg-slate-100'
            ]"
          >
            <span>{{ themeStore.isDarkMode ? '라이트 모드' : '다크 모드' }}</span>
            <!-- 태양 아이콘 (라이트 모드) -->
            <svg v-if="!themeStore.isDarkMode" class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z" />
            </svg>
            <!-- 달 아이콘 (다크 모드) -->
            <svg v-else class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z" />
            </svg>
          </button>
        </div>

        <!-- 로그인/로그아웃 버튼 -->
        <div 
          :class="[
            'pt-3 border-t transition-colors',
            isDarkMode ? 'border-gray-700' : 'border-gray-200'
          ]"
        >
          <div v-if="isLoggedIn">
            <button
              @click="onLogout"
              :class="[
                'w-full px-4 py-3 text-left rounded-lg font-medium transition-all',
                isDarkMode
                  ? 'text-gray-200 hover:text-white hover:bg-white/10 active:bg-white/20'
                  : 'text-gray-700 hover:bg-slate-50 hover:text-slate-900 active:bg-slate-100'
              ]"
            >
              로그아웃
            </button>
          </div>
          <div v-else class="space-y-2">
            <RouterLink
              to="/login"
              :class="[
                'block w-full px-4 py-3 text-center border rounded-lg font-medium transition-all',
                isDarkMode
                  ? 'text-gray-200 border-gray-600 hover:bg-white/10 hover:border-gray-500 active:bg-white/20'
                  : 'text-gray-700 border-gray-300 hover:bg-gray-50 hover:border-gray-400 active:bg-gray-100'
              ]"
              @click="mobileMenuOpen = false"
            >
              로그인
            </RouterLink>
            <RouterLink
              to="/signup"
              :class="[
                'block w-full px-4 py-3 text-center rounded-lg hover:shadow-lg font-medium transition-all duration-300 hover:scale-105',
                isDarkMode
                  ? 'text-white bg-slate-700 hover:bg-slate-600'
                  : 'text-white bg-slate-800 hover:bg-slate-900'
              ]"
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
    class="fixed inset-0 bg-black/30 backdrop-blur-sm z-40 md:hidden transition-opacity duration-300"
    @click="mobileMenuOpen = false"
  ></div>

  <!-- 상단바 높이만큼 여백 -->
  <div class="h-16"></div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useThemeStore } from '@/stores/theme'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const themeStore = useThemeStore()
const mobileMenuOpen = ref(false)

const isLoggedIn = computed(() => userStore.isLoggedIn)
const nickname = computed(() => userStore?.user?.nickname || '회원')
const profileImageUrl = computed(() => userStore?.user?.profileImageUrl)

// 다크모드 상태
const isDarkMode = computed(() => themeStore.isDarkMode)

// 메인 페이지인지 확인
const isHomePage = computed(() => route.path === '/')

// 다크모드 토글
const toggleTheme = () => {
  themeStore.toggleDarkMode()
}

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
/* 글래스모피즘 버튼 스타일 */
.glass-button-icon {
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: #4b5563;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.glass-button-icon:hover {
  background: rgba(255, 255, 255, 0.7);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transform: translateY(-1px);
}

nav {
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

/* 홈페이지일 때 NavBar 투명도 증가 */
nav:has(+ div .home-wrapper) {
  background: rgba(255, 255, 255, 0.1);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

/* 다크모드 NavBar 스타일 */
:deep(.dark) nav,
.dark nav {
  background: rgba(15, 23, 42, 0.85);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

:deep(.dark) nav:has(+ div .home-wrapper),
.dark nav:has(+ div .home-wrapper) {
  background: rgba(15, 23, 42, 0.6);
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

:deep(.dark) .glass-button-icon,
.dark .glass-button-icon {
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.15);
  color: #d1d5db;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

:deep(.dark) .glass-button-icon:hover,
.dark .glass-button-icon:hover {
  background: rgba(255, 255, 255, 0.15);
  border-color: rgba(255, 255, 255, 0.25);
  color: #ffffff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

/* 로고 호버 효과 */
.logo-link:hover {
  transform: translateY(-1px);
}

/* 메뉴 링크 밑줄 애니메이션 - 톤앤톤 적용 */
.nav-link::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 2px;
  background: linear-gradient(to right, var(--brand-500), var(--brand-400));
  transition: all 0.3s ease;
  transform: translateX(-50%);
}

.nav-link:hover::after {
  width: 70%;
}

.nav-link.router-link-active::after {
  width: 80%;
  background: linear-gradient(to right, var(--brand-400), var(--brand-300));
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
