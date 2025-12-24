<template>
  <CModal v-model="isOpen" size="lg" @update:modelValue="handleClose">
    <template #header>
      <h2 class="text-xl font-bold" :class="isDarkMode ? 'text-white' : 'text-gray-900'">
        {{ placeName }} 리뷰
      </h2>
    </template>

    <div class="review-modal-content" :class="isDarkMode ? 'dark' : ''">
      <!-- 로딩 중 -->
      <div v-if="reviewStore.loading" class="loading-container">
        <div class="loading-spinner"></div>
        <p class="loading-text">리뷰를 불러오는 중...</p>
      </div>

      <!-- 에러 메시지 -->
      <div v-else-if="reviewStore.error" class="error-message">
        {{ reviewStore.error }}
      </div>

      <!-- 리뷰 내용 -->
      <div v-else>
        <!-- 평균 별점 및 리뷰 개수 -->
        <div v-if="reviewStore.reviewSummary" class="summary-section">
          <div class="rating-display">
            <div class="average-rating">
              <span class="rating-value">{{ formatRating(reviewStore.reviewSummary.averageRating) }}</span>
              <div class="stars-container">
                <span
                  v-for="i in 5"
                  :key="i"
                  class="star"
                  :class="{
                    'star-filled': i <= Math.round(reviewStore.reviewSummary.averageRating),
                    'star-empty': i > Math.round(reviewStore.reviewSummary.averageRating)
                  }"
                >
                  ★
                </span>
              </div>
            </div>
            <div class="review-count">
              총 {{ reviewStore.reviewSummary.reviewCount || 0 }}개의 리뷰
            </div>
          </div>
        </div>

        <!-- 리뷰 목록 -->
        <div class="reviews-section">
          <h3 class="section-title">리뷰 목록</h3>
          <div v-if="!reviewStore.reviewSummary || !reviewStore.reviewSummary.reviews || reviewStore.reviewSummary.reviews.length === 0" class="empty-reviews">
            <div class="empty-icon">📝</div>
            <p class="empty-text">아직 등록된 리뷰가 없습니다</p>
          </div>
          <div v-else class="reviews-list">
            <div
              v-for="review in reviewStore.reviewSummary.reviews"
              :key="review.id"
              class="review-item"
            >
              <div class="review-header">
                <div class="reviewer-info">
                  <div class="reviewer-avatar">
                    <img
                      v-if="review.profileImageUrl"
                      :src="review.profileImageUrl"
                      :alt="review.nickname"
                    />
                    <span v-else class="avatar-placeholder">{{ review.nickname?.charAt(0) || 'U' }}</span>
                  </div>
                  <div class="reviewer-details">
                    <div class="reviewer-name">{{ review.nickname || '익명' }}</div>
                    <div class="review-date">{{ formatDate(review.createdAt) }}</div>
                  </div>
                </div>
                <div class="review-header-right">
                  <div v-if="editingReviewId !== review.id" class="review-rating">
                    <span
                      v-for="i in 5"
                      :key="i"
                      class="star-small"
                      :class="{
                        'star-filled': i <= review.rating,
                        'star-empty': i > review.rating
                      }"
                    >
                      ★
                    </span>
                  </div>
                  <div v-if="currentUserId && currentUserId === review.userId && editingReviewId !== review.id" class="review-actions">
                    <button
                      type="button"
                      class="btn-edit"
                      @click="startEdit(review)"
                      :disabled="updating || deleting"
                    >
                      수정
                    </button>
                    <button
                      type="button"
                      class="btn-delete"
                      @click="handleDelete(review.id)"
                      :disabled="updating || deleting"
                    >
                      삭제
                    </button>
                  </div>
                </div>
              </div>
              
              <!-- 수정 모드 -->
              <div v-if="editingReviewId === review.id" class="review-edit-form">
                <div class="form-group">
                  <label class="form-label">별점</label>
                  <div class="rating-input">
                    <button
                      v-for="i in 5"
                      :key="i"
                      type="button"
                      class="star-button"
                      :class="{
                        'star-filled': i <= editingReview.rating,
                        'star-empty': i > editingReview.rating
                      }"
                      @click="editingReview.rating = i"
                    >
                      <svg
                        class="star-icon"
                        viewBox="0 0 24 24"
                        fill="none"
                        stroke="currentColor"
                        stroke-width="1.5"
                        stroke-linejoin="round"
                        stroke-linecap="round"
                      >
                        <path d="M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21.02L12 17.77L5.82 21.02L7 14.14L2 9.27L8.91 8.26L12 2Z" />
                      </svg>
                    </button>
                  </div>
                </div>
                <div class="form-group">
                  <label class="form-label">내용</label>
                  <textarea
                    v-model="editingReview.content"
                    class="form-textarea"
                    rows="3"
                    placeholder="리뷰를 작성해주세요..."
                  ></textarea>
                </div>
                <div class="edit-actions">
                  <button
                    type="button"
                    class="btn-cancel"
                    @click="cancelEdit"
                    :disabled="updating"
                  >
                    취소
                  </button>
                  <button
                    type="button"
                    class="btn-save"
                    @click="saveEdit(review.id)"
                    :disabled="updating || editingReview.rating === 0"
                  >
                    <span v-if="updating">저장 중...</span>
                    <span v-else>저장</span>
                  </button>
                </div>
              </div>
              
              <!-- 일반 모드 -->
              <div v-else class="review-content">
                {{ review.content || '내용 없음' }}
              </div>
            </div>
          </div>
        </div>

        <!-- 리뷰 작성 폼 -->
        <div class="write-section">
          <h3 class="section-title">리뷰 작성</h3>
          <form @submit.prevent="handleSubmit" class="review-form">
            <div class="form-group">
              <label class="form-label">별점</label>
              <div class="rating-input">
                <button
                  v-for="i in 5"
                  :key="i"
                  type="button"
                  class="star-button"
                  :class="{
                    'star-filled': i <= newReview.rating,
                    'star-empty': i > newReview.rating
                  }"
                  @click="selectRating(i)"
                >
                  <svg
                    class="star-icon"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="1.5"
                    stroke-linejoin="round"
                    stroke-linecap="round"
                  >
                    <path d="M12 2L15.09 8.26L22 9.27L17 14.14L18.18 21.02L12 17.77L5.82 21.02L7 14.14L2 9.27L8.91 8.26L12 2Z" />
                  </svg>
                </button>
              </div>
              <div v-if="newReview.rating > 0" class="rating-selected-text">
                {{ newReview.rating }}점을 선택하셨습니다
              </div>
            </div>
            <div class="form-group">
              <label class="form-label">내용</label>
              <textarea
                v-model="newReview.content"
                class="form-textarea"
                rows="4"
                placeholder="리뷰를 작성해주세요..."
              ></textarea>
            </div>
            <div class="form-actions">
              <div class="form-actions-row">
                <span v-if="newReview.rating === 0 && !submitting" class="rating-error-message">
                  별점을 선택해주세요
                </span>
                <button
                  type="submit"
                  class="btn-submit"
                  :disabled="submitting || newReview.rating === 0"
                >
                  <span v-if="submitting">작성 중...</span>
                  <span v-else>리뷰 등록</span>
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  </CModal>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useReviewStore } from '@/stores/review'
import { useThemeStore } from '@/stores/theme'
import { useUserStore } from '@/stores/user'
import CModal from '@/components/common/CModal.vue'

