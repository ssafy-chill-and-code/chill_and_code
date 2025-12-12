<script setup>
import { onMounted, ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { usePostStore } from '@/stores/post'
import { useCommentStore } from '@/stores/comment'

const route = useRoute()
const router = useRouter()
const postStore = usePostStore()
const commentStore = useCommentStore()

const postId = route.params.postId
const message = ref('')

const newComment = ref('')
const editingId = ref(null)
const editingContent = ref('')

const comments = computed(() => commentStore.commentsByPost[postId] || [])

// 본문 내용에서 해시태그를 추출 + (없으면) 단어 기반으로 자동 태그 생성 + 지역 태그 항상 포함(표시만)
const hashtags = computed(() => {
  const text = (postStore.post?.content || '').trim()
  const region = (postStore.post?.region || '').trim()

  // 1) 컨텐츠 내 해시태그 추출
  const explicit = text.match(/#[^\s#]+/g) || []
  const tagSet = new Set(explicit)

  // 2) 자동 태그 생성(보수적): 명시적 태그가 없을 때만, 단어 형태(한글/영문 2~12자)이며 자주 등장(>=2회)하는 상위 2개만
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
    for (const t of auto) tagSet.add(t)
  }

  // 3) 지역 태그는 항상 포함
  if (region) tagSet.add(`#${region}`)

  return Array.from(tagSet)
})

// 게시글 상세의 카테고리 배지를 목록 탭 라벨과 동일하게 계산
const derivedCategory = computed(() => {
  const p = postStore.post || {}
  const title = p.title || ''
  const content = p.content || ''
  if ((p.region || '').trim() !== '') return '지역별'
  if (title.includes('후기') || content.includes('후기')) return '후기'
  if (
    title.includes('정보공유') || content.includes('정보공유') ||
    title.includes('정보') || content.includes('정보')
  ) return '정보공유'
  if (
    title.includes('동행') || content.includes('동행') ||
    title.includes('모집') || content.includes('모집')
  ) return '동행모집'
  return '전체'
})

// 표시용 유틸: 아바타 URL, 날짜 포맷 (UI만)
function avatarUrl() {
  const p = postStore.post || {}
  const seed = p.userId ?? p.title ?? 'user'
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

async function load() {
  await postStore.fetchPostDetail(postId)
  await commentStore.fetchCommentsByPost(postId)
}

function toList() {
  router.push({ name: 'posts' })
}

function toEdit() {
  router.push({ name: 'post-edit', params: { postId } })
}

async function removePost() {
  if (!confirm('Delete this post?')) return
  try {
    await postStore.deletePost(postId)
    toList()
  } catch (e) {
    message.value = e?.response?.data?.message || e.message
  }
}

async function addComment() {
  message.value = ''
  if (!newComment.value.trim()) return
  try {
    await commentStore.createComment(postId, newComment.value)
    newComment.value = ''
    await commentStore.fetchCommentsByPost(postId)
  } catch (e) {
    message.value = e?.response?.data?.message || e.message
  }
}

function startEdit(c) {
  editingId.value = c.commentId
  editingContent.value = c.content
}

function cancelEdit() {
  editingId.value = null
  editingContent.value = ''
}

async function saveEdit() {
  try {
    await commentStore.updateComment(editingId.value, editingContent.value)
    await commentStore.fetchCommentsByPost(postId)
    cancelEdit()
  } catch (e) {
    message.value = e?.response?.data?.message || e.message
  }
}

async function removeComment(id) {
  if (!confirm('Delete this comment?')) return
  try {
    await commentStore.deleteComment(id)
    await commentStore.fetchCommentsByPost(postId)
  } catch (e) {
    message.value = e?.response?.data?.message || e.message
  }
}

onMounted(load)
</script>

<template>
  <div class="container py-4">
    <!-- Back 버튼 영역 -->
    <div class="mb-3">
      <button class="btn btn-outline-secondary" @click="toList">← 목록으로</button>
    </div>

    <!-- 게시글 카드 (Post Main Card) -->
    <section class="post-detail-section card mb-4">
      <div class="card-body">
        <!-- 배지 영역: 카테고리 / 지역 -->
        <div class="d-flex align-items-center gap-2 mb-2">
          <span class="badge bg-secondary">{{ derivedCategory }}</span>
          <span v-if="postStore.post?.region" class="badge bg-light text-dark">{{ postStore.post.region }}</span>
        </div>

        <!-- 제목 -->
        <h1 class="h3 mb-2">{{ postStore.post ? postStore.post.title : '게시글 제목' }}</h1>

        <!-- 메타 정보: 아바타/닉네임, 지역, 날짜, 좋아요/댓글/조회 (리스트와 동일 스타일) -->
        <div class="d-flex flex-wrap align-items-center gap-4 text-muted small mb-3">
          <div class="d-flex align-items-center gap-1">
            <img :src="avatarUrl()" alt="avatar" class="avatar-sm" />
            <span>{{ postStore.post?.nickname || postStore.post?.userId }}</span>
          </div>
          <div class="d-flex align-items-center gap-1">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-3 h-3"><path d="M20 10c0 4.993-5.539 10.193-7.399 11.799a1 1 0 0 1-1.202 0C9.539 20.193 4 14.993 4 10a8 8 0 0 1 16 0"/><circle cx="12" cy="10" r="3"/></svg>
            <span>{{ postStore.post?.region || '-' }}</span>
          </div>
          <div class="d-flex align-items-center gap-1">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-3 h-3"><path d="M8 2v4"/><path d="M16 2v4"/><rect width="18" height="18" x="3" y="4" rx="2"/><path d="M3 10h18"/></svg>
            <span>{{ formatDate(postStore.post?.createdAt) }}</span>
          </div>
          <div class="d-flex align-items-center gap-1">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-3 h-3"><path d="M19 14c1.49-1.46 3-3.21 3-5.5A5.5 5.5 0 0 0 16.5 3c-1.76 0-3 .5-4.5 2-1.5-1.5-2.74-2-4.5-2A5.5 5.5 0 0 0 2 8.5c0 2.3 1.5 4.05 3 5.5l7 7Z"/></svg>
            <span>0</span>
          </div>
          <div class="d-flex align-items-center gap-1">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-3 h-3"><path d="M7.9 20A9 9 0 1 0 4 16.1L2 22Z"/></svg>
            <span>{{ comments.length }}</span>
          </div>
          <div class="d-flex align-items-center gap-1">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-3 h-3"><path d="M2.062 12.348a1 1 0 0 1 0-.696 10.75 10.75 0 0 1 19.876 0 1 1 0 0 1 0 .696 10.75 10.75 0 0 1-19.876 0"/><circle cx="12" cy="12" r="3"/></svg>
            <span>0</span>
          </div>
        </div>

        <!-- 본문 내용 -->
        <div class="mb-3" style="white-space: pre-wrap;">{{ postStore.post ? postStore.post.content : '게시글 내용' }}</div>

        <!-- 태그 목록: 본문에서 해시태그 추출하여 표시만 -->
        <div v-if="hashtags.length" class="mb-3 d-flex gap-2 flex-wrap">
          <span v-for="t in hashtags" :key="t" class="badge bg-light text-dark">{{ t }}</span>
        </div>

        <!-- 좋아요 왼쪽(70%) / 공유+수정+삭제 오른쪽(30%) -->
        <div class="like-share-actions mb-3">
          <button type="button" class="btn btn-outline-primary btn-like w-100">좋아요</button>
          <div class="right-actions">
            <button type="button" class="btn btn-outline-secondary w-100">공유</button>
            <button class="btn btn-primary w-100" @click="toEdit">수정</button>
            <button class="btn btn-danger w-100" @click="removePost">삭제</button>
          </div>
        </div>

        <div v-if="postStore.loading" class="d-flex align-items-center gap-2 mt-3">
          <div class="spinner-border spinner-border-sm" role="status"></div>
          <span>Loading...</span>
        </div>
        <div v-if="postStore.error" class="alert alert-danger mt-3">{{ postStore.error }}</div>
        <div v-if="message" class="alert alert-warning mt-3">{{ message }}</div>
      </div>
    </section>

    <!-- 댓글 카드 (Comment Section Card) -->
    <section class="card mb-4">
      <div class="card-body">
        <!-- 댓글 제목 + 개수 -->
        <h2 class="h5 mb-3">댓글 <span class="text-muted">{{ comments.length }}</span></h2>

        <!-- 댓글 입력 -->
        <div class="mb-3">
          <label class="form-label">댓글 입력</label>
          <textarea class="form-textarea form-control" v-model="newComment" placeholder="댓글을 입력하세요" rows="3"></textarea>
        </div>
        <div class="mb-3 text-end">
          <button class="btn btn-primary" @click="addComment">댓글 등록</button>
        </div>

        <!-- 댓글 리스트 -->
        <div>
          <ul v-if="comments.length" class="list-group">
            <li v-for="c in comments" :key="c.commentId" class="list-group-item">
              <div v-if="editingId !== c.commentId" class="d-flex justify-content-between align-items-start">
                <div>
                  <div>{{ c.content }}</div>
                  <small class="text-muted">by {{ c.nickname || c.userId }}</small>
                </div>
                <div class="btn-group btn-group-sm">
                  <button class="btn btn-outline-secondary" @click="startEdit(c)">수정</button>
                  <button class="btn btn-outline-danger" @click="removeComment(c.commentId)">삭제</button>
                </div>
              </div>
              <div v-else class="d-flex gap-2">
                <input class="form-input form-control" v-model="editingContent" />
                <button class="btn btn-primary btn-sm" @click="saveEdit">저장</button>
                <button class="btn btn-outline-secondary btn-sm" @click="cancelEdit">취소</button>
              </div>
            </li>
          </ul>
          <div v-else class="text-muted">댓글이 없습니다.</div>
        </div>

        <div v-if="commentStore.loading" class="d-flex align-items-center gap-2 mt-3">
          <div class="spinner-border spinner-border-sm" role="status"></div>
          <span>Loading...</span>
        </div>
        <div v-if="commentStore.error" class="alert alert-danger mt-3">{{ commentStore.error }}</div>
      </div>
    </section>

    <!-- 다음글 카드 (Next Post Card) -->
    <section class="card">
      <div class="card-body">
        <h2 class="h6 mb-2">다음글</h2>
        <div class="text-muted">다음 글이 없습니다.</div>
      </div>
    </section>
  </div>
  
</template>
