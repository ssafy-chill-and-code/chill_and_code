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

const hashtags = computed(() => {
  const title = (postStore.post?.title || '').trim()
  const content = (postStore.post?.content || '').trim()

  // 제목과 내용에서 사용자가 직접 작성한 #태그만 추출
  const titleTags = title.match(/#[^\s#]+/g) || []
  const contentTags = content.match(/#[^\s#]+/g) || []
  const allTags = [...titleTags, ...contentTags]
  
  const tagSet = new Set(allTags)
  
  return Array.from(tagSet)
})

const derivedCategory = computed(() => {
  const p = postStore.post || {}
  const title = p.title || ''
  const content = p.content || ''
  if (title.includes('후기') || content.includes('후기')) return '후기'
  if (
    title.includes('정보공유') || content.includes('정보공유') ||
    title.includes('정보') || content.includes('정보')
  ) return '정보공유'
  if (
    title.includes('동행') || content.includes('동행') ||
    title.includes('모집') || content.includes('모집')
  ) return '동행모집'
  return null // 카테고리 없음
})

function avatarUrl() {
  const p = postStore.post || {}
  // 사용자가 설정한 프로필 이미지가 있으면 그것을 사용, 없으면 dicebear
  if (p.profileImageUrl) {
    return p.profileImageUrl
  }
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

function commentAvatarUrl(comment) {
  // 사용자가 설정한 프로필 이미지가 있으면 그것을 사용, 없으면 dicebear
  if (comment?.profileImageUrl) {
    return comment.profileImageUrl
  }
  const seed = comment?.userId ?? 'user'
  return `https://api.dicebear.com/7.x/avataaars/svg?seed=${encodeURIComponent(String(seed))}`
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
  if (!confirm('이 게시글을 삭제하시겠습니까?')) return
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
  if (!confirm('이 댓글을 삭제하시겠습니까?')) return
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
  <div class="min-h-screen bg-white py-8 lg:py-12">
    <div class="max-w-4xl mx-auto px-4 lg:px-8">
      <!-- 목록으로 돌아가기 -->
      <div class="mb-6">
        <button 
          class="inline-flex items-center gap-2 text-gray-600 hover:text-slate-900 transition-colors group" 
          @click="toList"
        >
          <svg class="w-5 h-5 transition-transform group-hover:-translate-x-1" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m15 18-6-6 6-6"/>
          </svg>
          <span class="text-sm font-medium">목록으로 돌아가기</span>
        </button>
      </div>

      <!-- 게시글 상세 카드 -->
      <article class="bg-white border border-gray-100 rounded-2xl shadow-2xl p-8 lg:p-10 mb-6">
        <!-- 상단: 카테고리/지역 + 수정/삭제 버튼 -->
        <div class="flex items-start justify-between mb-4">
          <div class="flex items-center gap-2">
            <span v-if="derivedCategory" class="px-2.5 py-1 bg-gray-100 text-gray-700 rounded-lg text-xs font-semibold">
              {{ derivedCategory }}
            </span>
            <span class="px-2.5 py-1 bg-slate-50 text-slate-700 rounded-lg text-xs font-semibold">
              {{ postStore.post?.region || '전국' }}
            </span>
          </div>
          
          <!-- 작성자 전용: 수정/삭제 (우측 상단) -->
          <div class="flex items-center gap-2">
            <button 
              class="text-xs text-gray-600 hover:text-slate-900 font-medium transition-colors" 
              @click="toEdit"
            >
              수정
            </button>
            <span class="text-gray-300">·</span>
            <button 
              class="text-xs text-gray-600 hover:text-red-600 font-medium transition-colors" 
              @click="removePost"
            >
              삭제
            </button>
          </div>
        </div>

        <!-- 제목 -->
        <h1 class="text-2xl lg:text-3xl font-bold text-gray-900 mb-4 leading-tight">
          {{ postStore.post ? postStore.post.title : '게시글 제목' }}
        </h1>

        <!-- 메타 정보 -->
        <div class="flex flex-wrap items-center gap-4 text-xs text-gray-500 mb-6 pb-5 border-b border-gray-200">
          <div class="flex items-center gap-1.5">
            <img :src="avatarUrl()" alt="avatar" class="w-6 h-6 rounded-full" />
            <span class="font-semibold text-gray-700">{{ postStore.post?.nickname || postStore.post?.userId }}</span>
          </div>
          <div class="flex items-center gap-1">
            <svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M8 2v4"/><path d="M16 2v4"/><rect width="18" height="18" x="3" y="4" rx="2"/><path d="M3 10h18"/>
            </svg>
            <span>{{ formatDate(postStore.post?.createdAt) }}</span>
          </div>
          <div class="flex items-center gap-1">
            <svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M2.062 12.348a1 1 0 0 1 0-.696 10.75 10.75 0 0 1 19.876 0 1 1 0 0 1 0 .696 10.75 10.75 0 0 1-19.876 0"/><circle cx="12" cy="12" r="3"/>
            </svg>
            <span>조회 {{ postStore.post?.viewCount || 0 }}</span>
          </div>
          <div class="flex items-center gap-1">
            <svg xmlns="http://www.w3.org/2000/svg" width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M7.9 20A9 9 0 1 0 4 16.1L2 22Z"/>
            </svg>
            <span>댓글 {{ comments.length }}</span>
          </div>
        </div>

        <!-- 본문 -->
        <div class="mb-6" style="white-space: pre-wrap; line-height: 1.7; color: #374151; font-size: 0.95rem;">
          {{ postStore.post ? postStore.post.content : '게시글 내용' }}
        </div>

        <!-- 지도 링크 카드 -->
        <a 
          v-if="postStore.post?.placeUrl" 
          :href="postStore.post.placeUrl" 
          target="_blank" 
          rel="noopener noreferrer"
          class="block mb-6 p-4 bg-gradient-to-r from-slate-50 to-gray-50 border-2 border-slate-200 rounded-xl hover:border-slate-400 hover:shadow-md transition-all group"
        >
          <div class="flex items-center justify-between">
            <div class="text-sm font-bold text-slate-900 group-hover:text-slate-700 transition-colors">
              위치 정보:
            </div>
            <div class="flex-shrink-0 text-slate-400 group-hover:text-slate-600 group-hover:translate-x-1 transition-all">
              <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
              </svg>
            </div>
          </div>
        </a>

        <!-- 태그 -->
        <div v-if="hashtags.length" class="flex flex-wrap gap-1.5 mb-6">
          <span 
            v-for="t in hashtags" 
            :key="t" 
            class="px-2.5 py-1 bg-gray-50 text-gray-600 rounded-md text-xs font-medium"
          >
            {{ t }}
          </span>
        </div>

        <!-- 액션 버튼 (좋아요/공유/신고) -->
        <div class="flex items-center gap-3 pt-6 border-t border-gray-200">
          <!-- 좋아요 버튼 -->
          <button 
            type="button" 
            class="inline-flex items-center gap-1.5 px-3 py-2 text-gray-400 hover:text-gray-500 transition-colors cursor-not-allowed"
            disabled
            title="준비중입니다"
          >
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M19 14c1.49-1.46 3-3.21 3-5.5A5.5 5.5 0 0 0 16.5 3c-1.76 0-3 .5-4.5 2-1.5-1.5-2.74-2-4.5-2A5.5 5.5 0 0 0 2 8.5c0 2.3 1.5 4.05 3 5.5l7 7Z"/>
            </svg>
            <span class="text-xs font-medium">좋아요</span>
          </button>

          <!-- 공유 버튼 -->
          <button 
            type="button" 
            class="inline-flex items-center gap-1.5 px-3 py-2 text-gray-400 hover:text-gray-500 transition-colors cursor-not-allowed"
            disabled
            title="준비중입니다"
          >
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M4 12v8a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2v-8"/><polyline points="16 6 12 2 8 6"/><line x1="12" x2="12" y1="2" y2="15"/>
            </svg>
            <span class="text-xs font-medium">공유</span>
          </button>

          <!-- 신고 버튼 -->
          <button 
            type="button" 
            class="inline-flex items-center gap-1.5 px-3 py-2 text-gray-400 hover:text-gray-500 transition-colors cursor-not-allowed ml-auto"
            disabled
            title="준비중입니다"
          >
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M3 3h18v18H3zM12 8v4m0 4h.01"/>
            </svg>
            <span class="text-xs font-medium">신고</span>
          </button>
        </div>

        <!-- Loading/Error States -->
        <div v-if="postStore.loading" class="flex items-center justify-center gap-3 mt-6">
          <div class="animate-spin rounded-full h-5 w-5 border-b-2 border-slate-800"></div>
          <span class="text-gray-600 font-medium text-sm">불러오는 중...</span>
        </div>
        <div v-if="postStore.error" class="mt-4 bg-red-50 border border-red-200 rounded-xl p-3 text-red-700 text-sm">
          {{ postStore.error }}
        </div>
        <div v-if="message" class="mt-4 bg-yellow-50 border border-yellow-200 rounded-xl p-3 text-yellow-700 text-sm">
          {{ message }}
        </div>
      </article>

      <!-- 댓글 카드 -->
      <section class="bg-white border border-gray-100 rounded-2xl shadow-2xl p-8 lg:p-10">
        <h2 class="text-xl font-bold text-gray-900 mb-6">
          댓글 <span class="text-gray-500 font-normal text-lg">({{ comments.length }})</span>
        </h2>

        <!-- 댓글 입력 -->
        <div class="mb-8">
          <label class="block text-sm font-semibold text-gray-700 mb-2">댓글 작성</label>
          <textarea 
            class="w-full px-5 py-4 border border-gray-300 rounded-xl focus:ring-2 focus:ring-slate-500 focus:border-transparent resize-none transition-all" 
            v-model="newComment" 
            placeholder="댓글을 입력하세요" 
            rows="3"
          ></textarea>
          <div class="mt-3 flex justify-end">
            <button 
              class="px-5 py-3 bg-slate-800 text-white rounded-xl hover:bg-slate-900 hover:shadow-lg transition-all font-semibold text-sm" 
              @click="addComment"
            >
              댓글 등록
            </button>
          </div>
        </div>

        <!-- 댓글 리스트 -->
        <div>
          <ul v-if="comments.length" class="space-y-4">
            <li 
              v-for="c in comments" 
              :key="c.commentId" 
              class="border border-gray-200 rounded-xl p-5"
            >
              <div v-if="editingId !== c.commentId">
                <div class="mb-3 text-gray-900 leading-relaxed text-sm">{{ c.content }}</div>
                <div class="flex items-center justify-between">
                  <div class="flex items-center gap-2 text-xs text-gray-500">
                    <img :src="commentAvatarUrl(c)" alt="avatar" class="w-5 h-5 rounded-full" />
                    <span class="font-medium text-gray-700">{{ c.nickname || c.userId }}</span>
                    <span>·</span>
                    <span>{{ formatDate(c.createdAt) }}</span>
                  </div>
                  <div class="flex items-center gap-2">
                    <button 
                      class="text-xs text-gray-600 hover:text-slate-900 font-medium transition-colors" 
                      @click="startEdit(c)"
                    >
                      수정
                    </button>
                    <span class="text-gray-300">·</span>
                    <button 
                      class="text-xs text-gray-600 hover:text-red-600 font-medium transition-colors" 
                      @click="removeComment(c.commentId)"
                    >
                      삭제
                    </button>
                  </div>
                </div>
              </div>
              <div v-else class="space-y-3">
                <textarea 
                  class="w-full px-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-slate-500 focus:border-transparent transition-all text-sm resize-none" 
                  v-model="editingContent"
                  rows="2"
                />
                <div class="flex justify-end gap-2">
                  <button 
                    class="px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors text-xs font-semibold" 
                    @click="cancelEdit"
                  >
                    취소
                  </button>
                  <button 
                    class="px-4 py-2 bg-slate-800 text-white rounded-lg hover:bg-slate-900 transition-all text-xs font-semibold" 
                    @click="saveEdit"
                  >
                    저장
                  </button>
                </div>
              </div>
            </li>
          </ul>
          <div v-else class="text-center py-12">
            <div class="text-gray-400 mb-3">
              <svg class="w-12 h-12 mx-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"/>
              </svg>
            </div>
            <p class="text-gray-600 font-medium text-sm">댓글이 없습니다. 첫 번째 댓글을 작성해보세요!</p>
          </div>
        </div>

        <!-- Loading/Error States -->
        <div v-if="commentStore.loading" class="flex items-center justify-center gap-3 mt-6">
          <div class="animate-spin rounded-full h-5 w-5 border-b-2 border-slate-800"></div>
          <span class="text-gray-600 font-medium text-sm">불러오는 중...</span>
        </div>
        <div v-if="commentStore.error" class="mt-4 bg-red-50 border border-red-200 rounded-xl p-3 text-red-700 text-sm">
          {{ commentStore.error }}
        </div>
      </section>
    </div>
  </div>
</template>
