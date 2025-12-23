<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { usePostStore } from '@/stores/post'
import api from '@/api/axios'

const route = useRoute()
const router = useRouter()
const postStore = usePostStore()

const title = ref('')
const region = ref('')
const content = ref('')
const category = ref('')
const placeUrl = ref('')
const showPlaceInput = ref(false)
const uploadedImages = ref([])
const uploadingImage = ref(false)
const message = ref('')
const loading = ref(false)
const initialLoading = ref(true)

const categories = [
  { value: '후기', label: '후기' },
  { value: '정보공유', label: '정보공유' },
  { value: '동행모집', label: '동행모집' },
]

onMounted(async () => {
  try {
    await postStore.fetchPostDetail(route.params.postId)
    const p = postStore.post
    if (p) {
      // 제목에서 카테고리 태그 추출
      let originalTitle = p.title || ''
      const categoryMatch = originalTitle.match(/^\[([^\]]+)\]\s*/)
      if (categoryMatch) {
        const extractedCategory = categoryMatch[1]
        if (['후기', '정보공유', '동행모집'].includes(extractedCategory)) {
          category.value = extractedCategory
          originalTitle = originalTitle.replace(/^\[([^\]]+)\]\s*/, '')
        }
      }
      
      title.value = originalTitle
      region.value = p.region || ''
      content.value = p.content || ''
      placeUrl.value = p.placeUrl || ''
      showPlaceInput.value = !!p.placeUrl
    }
  } catch (e) {
    message.value = '게시글을 불러오는데 실패했습니다.'
  } finally {
    initialLoading.value = false
  }
})