const props = defineProps({
  modelValue: { type: Boolean, default: false },
  placeName: { type: String, required: true },
  region: { type: String, default: null }
})

const emit = defineEmits(['update:modelValue'])

const reviewStore = useReviewStore()
const themeStore = useThemeStore()
const userStore = useUserStore()
const isDarkMode = computed(() => themeStore.isDarkMode)

const currentUserId = computed(() => userStore.user?.userId || null)
const editingReviewId = ref(null)
const editingReview = ref({
  rating: 0,
  content: ''
})
const updating = ref(false)
const deleting = ref(false)

const isOpen = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const submitting = ref(false)
const newReview = ref({
  rating: 0,
  content: ''
})

// 모달이 열릴 때 리뷰 목록 조회 및 사용자 정보 확인
watch(() => props.modelValue, async (newVal) => {
  if (newVal && props.placeName) {
    try {
      // 로그인된 경우 사용자 정보 가져오기
      const token = sessionStorage.getItem('accessToken')
      if (token && !userStore.user) {
        try {
          await userStore.fetchProfile()
        } catch (e) {
          console.warn('사용자 정보 조회 실패:', e)
        }
      }
      
      await reviewStore.fetchReviews(props.placeName, props.region)
    } catch (e) {
      console.error('리뷰 조회 실패:', e)
      // 401 에러인 경우 (인증 필요)
      if (e?.response?.status === 401) {
        reviewStore.error = '리뷰를 조회하려면 로그인이 필요합니다.'
      }
    }
  } else {
    // 모달이 닫힐 때 폼 초기화
    newReview.value = { rating: 0, content: '' }
    cancelEdit()
    reviewStore.clearReviewSummary()
  }
})

