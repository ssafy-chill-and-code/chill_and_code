<script setup>
import { onMounted, ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { usePostStore } from '@/stores/post'

const router = useRouter()
const postStore = usePostStore()

const search = ref('')
const region = ref('')
const sort = ref('latest')
const page = ref(1)
const size = ref(10)

const categories = ['전체', '지역별', '후기', '정보공유', '동행모집']
const selectedCategory = ref('전체')

const filteredPosts = computed(() => {
  const list = postStore.posts || []
  const cat = selectedCategory.value
  if (cat === '전체') return list
  if (cat === '지역별') {
    return list.filter(p => (p.region || '').trim() !== '')
  }
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

function goCreate() {
  router.push({ name: 'post-create' })
}

function open(postId) {
  router.push({ name: 'post-detail', params: { postId } })
}

onMounted(() => {
  load()
  postStore.fetchRegionRank({ limit: 5 })
})

const popularKeywords = computed(() => {
  const counts = new Map()
  for (const p of filteredPosts.value) {
    const tags = extractTags(p)
    const regionTag = (p.region && p.region.trim()) ? `#${p.region.trim()}` : null
    for (const t of tags) {
      if (regionTag && t === regionTag) continue
      counts.set(t, (counts.get(t) || 0) + 1)
    }
  }
  return Array.from(counts.entries())
    .map(([tag, count]) => ({ tag, count }))
    .sort((a, b) => b.count - a.count || a.tag.localeCompare(b.tag))
    .slice(0, 8)
})

function extractTags(p) {
  const text = (p?.content || '').trim()
  const region = (p?.region || '').trim()
  const explicit = text.match(/#[^\s#]+/g) || []
  const set = new Set(explicit)

  if (explicit.length === 0 && text) {
    const stop = new Set([
      '그리고','하지만','그러나','또는','또','또한','등','및','그','이','저','것','수','등등','때','처럼','같이','같은','그냥','정말','진짜','너무','매우','아주','많이','조금','좀','더','이제','먼저','먼저는','그래서','그래도','그러면','해서','하면','하고','하는','했다','했다가','있다','있어요','없다','합니다','합니다만','입니다','있습니다','였습니다','였다','가','이','은','는','을','를','에','의','와','과','로','도','다','요'
    ])
    const tokens = (text.match(/[가-힣A-Za-z]{2,12}/g) || [])
      .filter(w => !stop.has(w) && !/^\d+$/.test(w))

    const freq = new Map()
    for (const t of tokens) freq.set(t, (freq.get(t) || 0) + 1)
    const auto = Array.from(freq.entries())
      .filter(([_w,c]) => c >= 2)
      .sort((a,b) => b[1]-a[1] || b[0].length-a[0].length)
      .slice(0, 2)
      .map(([w]) => `#${w}`)
    for (const t of auto) set.add(t)
  }

  if (region) set.add(`#${region}`)
  return Array.from(set)
}

function avatarUrl(p) {
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
  if ((p.region || '').trim() !== '') return '지역별'
  const title = p.title || ''
  const content = p.content || ''
  if (title.includes('후기') || content.includes('후기')) return '후기'
  if (title.includes('정보공유') || content.includes('정보공유') ||
      title.includes('정보') || content.includes('정보')) return '정보공유'
  if (title.includes('동행') || content.includes('동행') ||
      title.includes('모집') || content.includes('모집')) return '동행모집'
  return '전체'
}
</script>

<template>
  <div class="min-h-screen bg-white py-8 lg:py-12">
    <div class="max-w-7xl mx-auto px-4 lg:px-8">
      <div class="grid grid-cols-1 lg:grid-cols-[1fr_340px] gap-8">
        <!-- 메인 영역: 게시글 목록 -->
        <section>
          <!-- 페이지 타이틀 -->
          <header class="mb-8">
            <h1 class="text-4xl font-bold text-gray-900 mb-3">커뮤니티</h1>
            <p class="text-base text-gray-600 font-light">워케이션 경험과 정보를 공유하고 소통해보세요.</p>
          </header>

          <!-- 카테고리 필터 -->
          <div class="flex gap-2 overflow-x-auto pb-2 mb-6">
            <button
              v-for="c in categories"
              :key="c"
              type="button"
              class="px-4 py-2 rounded-lg text-xs font-semibold transition-all whitespace-nowrap"
              :class="selectedCategory === c 
                ? 'bg-indigo-600 text-white shadow-md' 
                : 'bg-white text-gray-700 border border-gray-300 hover:border-gray-400 hover:bg-gray-50'"
              @click="selectedCategory = c"
            >
              {{ c }}
            </button>
          </div>

          <!-- 검색/필터 영역 -->
          <div class="bg-white border border-gray-100 rounded-2xl shadow-2xl p-5 lg:p-6 mb-6">
            <div class="grid grid-cols-1 md:grid-cols-12 gap-3">
              <div class="md:col-span-5">
                <input 
                  type="text"
                  class="w-full px-4 py-2.5 text-sm border border-gray-300 rounded-xl focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all" 
                  v-model="search" 
                  placeholder="검색어를 입력하세요" 
                  @keydown.enter="load" 
                />
              </div>
              <div class="md:col-span-3">
                <select 
                  class="w-full px-4 py-2.5 text-sm border border-gray-300 rounded-xl focus:ring-2 focus:ring-slate-500 focus:border-transparent transition-all appearance-none bg-white" 
                  v-model="region" 
                  @change="load"
                >
                  <option value="">전체 지역</option>
                  <option value="서울">서울</option>
                  <option value="부산">부산</option>
                  <option value="제주">제주</option>
                  <option value="인천">인천</option>
                  <option value="대구">대구</option>
                  <option value="대전">대전</option>
                  <option value="광주">광주</option>
                  <option value="울산">울산</option>
                  <option value="경기">경기</option>
                  <option value="강원">강원</option>
                  <option value="충북">충북</option>
                  <option value="충남">충남</option>
                  <option value="전북">전북</option>
                  <option value="전남">전남</option>
                  <option value="경북">경북</option>
                  <option value="경남">경남</option>
                  <option value="세종">세종</option>
                </select>
              </div>
              <div class="md:col-span-2">
                <select 
                  class="w-full px-4 py-2.5 text-sm border border-gray-300 rounded-xl focus:ring-2 focus:ring-indigo-500 focus:border-transparent transition-all appearance-none bg-white" 
                  v-model="sort" 
                  @change="load"
                >
                  <option value="latest">최신순</option>
                  <option value="oldest">오래된순</option>
                  <option value="popular">인기순</option>
                </select>
              </div>
              <div class="md:col-span-2">
                <button 
                  class="w-full px-4 py-2.5 text-sm bg-slate-800 text-white rounded-xl hover:bg-slate-900 hover:shadow-lg transition-all font-semibold" 
                  @click="load"
                >
                  검색
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
                class="bg-white border border-gray-100 rounded-2xl shadow-md p-5 lg:p-6 cursor-pointer transition-all hover:shadow-lg hover:border-gray-200"
                role="button"
                tabindex="0"
                @click="open(p.postId)"
                @keydown.enter="open(p.postId)"
              >
                <!-- 카테고리/지역 라벨 -->
                <div class="flex gap-2 mb-3">
                  <span class="px-2.5 py-1 bg-gray-100 text-gray-700 rounded-lg text-xs font-semibold">
                    {{ deriveCategoryForPost(p) }}
                  </span>
                  <span v-if="p.region" class="px-2.5 py-1 bg-slate-50 text-slate-700 rounded-lg text-xs font-semibold">
                    {{ p.region }}
                  </span>
                </div>

                <!-- 제목 -->
                <h3 class="text-lg font-bold text-gray-900 mb-2 line-clamp-1 leading-snug">
                  {{ p.title }}
                </h3>

                <!-- 요약 내용 -->
                <p v-if="p.content" class="text-gray-600 text-sm mb-3 line-clamp-2 leading-relaxed">
                  {{ p.content }}
                </p>

                <!-- 태그 -->
                <div v-if="extractTags(p).length" class="flex flex-wrap gap-1.5 mb-3">
                  <span 
                    v-for="t in extractTags(p)" 
                    :key="t" 
                    class="px-2.5 py-1 bg-gray-50 text-gray-600 rounded-md text-xs font-medium"
                  >
                    {{ t }}
                  </span>
                </div>

                <!-- 메타 정보 -->
                <div class="flex flex-wrap items-center gap-4 text-xs text-gray-500 pt-3 border-t border-gray-100">
                  <div class="flex items-center gap-1.5">
                    <img :src="avatarUrl(p)" alt="avatar" class="w-5 h-5 rounded-full" />
                    <span class="font-medium text-gray-700">{{ p.nickname || p.userId }}</span>
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
                  <div class="flex items-center gap-1 opacity-40">
                    <svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                      <path d="M19 14c1.49-1.46 3-3.21 3-5.5A5.5 5.5 0 0 0 16.5 3c-1.76 0-3 .5-4.5 2-1.5-1.5-2.74-2-4.5-2A5.5 5.5 0 0 0 2 8.5c0 2.3 1.5 4.05 3 5.5l7 7Z"/>
                    </svg>
                    <span>준비중</span>
                  </div>
                  <div class="flex items-center gap-1">
                    <svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                      <path d="M7.9 20A9 9 0 1 0 4 16.1L2 22Z"/>
                    </svg>
                    <span>{{ p.commentCount || 0 }}</span>
                  </div>
                </div>
              </article>
            </div>

            <!-- Empty State -->
            <div v-if="!postStore.loading && filteredPosts.length === 0" class="bg-white border border-gray-100 rounded-2xl shadow-2xl p-12 text-center">
              <div class="text-gray-400 mb-4">
                <svg class="w-16 h-16 mx-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
                </svg>
              </div>
              <p class="text-gray-600 font-medium">게시글이 없습니다.</p>
            </div>

            <!-- Loading State -->
            <div v-if="postStore.loading" class="flex flex-col items-center justify-center gap-4 py-16">
              <div class="animate-spin rounded-full h-10 w-10 border-b-2 border-indigo-600"></div>
              <span class="text-gray-600 font-medium">불러오는 중...</span>
            </div>

            <!-- Error State -->
            <div v-if="postStore.error" class="bg-red-50 border border-red-200 rounded-2xl p-6 text-red-700 text-sm">
              {{ postStore.error }}
            </div>
          </div>
        </section>

        <!-- 우측 사이드바 -->
        <aside class="space-y-4">
          <!-- 글쓰기 버튼 (최상단) -->
          <div class="bg-white border border-gray-100 rounded-2xl shadow-2xl p-5 sticky top-6">
            <button 
              class="w-full px-5 py-4 bg-slate-800 text-white rounded-xl hover:bg-slate-900 hover:shadow-lg transition-all font-semibold flex items-center justify-center gap-2" 
              @click="goCreate"
            >
              <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M12 5v14m-7-7h14"/>
              </svg>
              <span>글쓰기</span>
            </button>
          </div>

          <!-- 인기 지역 랭킹 -->
          <div class="bg-white border border-gray-100 rounded-2xl shadow-2xl p-6">
            <h3 class="text-base font-bold text-gray-900 mb-5 flex items-center gap-2">
              <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-slate-700">
                <path d="M3 3v18h18"/><path d="m19 9-5 5-4-4-3 3"/>
              </svg>
              인기 지역
            </h3>
            <ul class="space-y-3">
              <li 
                v-for="(item, idx) in postStore.regionRanks" 
                :key="item.region" 
                class="flex items-center justify-between text-sm"
              >
                <div class="flex items-center gap-3">
                  <span 
                    class="w-6 h-6 rounded-full flex items-center justify-center text-xs font-bold"
                    :class="idx === 0 ? 'bg-indigo-100 text-indigo-700' : 
                            idx === 1 ? 'bg-gray-100 text-gray-700' : 
                            idx === 2 ? 'bg-gray-100 text-gray-600' : 
                            'bg-gray-50 text-gray-600'"
                  >
                    {{ idx + 1 }}
                  </span>
                  <span class="font-semibold text-gray-900">{{ item.region }}</span>
                </div>
                <span class="text-gray-500 text-xs font-medium">{{ item.count }}건</span>
              </li>
              <li v-if="postStore.regionRanks.length === 0" class="text-gray-500 text-sm text-center py-4">
                표시할 지역이 없습니다.
              </li>
            </ul>
          </div>

          <!-- 주요 키워드 -->
          <div v-if="popularKeywords.length > 0" class="bg-white border border-gray-100 rounded-2xl shadow-2xl p-6">
            <h3 class="text-base font-bold text-gray-900 mb-5 flex items-center gap-2">
              <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-slate-700">
                <path d="M3.85 8.62a4 4 0 0 1 4.78-4.77 4 4 0 0 1 6.74 0 4 4 0 0 1 4.78 4.78 4 4 0 0 1 0 6.74 4 4 0 0 1-4.77 4.78 4 4 0 0 1-6.75 0 4 4 0 0 1-4.78-4.77 4 4 0 0 1 0-6.76Z"/><path d="m9 12 2 2 4-4"/>
              </svg>
              주요 키워드
            </h3>
            <div class="flex flex-wrap gap-2">
              <span
                v-for="kw in popularKeywords"
                :key="kw.tag"
                class="px-3 py-1.5 bg-slate-50 text-slate-700 rounded-lg text-xs font-medium border border-slate-200 hover:bg-slate-100 transition-colors"
              >
                {{ kw.tag }}
              </span>
            </div>
          </div>
        </aside>
      </div>
    </div>
  </div>
</template>

<style scoped>
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
</style>
