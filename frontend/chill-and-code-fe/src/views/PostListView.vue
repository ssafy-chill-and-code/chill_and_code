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

// 카테고리 탭 상태 및 필터링(프론트엔드 측)
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
  // 전체 기간 기준 Top5 지역 랭킹 (windowDays 미지정)
  postStore.fetchRegionRank({ limit: 5 })
})

// 실시간 트렌드(프론트 집계): 현재 목록(필터 적용)에서 해시태그 상위 N개 집계
const trendingTags = computed(() => {
  const counts = new Map()
  for (const p of filteredPosts.value) {
    const tags = extractTags(p)
    // 지역 태그는 트렌드에서 제외(지역 랭킹 카드와 중복 방지)
    const regionTag = (p.region && p.region.trim()) ? `#${p.region.trim()}` : null
    for (const t of tags) {
      if (regionTag && t === regionTag) continue
      counts.set(t, (counts.get(t) || 0) + 1)
    }
  }
  return Array.from(counts.entries())
    .map(([tag, count]) => ({ tag, count }))
    .sort((a, b) => b.count - a.count || a.tag.localeCompare(b.tag))
    .slice(0, 10)
})

// 리스트 아이템 표시용 유틸 (표시만, 기능 변경 없음)
function extractTags(p) {
  const text = (p?.content || '').trim()
  const region = (p?.region || '').trim()
  const explicit = text.match(/#[^\s#]+/g) || []
  const set = new Set(explicit)

  // 자동 태그(보수적): 명시적 태그가 없을 때만, 한글/영문 2~12자 단어 중 빈도>=2 상위 2개
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
</script>

<template>
  <div class="container py-4">
    <div class="community-layout">
      <!-- 좌측(데스크탑): 메인(Post List) 영역, 모바일에서는 상단 -->
      <section class="community-main">
        <!-- 페이지 제목 영역 -->
        <header class="page-header">
          <h1 class="page-title">커뮤니티</h1>
          <p class="page-subtitle">워케이션 경험을 공유하고 소통하세요</p>
        </header>

        <!-- 카테고리 탭 영역 (반드시 메인 영역 내, 리스트 위) -->
        <div class="category-tabs">
          <button
            v-for="c in categories"
            :key="c"
            type="button"
            class="category-tab"
            :class="{ active: selectedCategory === c }"
            @click="selectedCategory = c"
          >
            {{ c }}
          </button>
        </div>

        <div class="search-section card mb-3">
          <div class="card-body">
            <div class="row g-2">
              <div class="form-group col-md-5">
                <input class="form-input form-control" v-model="search" placeholder="검색어를 입력하세요" @keydown.enter="load" />
              </div>
              <div class="form-group col-md-3">
                <!-- 지역 필터 -->
                <select class="form-input form-select" v-model="region" @change="load">
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
              <div class="form-group col-md-2">
                <!-- 정렬 -->
                <select class="form-input form-select" v-model="sort" @change="load">
                  <!-- 백엔드 Mapper 기준: latest | oldest | popular -->
                  <option value="latest">최신순</option>
                  <option value="oldest">오래된순</option>
                  <option value="popular">인기순(댓글수)</option>
                </select>
              </div>
              <div class="form-group col-md-2 d-grid">
                <button class="btn btn-primary" @click="load">검색</button>
              </div>
            </div>
          </div>
        </div>

        <div class="post-list-section">
          <!-- 실제 데이터 목록 표시 영역 (기존 로직 유지) -->
          <ul class="list-group">
            <li
              v-for="p in filteredPosts"
              :key="p.postId"
              class="list-group-item post-list-item"
              role="button"
              tabindex="0"
              @click="open(p.postId)"
              @keydown.enter="open(p.postId)"
            >
              <div class="d-flex align-items-start justify-content-between gap-2 mb-2">
                <h3 class="h6 fw-semibold text-body line-clamp-1 mb-0">{{ p.title }}</h3>
              </div>

              <p class="text-muted line-clamp-2 mb-2" v-if="p.content">{{ p.content }}</p>

              <div class="d-flex flex-wrap gap-2 mb-2" v-if="extractTags(p).length">
                <span v-for="t in extractTags(p)" :key="t" class="badge bg-light text-dark">{{ t }}</span>
              </div>

              <div class="d-flex flex-wrap align-items-center gap-4 text-muted small">
                <div class="d-flex align-items-center gap-1">
                  <img :src="avatarUrl(p)" alt="avatar" class="avatar-sm" />
                  <span>{{ p.nickname || p.userId }}</span>
                </div>
                <div class="d-flex align-items-center gap-1">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-3 h-3"><path d="M20 10c0 4.993-5.539 10.193-7.399 11.799a1 1 0 0 1-1.202 0C9.539 20.193 4 14.993 4 10a8 8 0 0 1 16 0"/><circle cx="12" cy="10" r="3"/></svg>
                  <span>{{ p.region || '-' }}</span>
                </div>
                <div class="d-flex align-items-center gap-1">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-3 h-3"><path d="M8 2v4"/><path d="M16 2v4"/><rect width="18" height="18" x="3" y="4" rx="2"/><path d="M3 10h18"/></svg>
                  <span>{{ formatDate(p.createdAt) }}</span>
                </div>
                <div class="d-flex align-items-center gap-1">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-3 h-3"><path d="M19 14c1.49-1.46 3-3.21 3-5.5A5.5 5.5 0 0 0 16.5 3c-1.76 0-3 .5-4.5 2-1.5-1.5-2.74-2-4.5-2A5.5 5.5 0 0 0 2 8.5c0 2.3 1.5 4.05 3 5.5l7 7Z"/></svg>
                  <span>0</span>
                </div>
                <div class="d-flex align-items-center gap-1">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-3 h-3"><path d="M7.9 20A9 9 0 1 0 4 16.1L2 22Z"/></svg>
                  <span>0</span>
                </div>
                <div class="d-flex align-items-center gap-1">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-3 h-3"><path d="M2.062 12.348a1 1 0 0 1 0-.696 10.75 10.75 0 0 1 19.876 0 1 1 0 0 1 0 .696 10.75 10.75 0 0 1-19.876 0"/><circle cx="12" cy="12" r="3"/></svg>
                  <span>0</span>
                </div>
              </div>
            </li>
          </ul>
          <!-- 필요시 플레이스홀더 -->
          <div v-if="!postStore.loading && postStore.posts.length === 0" class="alert alert-info mt-3">게시글이 없습니다.</div>
          <div v-if="postStore.loading" class="d-flex align-items-center gap-2 mt-3">
            <div class="spinner-border spinner-border-sm" role="status"></div>
            <span>Loading...</span>
          </div>
          <div v-if="postStore.error" class="alert alert-danger mt-3">{{ postStore.error }}</div>
        </div>
      </section>

      <!-- 우측(데스크탑): 사이드바, 모바일에서는 하단 -->
      <aside class="community-sidebar">
        <div class="sidebar-box card card-body mb-3 sidebar-ranking">
          <h3 class="mb-4 d-flex align-items-center gap-2 fw-semibold">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-primary">
              <polyline points="22 7 13.5 15.5 8.5 10.5 2 17"></polyline>
              <polyline points="16 7 22 7 22 13"></polyline>
            </svg>
            인기 지역 랭킹
          </h3>
          <ul class="list-unstyled mb-0">
            <li v-for="(item, idx) in postStore.regionRanks" :key="item.region" class="d-flex align-items-center justify-content-between mb-2">
              <div class="d-flex align-items-center gap-3">
                <span class="fw-bold" :class="{ 'text-warning': idx===0, 'text-secondary': idx===1 }" :style="idx===2 ? 'color:#fd7e14;' : ''">{{ idx + 1 }}</span>
                <span class="fw-semibold">{{ item.region }}</span>
              </div>
              <div class="d-flex align-items-center gap-2">
                <span class="small text-muted">{{ item.count }}</span>
                <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-muted">
                  <path d="M5 12h14"></path>
                </svg>
              </div>
            </li>
            <li v-if="postStore.regionRanks.length === 0" class="text-muted small">표시할 지역이 없습니다.</li>
          </ul>
        </div>
        <div class="sidebar-box card card-body mb-3 trend-card">
          <h3 class="mb-3 d-flex align-items-center gap-2 fw-semibold">
            <span class="text-accent">#</span>실시간 트렌드
          </h3>
          <div class="d-flex flex-wrap gap-2">
            <button
              v-for="t in trendingTags"
              :key="t.tag"
              type="button"
              class="trend-tag"
            >
              {{ t.tag }}
              <span class="ms-1 trend-count">{{ t.count }}</span>
            </button>
            <div v-if="trendingTags.length === 0" class="text-muted small">표시할 태그가 없습니다.</div>
          </div>
        </div>
        <button class="btn btn-success w-100" @click="goCreate">글쓰기</button>
      </aside>
    </div>
  </div>
</template>
