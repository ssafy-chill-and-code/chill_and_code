<script setup>
import { onMounted, ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { usePostStore } from '@/stores/post'
import { useThemeStore } from '@/stores/theme'

const router = useRouter()
const route = useRoute()
const postStore = usePostStore()
const themeStore = useThemeStore()

// 다크모드 상태 (전역 store에서 가져오기)
const isDarkMode = computed(() => themeStore.isDarkMode)

const search = ref('')
const region = ref('')
const sort = ref('latest')
const page = ref(1)
const size = ref(10)

// 모바일 사이드바 상태
const mobileSidebarOpen = ref(false)

// 카테고리 정의 (색상, 아이콘 포함)
const categoryConfig = {
  '전체': { 
    color: 'indigo', 
    bgColor: 'bg-indigo-100', 
    textColor: 'text-indigo-700',
    darkBgColor: 'bg-indigo-900/30',
    darkTextColor: 'text-indigo-300',
    iconPath: 'M8.25 6.75h12M8.25 12h12m-12 5.25h12M3.75 6.75h.007v.008H3.75V6.75zm.375 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zm0 0h.008v.008H4.125V6.75zm-.375 5.25h.007v.008H3.75v-.008zm.375 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zm0 0h.008v.008H4.125v-.008zm-.375 5.25h.007v.008H3.75v-.008zm.375 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zm0 0h.008v.008H4.125V18z'
  },
  '후기': { 
    color: 'amber', 
    bgColor: 'bg-amber-100', 
    textColor: 'text-amber-700',
    darkBgColor: 'bg-amber-900/30',
    darkTextColor: 'text-amber-300',
    iconPath: 'M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L10.582 16.07a4.5 4.5 0 01-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 011.13-1.897l8.932-8.931zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0115.75 21H5.25A2.25 2.25 0 013 18.75V8.25A2.25 2.25 0 015.25 6H10'
  },
  '정보공유': { 
    color: 'emerald', 
    bgColor: 'bg-emerald-100', 
    textColor: 'text-emerald-700',
    darkBgColor: 'bg-emerald-900/30',
    darkTextColor: 'text-emerald-300',
    iconPath: 'M11.25 11.25l.041-.02a.75.75 0 011.063.852l-.708 2.836a.75.75 0 001.063.853l.041-.021M21 12a9 9 0 11-18 0 9 9 0 0118 0zm-9-3.75h.008v.008H12V8.25z'
  },
  '동행모집': { 
    color: 'rose', 
    bgColor: 'bg-rose-100', 
    textColor: 'text-rose-700',
    darkBgColor: 'bg-rose-900/30',
    darkTextColor: 'text-rose-300',
    iconPath: 'M18 18.72a9.094 9.094 0 003.741-.479 3 3 0 00-4.682-2.72m.94 3.198l.001.031c0 .225-.012.447-.037.666A11.944 11.944 0 0112 21c-2.17 0-4.207-.576-5.963-1.584A6.062 6.062 0 016 18.719m12 0a5.971 5.971 0 00-.941-3.197m0 0A5.995 5.995 0 0012 12.75a5.995 5.995 0 00-5.059 2.772m0 0a3 3 0 00-4.681 2.72 8.748 8.748 0 003.74.477m.94-3.197a5.971 5.971 0 00-.94 3.197M15 6.75a3 3 0 11-6 0 3 3 0 016 0zm6 3a2.25 2.25 0 11-4.5 0 2.25 2.25 0 014.5 0zm-13.5 0a2.25 2.25 0 11-4.5 0 2.25 2.25 0 014.5 0z'
  }
}

const categories = ['전체', '후기', '정보공유', '동행모집']
const selectedCategory = ref('전체')

// 지역 목록
const regions = [
  { value: '', label: '전체 지역' },
  { value: '서울', label: '서울' },
  { value: '부산', label: '부산' },
  { value: '제주', label: '제주' },
  { value: '인천', label: '인천' },
  { value: '대구', label: '대구' },
  { value: '대전', label: '대전' },
  { value: '광주', label: '광주' },
  { value: '울산', label: '울산' },
  { value: '경기', label: '경기' },
  { value: '강원', label: '강원' },
  { value: '충북', label: '충북' },
  { value: '충남', label: '충남' },
  { value: '전북', label: '전북' },
  { value: '전남', label: '전남' },
  { value: '경북', label: '경북' },
  { value: '경남', label: '경남' },
  { value: '세종', label: '세종' }
]

// 지역 필터 선택 함수
function selectRegion(regionValue) {
  region.value = regionValue
  page.value = 1
  load()
}

const filteredPosts = computed(() => {
  const list = postStore.posts || []
  const cat = selectedCategory.value
  if (cat === '전체') return list
  if (cat === '후기') {
    return list.filter(p =>
      (p.title && p.title.includes('후기')) || (p.content && p.content.includes('후기'))
    )
  }
  if (cat === '정보공유') {
    return list.filter(p =>
      (p.title && (p.title.includes('정보') || p.title.includes('정보공유'))) ||
      (p.content && (p.content.includes('정보') || p.content.includes('정보공유')))
    )
  }
  if (cat === '동행모집') {
    return list.filter(p =>
      (p.title && (p.title.includes('동행') || p.title.includes('모집'))) ||
      (p.content && (p.content.includes('동행') || p.content.includes('모집')))
    )
  }
  return list
})

async function load() {
  await postStore.fetchPostList({
    search: search.value || undefined,
    region: region.value || undefined,
    sort: sort.value,
    page: page.value,
    size: size.value,
  })
}

function goToPage(newPage) {
  if (newPage < 1 || newPage > postStore.totalPages) return
  page.value = newPage
  load()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

function goCreate() {
  router.push({ name: 'post-create' })
}

function open(postId) {
  router.push({ name: 'post-detail', params: { postId } })
}

onMounted(() => {
  // URL query 파라미터에서 region 필터 적용
  if (route.query.region) {
    region.value = route.query.region
  }
  
  load()
  postStore.fetchRegionRank({ limit: 5 })
  postStore.fetchHashtagRank({ limit: 10, windowDays: 7 }) // 최근 7일, 10개
})

function extractTags(p) {
  const title = (p?.title || '').trim()
  const content = (p?.content || '').trim()
  
  // 제목과 내용에서 사용자가 직접 작성한 #태그만 추출
  const titleTags = title.match(/#[^\s#]+/g) || []
  const contentTags = content.match(/#[^\s#]+/g) || []
  const allTags = [...titleTags, ...contentTags]
  
  const set = new Set(allTags)
  
  return Array.from(set)
}

function avatarUrl(p) {
  // 사용자가 설정한 프로필 이미지가 있으면 그것을 사용, 없으면 dicebear
  if (p?.profileImageUrl) {
    return p.profileImageUrl
  }
  const seed = p?.userId ?? p?.title ?? 'user'
  return `https://api.dicebear.com/7.x/avataaars/svg?seed=${encodeURIComponent(String(seed))}`
}

function formatDate(input) {
  if (!input) return '-'
  const d = new Date(input)
  if (isNaN(d.getTime())) return input
  const m = d.getMonth() + 1
  const dd = d.getDate()
  return `${m}월 ${dd}일`
}

function deriveCategoryForPost(p) {
  const title = p.title || ''
  const content = p.content || ''
  if (title.includes('후기') || content.includes('후기')) return '후기'
  if (title.includes('정보공유') || content.includes('정보공유') ||
      title.includes('정보') || content.includes('정보')) return '정보공유'
  if (title.includes('동행') || content.includes('동행') ||
      title.includes('모집') || content.includes('모집')) return '동행모집'
  return null // 카테고리 없음
}

// 카테고리 스타일 가져오기
function getCategoryStyle(categoryName) {
  if (!categoryName || !categoryConfig[categoryName]) {
    return {
      bgColor: isDarkMode.value ? 'bg-gray-700/30' : 'bg-gray-100',
      textColor: isDarkMode.value ? 'text-gray-300' : 'text-gray-700',
      iconPath: 'M19.5 14.25v-2.625a3.375 3.375 0 00-3.375-3.375h-1.5A1.125 1.125 0 0113.5 7.125v-1.5a3.375 3.375 0 00-3.375-3.375H8.25m0 12.75h7.5m-7.5 3H12M10.5 2.25H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 00-9-9z'
    }
  }
  const config = categoryConfig[categoryName]
  return {
    bgColor: isDarkMode.value ? config.darkBgColor : config.bgColor,
    textColor: isDarkMode.value ? config.darkTextColor : config.textColor,
    iconPath: config.iconPath
  }
}
</script>

<template>
  <div 
    :class="[
      'community-view min-h-screen py-8 lg:py-12 relative transition-colors duration-300',
      isDarkMode 
        ? 'dark' 
        : ''
    ]"
    :style="{
      backgroundColor: isDarkMode ? '#0f172a' : '#f0f4f8'
    }"
  >
    <!-- 통일된 배경 패턴 -->
    <div 
      class="fixed inset-0 pointer-events-none"
      :style="{
        backgroundImage: isDarkMode
          ? 'radial-gradient(circle at 2px 2px, rgba(99, 102, 241, 0.15) 1px, transparent 0), radial-gradient(circle at 20px 20px, rgba(139, 92, 246, 0.1) 1px, transparent 0)'
          : 'radial-gradient(circle at 2px 2px, rgba(99, 102, 241, 0.08) 1px, transparent 0), radial-gradient(circle at 20px 20px, rgba(139, 92, 246, 0.05) 1px, transparent 0)',
        backgroundSize: '40px 40px, 60px 60px',
        backgroundPosition: '0 0, 20px 20px'
      }"
    ></div>
    <!-- 미묘한 그라데이션 오버레이 (글래스 효과 강조용) -->
    <div 
      class="fixed inset-0 pointer-events-none opacity-40"
      :style="{
        background: isDarkMode
          ? 'radial-gradient(ellipse at top, rgba(99, 102, 241, 0.1) 0%, transparent 50%), radial-gradient(ellipse at bottom, rgba(139, 92, 246, 0.1) 0%, transparent 50%)'
          : 'radial-gradient(ellipse at top, rgba(99, 102, 241, 0.05) 0%, transparent 50%), radial-gradient(ellipse at bottom, rgba(139, 92, 246, 0.05) 0%, transparent 50%)'
      }"
    ></div>
    
    <div class="max-w-7xl mx-auto px-4 lg:px-8 relative z-10">
      <div class="grid grid-cols-1 lg:grid-cols-[2fr_280px] gap-8">
        <!-- 메인 영역: 게시글 목록 -->
        <section>
          <!-- 페이지 타이틀 -->
          <header class="mb-8">
            <h1 
              :class="[
                'text-4xl font-bold mb-3 transition-colors',
                isDarkMode ? 'text-white' : 'text-gray-900'
              ]"
            >
              커뮤니티
            </h1>
            <p 
              :class="[
                'text-base font-light transition-colors',
                isDarkMode ? 'text-gray-300' : 'text-gray-600'
              ]"
            >
              워케이션 경험과 정보를 공유하고 소통해보세요.
            </p>
          </header>

          <!-- 모바일 글쓰기 버튼 (상단) -->
          <div class="lg:hidden mb-6">
            <button 
              class="w-full px-5 py-4 rounded-xl transition-all font-semibold flex items-center justify-center gap-2 relative overflow-hidden group" 
              :class="isDarkMode 
                ? 'bg-indigo-900/40 border border-indigo-500/30 text-indigo-200 hover:bg-indigo-900/60 hover:border-indigo-400/50 hover:text-white shadow-lg hover:shadow-indigo-500/20' 
                : 'bg-indigo-50/80 border border-indigo-200/60 text-indigo-700 hover:bg-indigo-100/90 hover:border-indigo-300/80 hover:text-indigo-900 shadow-md hover:shadow-indigo-200/50'"
              @click="goCreate"
            >
              <!-- 미묘한 그라데이션 오버레이 -->
              <div 
                class="absolute inset-0 opacity-0 group-hover:opacity-100 transition-opacity duration-300"
                :class="isDarkMode 
                  ? 'bg-gradient-to-br from-indigo-500/20 to-purple-500/20' 
                  : 'bg-gradient-to-br from-indigo-100/50 to-purple-100/50'"
              ></div>
              <svg 
                xmlns="http://www.w3.org/2000/svg" 
                width="18" 
                height="18" 
                viewBox="0 0 24 24" 
                fill="none" 
                stroke="currentColor" 
                stroke-width="2" 
                stroke-linecap="round" 
                stroke-linejoin="round"
                class="relative z-10 transition-transform group-hover:scale-110"
              >
                <path d="M12 5v14m-7-7h14"/>
              </svg>
              <span class="relative z-10">글쓰기</span>
            </button>
          </div>

          <!-- 모바일 필터 버튼 -->
          <div class="lg:hidden mb-4">
            <button
              @click="mobileSidebarOpen = !mobileSidebarOpen"
              class="w-full px-4 py-3 rounded-xl transition-all font-medium flex items-center justify-center gap-2 glass-button-secondary"
              :class="isDarkMode 
                ? 'text-gray-200 hover:text-white hover:bg-white/15' 
                : 'text-gray-700 hover:text-gray-900 hover:bg-white/80'"
            >
              <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M3 3h6v6H3z"/><path d="M14 4h6v6h-6z"/><path d="M14 14h6v6h-6z"/><path d="M3 14h6v6H3z"/>
              </svg>
              <span>지역 필터</span>
            </button>
          </div>

          <!-- 카테고리 필터 -->
          <div class="flex gap-2 overflow-x-auto py-3 mb-6">
              <button
                v-for="c in categories"
                :key="c"
                type="button"
                class="px-4 py-3 rounded-xl text-sm font-semibold transition-all whitespace-nowrap flex items-center gap-2"
                :class="[
                  selectedCategory === c 
                    ? `category-selected ${categoryConfig[c].bgColor} shadow-md ring-2 ring-offset-2 ${
                        c === '전체' ? (isDarkMode ? 'ring-indigo-500/50' : 'ring-indigo-300') :
                        c === '후기' ? (isDarkMode ? 'ring-amber-500/50' : 'ring-amber-300') :
                        c === '정보공유' ? (isDarkMode ? 'ring-emerald-500/50' : 'ring-emerald-300') :
                        c === '동행모집' ? (isDarkMode ? 'ring-rose-500/50' : 'ring-rose-300') : ''
                      }`
                    : isDarkMode
                      ? 'glass-button-secondary text-gray-200 hover:text-white hover:bg-white/15'
                      : 'glass-button-secondary text-gray-700 hover:text-gray-900 hover:bg-white/80'
                ]"
                @click="selectedCategory = c"
              >
                <svg 
                  xmlns="http://www.w3.org/2000/svg" 
                  fill="none" 
                  viewBox="0 0 24 24" 
                  stroke-width="1.5" 
                  stroke="currentColor" 
                  class="w-4 h-4 flex-shrink-0"
                  :class="selectedCategory === c 
                    ? 'stroke-current' 
                    : isDarkMode 
                      ? 'stroke-gray-400' 
                      : 'stroke-gray-500'"
                >
                  <path stroke-linecap="round" stroke-linejoin="round" :d="categoryConfig[c].iconPath" />
                </svg>
                <span>{{ c }}</span>
              </button>
          </div>

          <!-- 검색/필터 영역 -->
          <div class="glass-card p-5 lg:p-6 mb-6">
            <div class="grid grid-cols-1 md:grid-cols-12 gap-3">
              <div class="md:col-span-7">
                <div class="relative">
                  <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" :class="isDarkMode ? 'text-gray-400' : 'text-gray-400'">
                    <circle cx="11" cy="11" r="8"/>
                    <path d="m21 21-4.35-4.35"/>
                  </svg>
                  <input 
                    type="text"
                    :class="[
                      'w-full pl-11 pr-4 py-2.5 text-sm border rounded-xl focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all',
                      isDarkMode 
                        ? 'bg-gray-800/50 border-gray-600 text-gray-100 placeholder-gray-500' 
                        : 'border-gray-300 bg-white text-gray-900'
                    ]"
                    v-model="search" 
                    placeholder="검색어를 입력하세요" 
                    @keydown.enter="load" 
                  />
                </div>
              </div>
              <div class="md:col-span-3">
                <div class="relative">
                  <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" :class="['absolute left-4 top-1/2 transform -translate-y-1/2 pointer-events-none', isDarkMode ? 'text-gray-400' : 'text-gray-400']">
                    <path d="M3 3h6v6H3z"/><path d="M14 4h6v6h-6z"/><path d="M14 14h6v6h-6z"/><path d="M3 14h6v6H3z"/>
                  </svg>
                  <select 
                    :class="[
                      'w-full pl-11 pr-4 py-2.5 text-sm border rounded-xl focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all appearance-none',
                      isDarkMode 
                        ? 'bg-gray-800/50 border-gray-600 text-gray-100' 
                        : 'border-gray-300 bg-white text-gray-900'
                    ]"
                    v-model="sort" 
                    @change="load"
                  >
                    <option value="latest">최신순</option>
                    <option value="oldest">오래된순</option>
                    <option value="popular">인기순</option>
                  </select>
                </div>
              </div>
              <div class="md:col-span-2">
                <button 
                  class="glass-button-primary w-full px-4 py-2.5 text-sm rounded-xl transition-all font-semibold whitespace-nowrap flex items-center justify-center gap-2" 
                  @click="load"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <circle cx="11" cy="11" r="8"/>
                    <path d="m21 21-4.35-4.35"/>
                  </svg>
                  <span>검색</span>
                </button>
              </div>
            </div>
          </div>

          <!-- 게시글 리스트 -->
          <div>
            <div v-if="filteredPosts.length > 0" class="space-y-3">
              <article
                v-for="p in filteredPosts"
                :key="p.postId"
                class="glass-card rounded-2xl p-5 lg:p-6 cursor-pointer transition-all hover:shadow-lg"
                role="button"
                tabindex="0"
                @click="open(p.postId)"
                @keydown.enter="open(p.postId)"
              >
                <!-- 상단: 카테고리/지역 + 썸네일 -->
                <div class="flex items-start gap-4 mb-3">
                  <div class="flex-1">
                    <!-- 카테고리/지역 라벨 -->
                    <div class="flex gap-2 mb-3">
                      <span 
                        v-if="deriveCategoryForPost(p)" 
                        class="px-3 py-1.5 rounded-lg text-xs font-semibold flex items-center gap-1.5"
                        :class="[
                          getCategoryStyle(deriveCategoryForPost(p)).bgColor,
                          getCategoryStyle(deriveCategoryForPost(p)).textColor
                        ]"
                      >
                        <svg 
                          xmlns="http://www.w3.org/2000/svg" 
                          fill="none" 
                          viewBox="0 0 24 24" 
                          stroke-width="1.5" 
                          stroke="currentColor" 
                          class="w-3.5 h-3.5 flex-shrink-0"
                        >
                          <path stroke-linecap="round" stroke-linejoin="round" :d="getCategoryStyle(deriveCategoryForPost(p)).iconPath" />
                        </svg>
                        <span>{{ deriveCategoryForPost(p) }}</span>
                      </span>
                      <span 
                        class="px-3 py-1.5 rounded-lg text-xs font-semibold flex items-center gap-1.5"
                        :class="isDarkMode 
                          ? 'bg-indigo-900/30 text-indigo-300' 
                          : 'bg-indigo-50 text-indigo-700'"
                      >
                        <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                          <path d="M20 10c0 6-8 12-8 12s-8-6-8-12a8 8 0 0 1 16 0Z"/>
                          <circle cx="12" cy="10" r="3"/>
                        </svg>
                        <span>{{ p.region || '전국' }}</span>
                      </span>
                    </div>

                    <!-- 제목 -->
                    <h3 
                      :class="[
                        'text-lg font-bold mb-2 line-clamp-1 leading-snug transition-colors',
                        isDarkMode ? 'text-white' : 'text-gray-900'
                      ]"
                    >
                      {{ p.title }}
                    </h3>

                    <!-- 요약 내용 -->
                    <p 
                      v-if="p.content" 
                      :class="[
                        'text-sm mb-3 line-clamp-2 leading-relaxed transition-colors',
                        isDarkMode ? 'text-gray-300' : 'text-gray-600'
                      ]"
                    >
                      {{ p.content }}
                    </p>
                  </div>

                  <!-- 썸네일 이미지 -->
                  <div v-if="p.thumbnailUrl" class="flex-shrink-0">
                    <img 
                      :src="p.thumbnailUrl" 
                      :alt="p.title"
                      class="w-24 h-24 object-cover rounded-lg"
                      @error="$event.target.style.display='none'"
                    />
                  </div>
                </div>

                <!-- 태그 -->
                <div v-if="extractTags(p).length" class="flex flex-wrap gap-1.5 mb-3">
                  <span 
                    v-for="t in extractTags(p)" 
                    :key="t" 
                    class="px-2.5 py-1 rounded-md text-xs font-medium flex items-center gap-1"
                    :class="isDarkMode 
                      ? 'bg-purple-900/30 text-purple-300 border border-purple-700/50' 
                      : 'bg-purple-50 text-purple-700 border border-purple-200'"
                  >
                    <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                      <path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z"/>
                    </svg>
                    <span>{{ t }}</span>
                  </span>
                </div>

                <!-- 메타 정보 -->
                <div 
                  :class="[
                    'flex flex-wrap items-center gap-4 text-xs pt-3 border-t transition-colors',
                    isDarkMode ? 'text-gray-300 border-gray-600' : 'text-gray-500 border-gray-100'
                  ]"
                >
                  <div class="flex items-center gap-1.5">
                    <img :src="avatarUrl(p)" alt="avatar" class="w-5 h-5 rounded-full" />
                    <span 
                      :class="[
                        'font-medium transition-colors',
                        isDarkMode ? 'text-gray-200' : 'text-gray-700'
                      ]"
                    >
                      {{ p.nickname || p.userId }}
                    </span>
                  </div>
                  <div class="flex items-center gap-1">
                    <svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                      <path d="M8 2v4"/><path d="M16 2v4"/><rect width="18" height="18" x="3" y="4" rx="2"/><path d="M3 10h18"/>
                    </svg>
                    <span>{{ formatDate(p.createdAt) }}</span>
                  </div>
                  <div class="flex items-center gap-1">
                    <svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                      <path d="M2.062 12.348a1 1 0 0 1 0-.696 10.75 10.75 0 0 1 19.876 0 1 1 0 0 1 0 .696 10.75 10.75 0 0 1-19.876 0"/><circle cx="12" cy="12" r="3"/>
                    </svg>
                    <span>{{ p.viewCount || 0 }}</span>
                  </div>
                  <div class="flex items-center gap-1">
                    <svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                      <path d="M19 14c1.49-1.46 3-3.21 3-5.5A5.5 5.5 0 0 0 16.5 3c-1.76 0-3 .5-4.5 2-1.5-1.5-2.74-2-4.5-2A5.5 5.5 0 0 0 2 8.5c0 2.3 1.5 4.05 3 5.5l7 7Z"/>
                    </svg>
                    <span>{{ p.likeCount || 0 }}</span>
                  </div>
                  <div class="flex items-center gap-1">
                    <svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                      <path d="M7.9 20A9 9 0 1 0 4 16.1L2 22Z"/>
                    </svg>
                    <span>{{ p.commentCount || 0 }}</span>
                  </div>
                  <div 
                    v-if="p.placeUrl" 
                    :class="['flex items-center gap-1', isDarkMode ? 'text-indigo-300' : 'text-slate-700']" 
                    title="장소 링크 포함"
                  >
                    <svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                      <path d="M20 10c0 6-8 12-8 12s-8-6-8-12a8 8 0 0 1 16 0Z"/>
                      <circle cx="12" cy="10" r="3"/>
                    </svg>
                    <span class="font-medium">지도</span>
                  </div>
                </div>
              </article>
            </div>

            <!-- Empty State -->
            <div v-if="!postStore.loading && filteredPosts.length === 0" class="glass-card p-12 text-center">
              <div 
                :class="[
                  'mb-4 transition-colors',
                  isDarkMode ? 'text-gray-400' : 'text-gray-400'
                ]"
              >
                <svg class="w-16 h-16 mx-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
                </svg>
              </div>
              <p 
                :class="[
                  'font-medium transition-colors',
                  isDarkMode ? 'text-gray-300' : 'text-gray-600'
                ]"
              >
                게시글이 없습니다.
              </p>
            </div>

            <!-- Loading State -->
            <div v-if="postStore.loading" class="flex flex-col items-center justify-center gap-4 py-16">
              <div 
                :class="[
                  'animate-spin rounded-full h-10 w-10 border-4 transition-colors',
                  isDarkMode 
                    ? 'border-gray-700 border-t-indigo-400' 
                    : 'border-gray-200 border-t-indigo-600'
                ]"
              ></div>
              <span 
                :class="[
                  'font-medium transition-colors',
                  isDarkMode ? 'text-gray-300' : 'text-gray-600'
                ]"
              >
                불러오는 중...
              </span>
            </div>

            <!-- Error State -->
            <div 
              v-if="postStore.error" 
              :class="[
                'glass-error rounded-2xl p-6 text-sm',
                isDarkMode ? 'text-red-300' : 'text-red-700'
              ]"
            >
              {{ postStore.error }}
            </div>

            <!-- 페이지네이션 -->
            <div v-if="!postStore.loading && filteredPosts.length > 0 && postStore.totalPages > 1" class="flex items-center justify-center gap-2 mt-8">
              <!-- 첫 페이지 -->
              <button 
                class="px-3 py-2 rounded-lg text-sm font-medium transition-all"
                :class="postStore.currentPage === 1 
                  ? (isDarkMode ? 'text-gray-600 cursor-not-allowed' : 'text-gray-400 cursor-not-allowed')
                  : (isDarkMode ? 'text-gray-200 hover:bg-white/10' : 'text-gray-700 hover:bg-gray-100')"
                :disabled="postStore.currentPage === 1"
                @click="goToPage(1)"
              >
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="m11 17-5-5 5-5"/><path d="m18 17-5-5 5-5"/>
                </svg>
              </button>

              <!-- 이전 페이지 -->
              <button 
                class="px-3 py-2 rounded-lg text-sm font-medium transition-all"
                :class="postStore.currentPage === 1 
                  ? (isDarkMode ? 'text-gray-600 cursor-not-allowed' : 'text-gray-400 cursor-not-allowed')
                  : (isDarkMode ? 'text-gray-200 hover:bg-white/10' : 'text-gray-700 hover:bg-gray-100')"
                :disabled="postStore.currentPage === 1"
                @click="goToPage(postStore.currentPage - 1)"
              >
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="m15 18-6-6 6-6"/>
                </svg>
              </button>

              <!-- 페이지 번호들 -->
              <template v-for="pageNum in Array.from({ length: Math.min(5, postStore.totalPages) }, (_, i) => {
                const start = Math.max(1, Math.min(postStore.currentPage - 2, postStore.totalPages - 4))
                return start + i
              })" :key="pageNum">
                <button
                  v-if="pageNum <= postStore.totalPages"
                  class="px-4 py-2 rounded-lg text-sm font-semibold transition-all"
                  :class="pageNum === postStore.currentPage 
                    ? 'glass-button-primary text-white shadow-md' 
                    : isDarkMode
                      ? 'glass-button-secondary text-gray-200 hover:text-white'
                      : 'text-gray-700 hover:bg-gray-100'"
                  @click="goToPage(pageNum)"
                >
                  {{ pageNum }}
                </button>
              </template>

              <!-- 다음 페이지 -->
              <button 
                class="px-3 py-2 rounded-lg text-sm font-medium transition-all"
                :class="postStore.currentPage === postStore.totalPages 
                  ? (isDarkMode ? 'text-gray-600 cursor-not-allowed' : 'text-gray-400 cursor-not-allowed')
                  : (isDarkMode ? 'text-gray-200 hover:bg-white/10' : 'text-gray-700 hover:bg-gray-100')"
                :disabled="postStore.currentPage === postStore.totalPages"
                @click="goToPage(postStore.currentPage + 1)"
              >
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="m9 18 6-6-6-6"/>
                </svg>
              </button>

              <!-- 마지막 페이지 -->
              <button 
                class="px-3 py-2 rounded-lg text-sm font-medium transition-all"
                :class="postStore.currentPage === postStore.totalPages 
                  ? (isDarkMode ? 'text-gray-600 cursor-not-allowed' : 'text-gray-400 cursor-not-allowed')
                  : (isDarkMode ? 'text-gray-200 hover:bg-white/10' : 'text-gray-700 hover:bg-gray-100')"
                :disabled="postStore.currentPage === postStore.totalPages"
                @click="goToPage(postStore.totalPages)"
              >
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="m13 17 5-5-5-5"/><path d="m6 17 5-5-5-5"/>
                </svg>
              </button>
            </div>
          </div>
        </section>

        <!-- 우측 사이드바 (데스크톱) -->
        <aside class="hidden lg:block space-y-4">
          <!-- 글쓰기 버튼 (최상단) -->
          <div class="glass-card p-5 sticky top-6">
            <button 
              class="w-full px-5 py-4 rounded-xl transition-all font-semibold flex items-center justify-center gap-2 relative overflow-hidden group" 
              :class="isDarkMode 
                ? 'bg-indigo-900/40 border border-indigo-500/30 text-indigo-200 hover:bg-indigo-900/60 hover:border-indigo-400/50 hover:text-white shadow-lg hover:shadow-indigo-500/20' 
                : 'bg-indigo-50/80 border border-indigo-200/60 text-indigo-700 hover:bg-indigo-100/90 hover:border-indigo-300/80 hover:text-indigo-900 shadow-md hover:shadow-indigo-200/50'"
              @click="goCreate"
            >
              <!-- 미묘한 그라데이션 오버레이 -->
              <div 
                class="absolute inset-0 opacity-0 group-hover:opacity-100 transition-opacity duration-300"
                :class="isDarkMode 
                  ? 'bg-gradient-to-br from-indigo-500/20 to-purple-500/20' 
                  : 'bg-gradient-to-br from-indigo-100/50 to-purple-100/50'"
              ></div>
              <svg 
                xmlns="http://www.w3.org/2000/svg" 
                width="18" 
                height="18" 
                viewBox="0 0 24 24" 
                fill="none" 
                stroke="currentColor" 
                stroke-width="2" 
                stroke-linecap="round" 
                stroke-linejoin="round"
                class="relative z-10 transition-transform group-hover:scale-110"
              >
                <path d="M12 5v14m-7-7h14"/>
              </svg>
              <span class="relative z-10">글쓰기</span>
            </button>
          </div>

          <!-- 인기 지역 랭킹 -->
          <div class="glass-card p-6">
            <h3 
              :class="[
                'text-base font-bold mb-5 flex items-center gap-2 transition-colors',
                isDarkMode ? 'text-white' : 'text-gray-900'
              ]"
            >
              <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" :class="isDarkMode ? 'text-indigo-400' : 'text-indigo-600'">
                <path d="M3 3v18h18"/><path d="m19 9-5 5-4-4-3 3"/>
              </svg>
              인기 지역
            </h3>
            <ul class="space-y-3">
              <li 
                v-for="(item, idx) in postStore.regionRanks" 
                :key="item.region"
                class="flex items-center justify-between text-sm cursor-pointer hover:opacity-80 transition-all hover:translate-x-1"
                @click="selectRegion(item.region)"
              >
                <div class="flex items-center gap-3">
                  <span 
                    class="w-7 h-7 rounded-full flex items-center justify-center text-xs font-bold shadow-sm"
                    :class="idx === 0 
                      ? (isDarkMode ? 'bg-gradient-to-br from-yellow-400 to-yellow-600 text-white' : 'bg-gradient-to-br from-yellow-400 to-yellow-500 text-white') 
                      : idx === 1 
                        ? (isDarkMode ? 'bg-gradient-to-br from-gray-400 to-gray-600 text-white' : 'bg-gradient-to-br from-gray-300 to-gray-400 text-gray-700')
                        : idx === 2 
                          ? (isDarkMode ? 'bg-gradient-to-br from-amber-600 to-amber-800 text-white' : 'bg-gradient-to-br from-amber-400 to-amber-600 text-white')
                          : (isDarkMode ? 'bg-gray-700/50 text-gray-300' : 'bg-gray-100 text-gray-600')"
                  >
                    {{ idx + 1 }}
                  </span>
                  <span 
                    :class="[
                      'font-semibold transition-colors',
                      isDarkMode ? 'text-white' : 'text-gray-900'
                    ]"
                  >
                    {{ item.region }}
                  </span>
                </div>
                <span 
                  :class="[
                    'text-xs font-medium px-2 py-0.5 rounded-full transition-colors',
                    isDarkMode ? 'bg-indigo-900/30 text-indigo-300' : 'bg-indigo-50 text-indigo-700'
                  ]"
                >
                  {{ item.count }}건
                </span>
              </li>
              <li 
                v-if="postStore.regionRanks.length === 0" 
                :class="[
                  'text-sm text-center py-4 transition-colors',
                  isDarkMode ? 'text-gray-400' : 'text-gray-500'
                ]"
              >
                표시할 지역이 없습니다.
              </li>
            </ul>
          </div>

          <!-- 지역 필터 -->
          <div class="glass-card p-6">
            <h3 
              :class="[
                'text-base font-bold mb-5 flex items-center gap-2 transition-colors',
                isDarkMode ? 'text-white' : 'text-gray-900'
              ]"
            >
              <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" :class="isDarkMode ? 'text-indigo-400' : 'text-indigo-600'">
                <path d="M20 10c0 6-8 12-8 12s-8-6-8-12a8 8 0 0 1 16 0Z"/>
                <circle cx="12" cy="10" r="3"/>
              </svg>
              지역 필터
            </h3>
            <div class="space-y-2 max-h-80 overflow-y-auto">
              <button
                v-for="reg in regions"
                :key="reg.value"
                type="button"
                @click="selectRegion(reg.value)"
                :class="[
                  'w-full px-4 py-2.5 rounded-lg text-sm font-medium transition-all text-left flex items-center gap-2 overflow-hidden',
                  region === reg.value
                    ? 'bg-indigo-500 text-white shadow-md'
                    : isDarkMode
                      ? 'glass-button-secondary text-gray-200 hover:text-white hover:bg-white/15'
                      : 'glass-button-secondary text-gray-700 hover:text-gray-900 hover:bg-white/80'
                ]"
              >
                <svg v-if="region === reg.value" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M20 6 9 17l-5-5"/>
                </svg>
                <span>{{ reg.label }}</span>
              </button>
            </div>
          </div>

          <!-- 실시간 트렌드 -->
          <div v-if="postStore.hashtagRanks.length > 0" class="glass-card p-6">
            <h3 
              :class="[
                'text-base font-bold mb-5 flex items-center gap-2 transition-colors',
                isDarkMode ? 'text-white' : 'text-gray-900'
              ]"
            >
              <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" :class="isDarkMode ? 'text-indigo-400' : 'text-indigo-600'">
                <path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z"/>
              </svg>
              실시간 트렌드
              <span 
                :class="[
                  'ml-auto text-xs font-normal transition-colors',
                  isDarkMode ? 'text-gray-400' : 'text-gray-500'
                ]"
              >
                최근 7일
              </span>
            </h3>
            <div class="flex flex-wrap gap-2">
              <button
                v-for="tag in postStore.hashtagRanks"
                :key="tag.hashtag"
                @click="search = tag.hashtag; load()"
                :class="[
                  'px-3 py-2 rounded-lg text-xs font-medium border transition-all flex items-center gap-1.5',
                  isDarkMode
                    ? 'bg-indigo-900/40 text-indigo-200 border-indigo-700/50 hover:bg-indigo-900/60 hover:border-indigo-600/70'
                    : 'bg-indigo-50 text-indigo-700 border-indigo-200 hover:bg-indigo-100 hover:border-indigo-300'
                ]"
              >
                <span>{{ tag.hashtag }}</span>
                <span :class="isDarkMode ? 'text-indigo-300 font-semibold' : 'text-indigo-500 font-semibold'">{{ tag.count }}</span>
              </button>
            </div>
          </div>
        </aside>
      </div>
    </div>

    <!-- 모바일 사이드바 오버레이 -->
    <div 
      v-if="mobileSidebarOpen"
      class="fixed inset-0 bg-black/50 backdrop-blur-sm z-40 lg:hidden transition-opacity duration-300"
      @click="mobileSidebarOpen = false"
    ></div>

    <!-- 모바일 사이드바 (드로어) -->
    <aside 
      :class="[
        'fixed top-0 right-0 bottom-0 w-80 max-w-[85vw] z-50 lg:hidden transform transition-transform duration-300 ease-in-out overflow-y-auto',
        mobileSidebarOpen ? 'translate-x-0' : 'translate-x-full'
      ]"
      :style="{
        backgroundColor: isDarkMode ? 'rgba(15, 23, 42, 0.95)' : 'rgba(255, 255, 255, 0.95)',
        backdropFilter: 'blur(20px)',
        WebkitBackdropFilter: 'blur(20px)',
        borderLeft: isDarkMode ? '1px solid rgba(255, 255, 255, 0.2)' : '1px solid rgba(0, 0, 0, 0.1)',
        boxShadow: '0 8px 32px rgba(0, 0, 0, 0.3)'
      }"
    >
      <div class="p-6 space-y-4">
        <!-- 닫기 버튼 -->
        <div class="flex items-center justify-between mb-6">
          <h2 
            :class="[
              'text-xl font-bold transition-colors',
              isDarkMode ? 'text-white' : 'text-gray-900'
            ]"
          >
            필터
          </h2>
          <button
            @click="mobileSidebarOpen = false"
            :class="[
              'p-2 rounded-lg transition-colors',
              isDarkMode 
                ? 'text-gray-300 hover:text-white hover:bg-white/10' 
                : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'
            ]"
          >
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M18 6L6 18M6 6l12 12"/>
            </svg>
          </button>
        </div>

        <!-- 인기 지역 랭킹 -->
        <div class="glass-card p-6">
          <h3 
            :class="[
              'text-base font-bold mb-5 flex items-center gap-2 transition-colors',
              isDarkMode ? 'text-white' : 'text-gray-900'
            ]"
          >
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" :class="isDarkMode ? 'text-indigo-400' : 'text-indigo-600'">
              <path d="M3 3v18h18"/><path d="m19 9-5 5-4-4-3 3"/>
            </svg>
            인기 지역
          </h3>
          <ul class="space-y-3">
            <li 
              v-for="(item, idx) in postStore.regionRanks" 
              :key="item.region"
              class="flex items-center justify-between text-sm cursor-pointer hover:opacity-80 transition-all hover:translate-x-1"
              @click="selectRegion(item.region); mobileSidebarOpen = false"
            >
              <div class="flex items-center gap-3">
                <span 
                  class="w-7 h-7 rounded-full flex items-center justify-center text-xs font-bold shadow-sm"
                  :class="idx === 0 
                    ? (isDarkMode ? 'bg-gradient-to-br from-yellow-400 to-yellow-600 text-white' : 'bg-gradient-to-br from-yellow-400 to-yellow-500 text-white') 
                    : idx === 1 
                      ? (isDarkMode ? 'bg-gradient-to-br from-gray-400 to-gray-600 text-white' : 'bg-gradient-to-br from-gray-300 to-gray-400 text-gray-700')
                      : idx === 2 
                        ? (isDarkMode ? 'bg-gradient-to-br from-amber-600 to-amber-800 text-white' : 'bg-gradient-to-br from-amber-400 to-amber-600 text-white')
                        : (isDarkMode ? 'bg-gray-700/50 text-gray-300' : 'bg-gray-100 text-gray-600')"
                >
                  {{ idx + 1 }}
                </span>
                <span 
                  :class="[
                    'font-semibold transition-colors',
                    isDarkMode ? 'text-white' : 'text-gray-900'
                  ]"
                >
                  {{ item.region }}
                </span>
              </div>
              <span 
                :class="[
                  'text-xs font-medium px-2 py-0.5 rounded-full transition-colors',
                  isDarkMode ? 'bg-indigo-900/30 text-indigo-300' : 'bg-indigo-50 text-indigo-700'
                ]"
              >
                {{ item.count }}건
              </span>
            </li>
            <li 
              v-if="postStore.regionRanks.length === 0" 
              :class="[
                'text-sm text-center py-4 transition-colors',
                isDarkMode ? 'text-gray-400' : 'text-gray-500'
              ]"
            >
              표시할 지역이 없습니다.
            </li>
          </ul>
        </div>

        <!-- 지역 필터 -->
        <div class="glass-card p-6">
          <h3 
            :class="[
              'text-base font-bold mb-5 flex items-center gap-2 transition-colors',
              isDarkMode ? 'text-white' : 'text-gray-900'
            ]"
          >
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" :class="isDarkMode ? 'text-indigo-400' : 'text-indigo-600'">
              <path d="M20 10c0 6-8 12-8 12s-8-6-8-12a8 8 0 0 1 16 0Z"/>
              <circle cx="12" cy="10" r="3"/>
            </svg>
            지역 필터
          </h3>
          <div class="space-y-2 max-h-80 overflow-y-auto">
            <button
              v-for="reg in regions"
              :key="reg.value"
              type="button"
              @click="selectRegion(reg.value); mobileSidebarOpen = false"
              :class="[
                'w-full px-4 py-2.5 rounded-lg text-sm font-medium transition-all text-left flex items-center gap-2 overflow-hidden',
                region === reg.value
                  ? 'bg-indigo-500 text-white shadow-md'
                  : isDarkMode
                    ? 'glass-button-secondary text-gray-200 hover:text-white hover:bg-white/15'
                    : 'glass-button-secondary text-gray-700 hover:text-gray-900 hover:bg-white/80'
              ]"
            >
              <svg v-if="region === reg.value" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M20 6 9 17l-5-5"/>
              </svg>
              <span>{{ reg.label }}</span>
            </button>
          </div>
        </div>
      </div>
    </aside>
  </div>