function handleClose() {
  isOpen.value = false
}

function formatRating(rating) {
  if (!rating || rating === 0) return '0.0'
  return rating.toFixed(1)
}

function formatDate(dateString) {
  if (!dateString) return ''
  const date = new Date(dateString)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}.${month}.${day}`
}

function selectRating(rating) {
  newReview.value.rating = rating
  console.log('별점 선택:', rating, '현재 newReview:', newReview.value)
}

function startEdit(review) {
  editingReviewId.value = review.id
  editingReview.value = {
    rating: review.rating,
    content: review.content || ''
  }
}

function cancelEdit() {
  editingReviewId.value = null
  editingReview.value = {
    rating: 0,
    content: ''
  }
}

async function saveEdit(reviewId) {
  if (!editingReview.value.rating) {
    alert('별점을 선택해주세요.')
    return
  }

  updating.value = true
  try {
    await reviewStore.updateReview(
      reviewId,
      editingReview.value.rating,
      editingReview.value.content
    )
    
    // 리뷰 목록 새로고침
    await reviewStore.fetchReviews(props.placeName, props.region)
    
    // 수정 모드 종료
    cancelEdit()
    
    alert('수정되었습니다.')
  } catch (e) {
    console.error('리뷰 수정 실패:', e)
    console.error('에러 응답:', e?.response)
    console.error('에러 상태:', e?.response?.status)
    console.error('에러 메시지:', e?.response?.data)
    
    let errorMessage = '리뷰 수정에 실패했습니다.'
    if (e?.response?.data?.message) {
      errorMessage = e.response.data.message
    } else if (e?.response?.status === 401) {
      errorMessage = '로그인이 필요합니다.'
    } else if (e?.response?.status === 403) {
      errorMessage = '본인의 리뷰만 수정할 수 있습니다.'
    } else if (e?.response?.status === 404) {
      errorMessage = '리뷰를 찾을 수 없습니다.'
    } else if (e?.message) {
      errorMessage = e.message
    }
    
    alert(errorMessage)
  } finally {
    updating.value = false
  }
}

async function handleDelete(reviewId) {
  if (!confirm('정말 이 리뷰를 삭제하시겠습니까?')) {
    return
  }

  deleting.value = true
  try {
    await reviewStore.deleteReview(reviewId)
    
    // 리뷰 목록 새로고침
    await reviewStore.fetchReviews(props.placeName, props.region)
    
    alert('리뷰가 삭제되었습니다.')
  } catch (e) {
    console.error('리뷰 삭제 실패:', e)
    console.error('에러 응답:', e?.response)
    console.error('에러 상태:', e?.response?.status)
    console.error('에러 메시지:', e?.response?.data)
    
    let errorMessage = '리뷰 삭제에 실패했습니다.'
    if (e?.response?.data?.message) {
      errorMessage = e.response.data.message
    } else if (e?.response?.status === 401) {
      errorMessage = '로그인이 필요합니다.'
    } else if (e?.response?.status === 403) {
      errorMessage = '본인의 리뷰만 삭제할 수 있습니다.'
    } else if (e?.response?.status === 404) {
      errorMessage = '리뷰를 찾을 수 없습니다.'
    } else if (e?.message) {
      errorMessage = e.message
    }
    
    alert(errorMessage)
  } finally {
    deleting.value = false
  }
}

async function handleSubmit() {
  if (!newReview.value.rating) {
    alert('별점을 선택해주세요.')
    return
  }

  // 로그인 확인
  const token = sessionStorage.getItem('accessToken')
  if (!token) {
    alert('리뷰를 작성하려면 로그인이 필요합니다.')
    return
  }

  submitting.value = true
  try {
    await reviewStore.createReview(
      props.placeName,
      props.region,
      newReview.value.rating,
      newReview.value.content
    )
    
    // 리뷰 목록 새로고침
    await reviewStore.fetchReviews(props.placeName, props.region)
    
    // 폼 초기화
    newReview.value = { rating: 0, content: '' }
    
    alert('리뷰가 등록되었습니다.')
  } catch (e) {
    console.error('리뷰 작성 실패:', e)
    
    // 에러 타입별 메시지
    let errorMessage = '리뷰 작성에 실패했습니다.'
    if (e?.response?.status === 401) {
      errorMessage = '리뷰를 작성하려면 로그인이 필요합니다.'
    } else if (e?.response?.status === 400) {
      errorMessage = e?.response?.data?.message || '입력 정보를 확인해주세요.'
    } else if (e?.response?.data?.message) {
      errorMessage = e.response.data.message
    } else if (reviewStore.error) {
      errorMessage = reviewStore.error
    }
    
    alert(errorMessage)
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
/* CModal 오버라이드 - 글래스모피즘 적용 */
:deep(.modal-content) {
  background: rgba(255, 255, 255, 0.9) !important;
  backdrop-filter: blur(16px) !important;
  -webkit-backdrop-filter: blur(16px) !important;
  border: 1px solid rgba(255, 255, 255, 0.5) !important;
  border-radius: 20px !important;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.2) !important;
  overflow: hidden;
}

:deep(.dark .modal-content) {
  background: rgba(15, 23, 42, 0.9) !important;
  border: 1px solid rgba(255, 255, 255, 0.2) !important;
  box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.4) !important;
}

:deep(.modal-header) {
  background: rgba(255, 255, 255, 0.6) !important;
  backdrop-filter: blur(12px) !important;
  -webkit-backdrop-filter: blur(12px) !important;
  border-bottom: 1px solid rgba(255, 255, 255, 0.3) !important;
  padding: 1rem 1.25rem !important;
}

:deep(.dark .modal-header) {
  background: rgba(255, 255, 255, 0.08) !important;
  border-bottom: 1px solid rgba(255, 255, 255, 0.15) !important;
}

:deep(.btn-close) {
  background: rgba(255, 255, 255, 0.5) !important;
  backdrop-filter: blur(8px) !important;
  -webkit-backdrop-filter: blur(8px) !important;
  border: 1px solid rgba(255, 255, 255, 0.3) !important;
  border-radius: 8px !important;
  width: 32px !important;
  height: 32px !important;
  transition: all 0.3s ease !important;
}

:deep(.btn-close:hover) {
  background: rgba(255, 255, 255, 0.7) !important;
  transform: scale(1.1) !important;
}

:deep(.dark .btn-close) {
  background: rgba(255, 255, 255, 0.1) !important;
  border-color: rgba(255, 255, 255, 0.2) !important;
}

:deep(.dark .btn-close:hover) {
  background: rgba(255, 255, 255, 0.2) !important;
}

.review-modal-content {
  padding: 1.25rem;
  max-height: 70vh;
  overflow-y: auto;
}

/* Loading */
.loading-container {
  text-align: center;
  padding: 3rem 0;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #e2e8f0;
  border-top-color: #1e293b;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

.loading-text {
  color: #64748b;
  font-size: 0.9375rem;
}

.dark .loading-text {
  color: #cbd5e1;
}

/* Error */
.error-message {
  padding: 1.25rem;
  background: rgba(254, 242, 242, 0.9);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(254, 202, 202, 0.5);
  border-radius: 12px;
  color: #dc2626;
  text-align: center;
  box-shadow: 0 4px 16px 0 rgba(220, 38, 38, 0.1);
}

.dark .error-message {
  background: rgba(220, 38, 38, 0.15);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border-color: rgba(220, 38, 38, 0.3);
  color: #fca5a5;
  box-shadow: 0 4px 16px 0 rgba(220, 38, 38, 0.2);
}

/* Summary Section */
.summary-section {
  margin-bottom: 1.5rem;
  padding: 1rem 1.25rem;
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.4);
  border-radius: 12px;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.1);
}

.dark .summary-section {
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.15);
  box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.3);
}

.rating-display {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
}

.average-rating {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.rating-value {
  font-size: 1.75rem;
  font-weight: 700;
  color: #1e293b;
}

.dark .rating-value {
  color: #ffffff;
}

.stars-container {
  display: flex;
  gap: 0.25rem;
}

.star {
  font-size: 1.25rem;
  color: #cbd5e1;
  transition: color 0.2s;
}

.star-filled {
  color: #fbbf24;
}

.star-empty {
  color: #e2e8f0;
}

.dark .star-empty {
  color: #475569;
}

.review-count {
  font-size: 0.9375rem;
  color: #64748b;
}

.dark .review-count {
  color: #cbd5e1;
}

/* Reviews Section */
.reviews-section {
  margin-bottom: 1.5rem;
}

.section-title {
  font-size: 1rem;
  font-weight: 700;
  margin-bottom: 0.75rem;
  color: #1e293b;
}

.dark .section-title {
  color: #ffffff;
}

.empty-reviews {
  text-align: center;
  padding: 2rem 1.25rem;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 12px;
  color: #64748b;
  box-shadow: 0 4px 16px 0 rgba(31, 38, 135, 0.08);
}

.dark .empty-reviews {
  background: rgba(255, 255, 255, 0.05);
  border-color: rgba(255, 255, 255, 0.1);
  color: #94a3b8;
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.2);
}

.empty-icon {
  font-size: 2.5rem;
  margin-bottom: 0.5rem;
}

.empty-text {
  font-size: 0.9375rem;
}

.reviews-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  max-height: 350px;
  overflow-y: auto;
  padding-right: 0.5rem;
}

.review-item {
  padding: 1rem;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.4);
  box-shadow: 0 4px 16px 0 rgba(31, 38, 135, 0.1);
  transition: all 0.3s ease;
}

.review-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px 0 rgba(31, 38, 135, 0.15);
  border-color: rgba(255, 255, 255, 0.6);
}

.dark .review-item {
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.15);
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.3);
}

.dark .review-item:hover {
  box-shadow: 0 8px 24px 0 rgba(0, 0, 0, 0.4);
  border-color: rgba(255, 255, 255, 0.25);
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 0.75rem;
  gap: 1rem;
}

.review-header-right {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  flex-shrink: 0;
}

.reviewer-info {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.reviewer-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  background: #e2e8f0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.reviewer-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-placeholder {
  font-size: 1.125rem;
  font-weight: 600;
  color: #64748b;
}

.dark .avatar-placeholder {
  color: #94a3b8;
}

.reviewer-details {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.reviewer-name {
  font-weight: 600;
  font-size: 0.9375rem;
  color: #1e293b;
}

.dark .reviewer-name {
  color: #ffffff;
}

.review-date {
  font-size: 0.8125rem;
  color: #94a3b8;
}

.dark .review-date {
  color: #64748b;
}

.review-rating {
  display: flex;
  gap: 0.125rem;
}

.review-actions {
  display: flex;
  gap: 0.5rem;
  align-items: center;
}

.btn-edit,
.btn-delete {
  padding: 0.375rem 0.75rem;
  border-radius: 8px;
  font-size: 0.8125rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  border: none;
}

.btn-edit {
  background: rgba(30, 41, 59, 0.1);
  color: #1e293b;
  border: 1px solid rgba(30, 41, 59, 0.2);
}

.btn-edit:hover:not(:disabled) {
  background: rgba(30, 41, 59, 0.2);
  transform: translateY(-1px);
}

.dark .btn-edit {
  background: rgba(255, 255, 255, 0.1);
  color: #e2e8f0;
  border-color: rgba(255, 255, 255, 0.2);
}

.dark .btn-edit:hover:not(:disabled) {
  background: rgba(255, 255, 255, 0.2);
}

.btn-delete {
  background: rgba(220, 38, 38, 0.1);
  color: #dc2626;
  border: 1px solid rgba(220, 38, 38, 0.2);
}

.btn-delete:hover:not(:disabled) {
  background: rgba(220, 38, 38, 0.2);
  transform: translateY(-1px);
}

.dark .btn-delete {
  background: rgba(220, 38, 38, 0.15);
  color: #fca5a5;
  border-color: rgba(220, 38, 38, 0.3);
}

.dark .btn-delete:hover:not(:disabled) {
  background: rgba(220, 38, 38, 0.25);
}

.btn-edit:disabled,
.btn-delete:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.star-small {
  font-size: 1rem;
  color: #cbd5e1;
}

.star-small.star-filled {
  color: #fbbf24;
}

.star-small.star-empty {
  color: #e2e8f0;
}

.dark .star-small.star-empty {
  color: #475569;
}

.review-content {
  font-size: 0.9375rem;
  line-height: 1.6;
  color: #475569;
  white-space: pre-wrap;
}

.dark .review-content {
  color: #cbd5e1;
}

/* Review Edit Form */
.review-edit-form {
  margin-top: 0.75rem;
  padding: 1rem;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 12px;
}

.dark .review-edit-form {
  background: rgba(255, 255, 255, 0.05);
  border-color: rgba(255, 255, 255, 0.1);
}

.edit-actions {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
  margin-top: 1rem;
}

.btn-cancel {
  padding: 0.625rem 1.25rem;
  border: 1px solid rgba(30, 41, 59, 0.3);
  background: rgba(255, 255, 255, 0.8);
  color: #1e293b;
  border-radius: 8px;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-cancel:hover:not(:disabled) {
  background: rgba(30, 41, 59, 0.1);
}

.dark .btn-cancel {
  background: rgba(255, 255, 255, 0.1);
  color: #e2e8f0;
  border-color: rgba(255, 255, 255, 0.2);
}

.dark .btn-cancel:hover:not(:disabled) {
  background: rgba(255, 255, 255, 0.15);
}

.btn-save {
  padding: 0.625rem 1.25rem;
  border: none;
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
  color: white;
  border-radius: 8px;
  font-size: 0.875rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-save:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(30, 41, 59, 0.3);
}

.btn-save:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Write Section */
.write-section {
  padding: 1.25rem;
  margin-top: 1.25rem;
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.4);
  border-radius: 12px;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.1);
}

.dark .write-section {
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.15);
  box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.3);
}

.review-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-label {
  font-size: 0.9375rem;
  font-weight: 600;
  color: #1e293b;
}

.dark .form-label {
  color: #ffffff;
}

.rating-input {
  display: flex;
  gap: 0.5rem;
  align-items: center;
  flex-wrap: wrap;
}

.star-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0.5rem;
  margin: -0.5rem;
  transition: transform 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 48px;
  min-height: 48px;
  user-select: none;
  -webkit-user-select: none;
}

.star-button:hover {
  transform: scale(1.15);
}

.star-button:active {
  transform: scale(0.95);
}

.star-icon {
  width: 2rem;
  height: 2rem;
  transition: all 0.2s ease;
  stroke: #e2e8f0;
  fill: none;
}

.star-button.star-filled .star-icon {
  stroke: #fbbf24;
  fill: #fbbf24;
}

.star-button.star-empty .star-icon {
  stroke: #e2e8f0;
  fill: none;
}

.dark .star-button.star-empty .star-icon {
  stroke: #475569;
}

.dark .star-button.star-filled .star-icon {
  stroke: #fbbf24;
  fill: #fbbf24;
}

.rating-selected-text {
  margin-top: 0.5rem;
  font-size: 0.875rem;
  color: #64748b;
  font-weight: 500;
}

.dark .rating-selected-text {
  color: #94a3b8;
}

.form-textarea {
  width: 100%;
  padding: 0.875rem;
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  font-size: 0.9375rem;
  font-family: inherit;
  resize: vertical;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  color: #1e293b;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px 0 rgba(31, 38, 135, 0.08);
}

.form-textarea:focus {
  outline: none;
  border-color: rgba(30, 41, 59, 0.4);
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 4px 12px 0 rgba(31, 38, 135, 0.15);
}

.dark .form-textarea {
  background: rgba(255, 255, 255, 0.1);
  border-color: rgba(255, 255, 255, 0.2);
  color: #e2e8f0;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.2);
}

.dark .form-textarea:focus {
  border-color: rgba(255, 255, 255, 0.4);
  background: rgba(255, 255, 255, 0.15);
  box-shadow: 0 4px 12px 0 rgba(0, 0, 0, 0.3);
}

.form-actions {
  display: flex;
  justify-content: flex-end;
}

.form-actions-row {
  display: flex;
  align-items: center;
  gap: 1rem;
  flex-wrap: wrap;
}

.rating-error-message {
  color: #dc2626;
  font-size: 0.875rem;
  font-weight: 500;
  white-space: nowrap;
}

.dark .rating-error-message {
  color: #fca5a5;
}

.btn-submit {
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 8px;
  font-size: 0.9375rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-submit:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(30, 41, 59, 0.3);
}

.btn-submit:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

/* 스크롤바 스타일링 */
.reviews-list::-webkit-scrollbar {
  width: 6px;
}

.reviews-list::-webkit-scrollbar-track {
  background: #f1f5f9;
  border-radius: 3px;
}

.dark .reviews-list::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.05);
}

.reviews-list::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 3px;
}

.dark .reviews-list::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.2);
}

.reviews-list::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}

.dark .reviews-list::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.3);
}
</style>
