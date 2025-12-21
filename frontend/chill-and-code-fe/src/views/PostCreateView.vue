<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { usePostStore } from '@/stores/post'

const router = useRouter()
const postStore = usePostStore()

const title = ref('')
const region = ref('')
const content = ref('')
const message = ref('')
const loading = ref(false)

async function create() {
  message.value = ''
  if (!title.value.trim() || !content.value.trim()) {
    message.value = '제목과 내용은 필수 항목입니다.'
    return
  }
  
  loading.value = true
  try {
    const res = await postStore.createPost({ 
      title: title.value, 
      region: region.value, 
      content: content.value 
    })
    const newId = res?.data?.postId
    if (newId) {
      router.push({ name: 'post-detail', params: { postId: newId } })
    } else {
      router.push({ name: 'posts' })
    }
  } catch (e) {
    message.value = e?.response?.data?.message || '게시글 등록에 실패했습니다.'
  } finally {
    loading.value = false
  }
}

function cancel() {
  router.push({ name: 'posts' })
}
</script>

<template>
  <div class="min-h-screen bg-white py-8 lg:py-12">
    <div class="max-w-4xl mx-auto px-4 lg:px-8">
      <!-- 상단 네비게이션 -->
      <div class="mb-8">
        <button
          @click="cancel"
          class="inline-flex items-center gap-2 text-gray-600 hover:text-slate-900 transition-colors group"
        >
          <svg class="w-5 h-5 transition-transform group-hover:-translate-x-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"/>
          </svg>
          <span class="text-sm font-medium">목록으로 돌아가기</span>
        </button>
      </div>

      <!-- 메인 카드 -->
      <div class="bg-white border border-gray-100 rounded-2xl shadow-2xl p-8 lg:p-12">
        <!-- 페이지 타이틀 -->
        <header class="mb-10">
          <h1 class="text-3xl font-bold text-gray-900 mb-3">게시글 작성</h1>
          <p class="text-base text-gray-600 font-light">워케이션 경험과 정보를 공유해보세요.</p>
        </header>

        <!-- 에러 메시지 -->
        <div v-if="message" class="mb-6 p-4 bg-red-50 border border-red-200 rounded-xl text-red-700 text-sm">
          {{ message }}
        </div>

        <!-- 폼 -->
        <form @submit.prevent="create" class="space-y-6">
          <!-- 제목 입력 -->
          <div>
            <label for="title" class="block text-sm font-semibold text-gray-700 mb-3">
              제목
              <span class="text-red-500 ml-1">*</span>
            </label>
            <input
              id="title"
              v-model="title"
              type="text"
              placeholder="제목을 입력하세요"
              :disabled="loading"
              class="w-full px-5 py-4 border border-gray-300 rounded-xl focus:ring-2 focus:ring-slate-500 focus:border-transparent disabled:bg-gray-50 disabled:cursor-not-allowed transition-all text-base"
            />
          </div>

          <!-- 지역 선택 -->
          <div>
            <label for="region" class="block text-sm font-semibold text-gray-700 mb-3">
              지역
              <span class="text-gray-400 text-xs font-normal ml-2">(선택사항)</span>
            </label>
            <select
              id="region"
              v-model="region"
              :disabled="loading"
              class="w-full px-5 py-4 border border-gray-300 rounded-xl focus:ring-2 focus:ring-slate-500 focus:border-transparent disabled:bg-gray-50 disabled:cursor-not-allowed transition-all appearance-none bg-white text-base"
            >
              <option value="">지역을 선택하세요</option>
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

          <!-- 내용 입력 -->
          <div>
            <label for="content" class="block text-sm font-semibold text-gray-700 mb-3">
              내용
              <span class="text-red-500 ml-1">*</span>
            </label>
            <textarea
              id="content"
              v-model="content"
              rows="12"
              placeholder="워케이션 경험, 추천 장소, 꿀팁 등을 자유롭게 공유해주세요.&#10;&#10;해시태그를 사용하면 다른 사람들이 더 쉽게 찾을 수 있습니다. (예: #제주 #카페추천)"
              :disabled="loading"
              class="w-full px-5 py-4 border border-gray-300 rounded-xl focus:ring-2 focus:ring-slate-500 focus:border-transparent disabled:bg-gray-50 disabled:cursor-not-allowed transition-all resize-y text-base leading-relaxed"
            ></textarea>
          </div>

          <!-- 작성 팁 -->
          <div class="bg-slate-50 border border-slate-200 rounded-xl p-5">
            <h3 class="text-sm font-semibold text-slate-800 mb-3 flex items-center gap-2">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <circle cx="12" cy="12" r="10"/><path d="M12 16v-4"/><path d="M12 8h.01"/>
              </svg>
              작성 가이드
            </h3>
            <ul class="space-y-2 text-sm text-slate-600">
              <li class="flex items-start gap-2">
                <span class="text-slate-400 mt-0.5">•</span>
                <span>제목은 핵심 내용이 드러나도록 명확하게 작성해주세요.</span>
              </li>
              <li class="flex items-start gap-2">
                <span class="text-slate-400 mt-0.5">•</span>
                <span>지역을 선택하면 지역별 게시판에 자동으로 분류됩니다.</span>
              </li>
              <li class="flex items-start gap-2">
                <span class="text-slate-400 mt-0.5">•</span>
                <span>해시태그(#)를 사용하면 주요 키워드로 등록되어 검색이 쉬워집니다.</span>
              </li>
            </ul>
          </div>

          <!-- 버튼 그룹 -->
          <div class="flex flex-col sm:flex-row gap-3 pt-6">
            <button
              type="submit"
              :disabled="loading"
              class="flex-1 bg-slate-800 text-white rounded-xl px-6 py-4 font-semibold hover:bg-slate-900 hover:shadow-lg disabled:bg-slate-300 disabled:cursor-not-allowed transition-all"
            >
              <span v-if="loading" class="flex items-center justify-center gap-2">
                <svg class="animate-spin h-5 w-5" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                등록 중...
              </span>
              <span v-else>게시글 등록</span>
            </button>
            <button
              type="button"
              @click="cancel"
              :disabled="loading"
              class="flex-1 bg-white text-gray-700 border-2 border-gray-300 rounded-xl px-6 py-4 font-semibold hover:bg-gray-50 hover:border-gray-400 disabled:bg-gray-50 disabled:cursor-not-allowed transition-all"
            >
              취소
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>