</template>

<style scoped>
/* 글래스모피즘 스타일 클래스 - 라이트 모드 기본값 */
.glass-card {
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.glass-button-primary {
  background: rgba(30, 41, 59, 0.8);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: white;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
}

.glass-button-primary:hover {
  background: rgba(15, 23, 42, 0.9);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
  transform: translateY(-1px);
}

.glass-button-secondary {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.4);
  color: #374151;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.glass-button-secondary:hover {
  background: rgba(255, 255, 255, 0.8);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
  transform: translateY(-1px);
}

.glass-error {
  background: rgba(254, 242, 242, 0.8);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border-left: 4px solid #ef4444;
  box-shadow: 0 4px 16px rgba(239, 68, 68, 0.2);
  transition: all 0.3s ease;
}

/* 다크모드 스타일 */
:deep(.dark) .glass-card,
.dark .glass-card {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
}

:deep(.dark) .glass-button-secondary,
.dark .glass-button-secondary {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: #e5e7eb;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
}

:deep(.dark) .glass-button-secondary:hover,
.dark .glass-button-secondary:hover {
  background: rgba(255, 255, 255, 0.15);
  border-color: rgba(255, 255, 255, 0.3);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
}

:deep(.dark) .glass-error,
.dark .glass-error {
  background: rgba(239, 68, 68, 0.15);
  box-shadow: 0 4px 16px rgba(239, 68, 68, 0.3);
}

.line-clamp-1 {
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 카테고리 선택된 버튼 텍스트 색상 강제 적용 */
.category-selected {
  color: inherit !important;
}

.category-selected.bg-indigo-100 {
  color: #4338ca !important;
}

.category-selected.bg-amber-100 {
  color: #b45309 !important;
}

.category-selected.bg-emerald-100 {
  color: #047857 !important;
}

.category-selected.bg-rose-100 {
  color: #be123c !important;
}

.category-selected span {
  color: inherit !important;
}

.category-selected svg {
  color: inherit !important;
}
</style>