async function handleImageUpload(event) {
  const files = event.target.files
  if (!files || files.length === 0) return

  uploadingImage.value = true
  try {
    for (const file of files) {
      // 파일 크기 체크 (5MB 제한)
      if (file.size > 5 * 1024 * 1024) {
        message.value = '이미지 크기는 5MB 이하여야 합니다.'
        continue
      }

      const formData = new FormData()
      formData.append('file', file)

      const response = await api.post('/files/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })

      const imageUrl = response.data.data.url
      uploadedImages.value.push(imageUrl)
    }
  } catch (e) {
    message.value = e?.response?.data?.message || '이미지 업로드에 실패했습니다.'
  } finally {
    uploadingImage.value = false
    // input 초기화
    event.target.value = ''
  }
}

function removeImage(index) {
  uploadedImages.value.splice(index, 1)
}

async function save() {
  message.value = ''
  if (!title.value.trim() || !content.value.trim()) {
    message.value = '제목과 내용은 필수 항목입니다.'
    return
  }
  
  loading.value = true
  try {
    // 카테고리가 선택되었으면 제목에 태그 추가
    let finalTitle = title.value.trim()
    if (category.value && !finalTitle.includes(`[${category.value}]`) && !finalTitle.includes(category.value)) {
      finalTitle = `[${category.value}] ${finalTitle}`
    }

    // 새로 업로드된 이미지 URL들을 content에 추가
    let finalContent = content.value
    if (uploadedImages.value.length > 0) {
      const imageHtml = uploadedImages.value.map(url => `<img src="${url}" alt="게시글 이미지">`).join('\n')
      finalContent = finalContent + '\n\n' + imageHtml
    }
    
    await postStore.updatePost(route.params.postId, { 
      title: finalTitle, 
      region: region.value, 
      content: finalContent,
      placeUrl: placeUrl.value || null
    })
    router.push({ name: 'post-detail', params: { postId: route.params.postId } })
  } catch (e) {
    message.value = e?.response?.data?.message || '게시글 수정에 실패했습니다.'
  } finally {
    loading.value = false
  }
}

function cancel() {
  router.push({ name: 'post-detail', params: { postId: route.params.postId } })
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
          <span class="text-sm font-medium">게시글로 돌아가기</span>
        </button>
      </div>

      <!-- 초기 로딩 상태 -->
      <div v-if="initialLoading" class="flex flex-col items-center justify-center gap-4 py-20">
        <div class="animate-spin rounded-full h-10 w-10 border-b-2 border-slate-800"></div>
        <span class="text-gray-600 font-medium">게시글을 불러오는 중...</span>
      </div>

      <!-- 메인 카드 -->
      <div v-else class="bg-white border border-gray-100 rounded-2xl shadow-2xl p-8 lg:p-12">
        <!-- 페이지 타이틀 -->
        <header class="mb-10">
          <h1 class="text-3xl font-bold text-gray-900 mb-3">게시글 수정</h1>
          <p class="text-base text-gray-600 font-light">게시글 내용을 수정하세요.</p>
        </header>

        <!-- 에러 메시지 -->
        <div v-if="message" class="mb-6 p-4 bg-red-50 border border-red-200 rounded-xl text-red-700 text-sm">
          {{ message }}
        </div>

        <!-- 폼 -->
        <form @submit.prevent="save" class="space-y-6">
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

          <!-- 카테고리 선택 -->
          <div>
            <label class="block text-sm font-semibold text-gray-700 mb-3">
              카테고리
              <span class="text-gray-400 text-xs font-normal ml-2">(선택사항)</span>
            </label>
            <div class="flex flex-wrap gap-3">
              <button
                v-for="cat in categories"
                :key="cat.value"
                type="button"
                @click="category = (category === cat.value ? '' : cat.value)"
                :disabled="loading"
                class="px-5 py-3 rounded-xl text-sm font-semibold transition-all"
                :class="category === cat.value 
                  ? 'bg-slate-800 text-white shadow-md' 
                  : 'bg-white text-gray-700 border-2 border-gray-300 hover:border-slate-400 hover:bg-gray-50'"
              >
                {{ cat.label }}
              </button>
            </div>
            <p v-if="!category" class="mt-2 text-xs text-gray-500">카테고리를 선택하지 않으면 일반 게시글로 유지됩니다.</p>
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

          <!-- 장소 수정 -->
          <div>
            <div class="flex items-center justify-between mb-3">
              <label class="block text-sm font-semibold text-gray-700">
                장소 링크
                <span class="text-gray-400 text-xs font-normal ml-2">(선택사항)</span>
              </label>
              <button
                type="button"
                @click="showPlaceInput = !showPlaceInput"
                :disabled="loading"
                class="inline-flex items-center gap-2 px-4 py-2 text-sm font-semibold rounded-lg transition-all"
                :class="showPlaceInput 
                  ? 'bg-slate-100 text-slate-700 hover:bg-slate-200' 
                  : 'bg-slate-800 text-white hover:bg-slate-900'"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                </svg>
                {{ showPlaceInput ? '장소 취소' : (placeUrl ? '장소 수정' : '장소 추가') }}
              </button>
            </div>
            <transition name="slide">
              <div v-if="showPlaceInput" class="space-y-3">
                <input
                  v-model="placeUrl"
                  type="url"
                  placeholder="지도 링크를 붙여넣으세요 (네이버/카카오/구글 지도)"
                  :disabled="loading"
                  class="w-full px-5 py-4 border border-gray-300 rounded-xl focus:ring-2 focus:ring-slate-500 focus:border-transparent disabled:bg-gray-50 disabled:cursor-not-allowed transition-all text-base"
                />
                <p v-if="placeUrl" class="flex items-center gap-2 text-sm text-green-600">
                  <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
                  </svg>
                  장소 링크가 추가되었습니다
                </p>
              </div>
            </transition>
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

          <!-- 이미지 업로드 (추가) -->
          <div>
            <label class="block text-sm font-semibold text-gray-700 mb-3">
              이미지 추가
              <span class="text-gray-400 text-xs font-normal ml-2">(선택사항, 최대 5MB)</span>
            </label>
            
            <!-- 업로드 버튼 -->
            <div class="mb-3">
              <label 
                for="image-upload-edit"
                class="inline-flex items-center gap-2 px-5 py-3 bg-white text-gray-700 border-2 border-gray-300 rounded-xl hover:bg-gray-50 hover:border-gray-400 transition-all cursor-pointer font-semibold text-sm"
                :class="{'opacity-50 cursor-not-allowed': loading || uploadingImage}"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
                <span v-if="uploadingImage">업로드 중...</span>
                <span v-else>이미지 선택</span>
              </label>
              <input
                id="image-upload-edit"
                type="file"
                accept="image/*"
                multiple
                @change="handleImageUpload"
                :disabled="loading || uploadingImage"
                class="hidden"
              />
            </div>

            <!-- 업로드된 이미지 미리보기 -->
            <div v-if="uploadedImages.length > 0" class="grid grid-cols-2 md:grid-cols-3 gap-3">
              <div 
                v-for="(imageUrl, index) in uploadedImages" 
                :key="index"
                class="relative group"
              >
                <img 
                  :src="imageUrl" 
                  alt="업로드된 이미지"
                  class="w-full h-32 object-cover rounded-lg border-2 border-gray-200"
                />
                <button
                  type="button"
                  @click="removeImage(index)"
                  class="absolute top-2 right-2 w-7 h-7 bg-red-500 text-white rounded-full hover:bg-red-600 transition-all opacity-0 group-hover:opacity-100 flex items-center justify-center"
                  title="이미지 삭제"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                  </svg>
                </button>
              </div>
            </div>
          </div>

          <!-- 수정 안내 -->
          <div class="bg-slate-50 border border-slate-200 rounded-xl p-5">
            <h3 class="text-sm font-semibold text-slate-800 mb-3 flex items-center gap-2">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <circle cx="12" cy="12" r="10"/><path d="M12 16v-4"/><path d="M12 8h.01"/>
              </svg>
              수정 안내
            </h3>
            <ul class="space-y-2 text-sm text-slate-600">
              <li class="flex items-start gap-2">
                <span class="text-slate-400 mt-0.5">•</span>
                <span>수정된 내용은 즉시 반영됩니다.</span>
              </li>
              <li class="flex items-start gap-2">
                <span class="text-slate-400 mt-0.5">•</span>
                <span>제목과 내용은 필수로 입력해야 합니다.</span>
              </li>
              <li class="flex items-start gap-2">
                <span class="text-slate-400 mt-0.5">•</span>
                <span>카테고리 변경 시 제목 태그가 업데이트됩니다. (다시 클릭하면 해제)</span>
              </li>
              <li class="flex items-start gap-2">
                <span class="text-slate-400 mt-0.5">•</span>
                <span>지역 변경 시 게시글 분류가 함께 변경됩니다.</span>
              </li>
              <li class="flex items-start gap-2">
                <span class="text-slate-400 mt-0.5">•</span>
                <span>장소 링크를 추가/수정하면 게시글에 지도 보기 카드가 표시됩니다.</span>
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
                수정 중...
              </span>
              <span v-else>수정 완료</span>
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

<style scoped>
.slide-enter-active, .slide-leave-active {
  transition: all 0.3s ease;
}
.slide-enter-from {
  opacity: 0;
  transform: translateY(-10px);
}
.slide-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style>
