<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { usePostStore } from '@/stores/post'

const router = useRouter()
const postStore = usePostStore()

const search = ref('')
const region = ref('')
const sort = ref('latest')
const page = ref(1)
const size = ref(10)

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

onMounted(load)
</script>

<template>
  <div>
    <h2 class="page-title">커뮤니티</h2>

    <div class="community-layout">
      <!-- 좌측: 검색 + 목록 -->
  <section class="community-main">
    <div class="search-section">
      <div class="form-group">
        <input class="form-input" v-model="search" placeholder="검색어를 입력하세요" @keydown.enter="load" />
      </div>
      <div class="form-group">
        <!-- 지역 필터 -->
        <select class="form-input" v-model="region" @change="load">
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
      <div class="form-group">
        <!-- 정렬 -->
        <select class="form-input" v-model="sort" @change="load">
          <!-- 백엔드 Mapper 기준: latest | oldest | popular -->
          <option value="latest">최신순</option>
          <option value="oldest">오래된순</option>
          <option value="popular">인기순(댓글수)</option>
        </select>
      </div>
      <div class="form-group">
        <button class="btn" @click="load">검색</button>
      </div>
    </div>

        <div class="post-list-section">
          <!-- 실제 데이터 목록 표시 영역 (기존 로직 유지) -->
          <ul>
            <li v-for="p in postStore.posts" :key="p.postId">
              <div>
                <strong>{{ p.title }}</strong>
              </div>
              <div>Region: {{ p.region || '-' }}</div>
              <button class="btn" @click="open(p.postId)">열기</button>
            </li>
          </ul>
          <!-- 필요시 플레이스홀더 -->
          <div v-if="!postStore.loading && postStore.posts.length === 0" class="post-item-placeholder">게시글 목록 영역</div>
          <div v-if="postStore.loading">Loading...</div>
          <div v-if="postStore.error">{{ postStore.error }}</div>
        </div>
      </section>

      <!-- 우측 사이드 -->
      <aside class="community-sidebar">
        <div class="sidebar-box">인기 지역 랭킹</div>
        <div class="sidebar-box">실시간 트렌드</div>
        <button class="btn" @click="goCreate">글쓰기</button>
      </aside>
    </div>
  </div>
</template>
