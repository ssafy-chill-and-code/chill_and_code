<template>
  <div 
    :class="[
      'place-result-wrapper',
      isDarkMode ? 'dark' : ''
    ]"
  >
    <div class="container-custom py-5">
      <!-- 헤더 -->
      <header class="text-center mb-5">
        <div class="step-indicator">추천 결과</div>
        <h1 class="page-title">당신을 위한 워케이션 장소</h1>
        <p class="page-subtitle">{{ periodText }}에 추천하는 최고의 워케이션 장소들입니다.</p>
      </header>

      <!-- 로딩 중 -->
      <div v-if="placeStore.loading" class="loading-wrapper">
        <div class="loading-spinner"></div>
        <div class="loading-text">장소를 추천하고 있습니다...</div>
      </div>

      <!-- 에러 메시지 -->
      <div v-else-if="placeStore.error" class="error-card">
        <div class="error-icon">😔</div>
        <div class="error-title">오류가 발생했습니다</div>
        <div class="error-message">{{ placeStore.error }}</div>
        <button class="btn-retry" @click="router.push('/recommend/place')">다시 시도</button>
      </div>

      <!-- 결과가 없을 때 -->
      <div v-else-if="!places || places.length === 0" class="empty-card">
        <div class="empty-icon">🔍</div>
        <div class="empty-title">추천 가능한 장소가 없습니다</div>
        <div class="empty-message">조건을 변경해주세요</div>
        <button class="btn-retry" @click="router.push('/recommend/place')">조건 변경하기</button>
      </div>

      <!-- 결과 -->
      <div v-else class="results-wrapper">
        <!-- 선택 요약 -->
        <section class="summary-card mb-4">
          <div class="summary-item">
            <div class="summary-label">기간</div>
            <div class="summary-value">{{ periodText }}</div>
          </div>
          <div class="summary-divider"></div>
          <div class="summary-item">
            <div class="summary-label">스타일</div>
            <div class="summary-value">{{ displayStyle }}</div>
          </div>
          <div class="summary-divider"></div>
          <div class="summary-item">
            <div class="summary-label">예산</div>
            <div class="summary-value">{{ displayBudget }}</div>
          </div>
          <div class="summary-divider"></div>
          <div class="summary-item">
            <div class="summary-label">추천 수</div>
            <div class="summary-value">{{ places.length }}곳</div>
          </div>
        </section>

        <!-- 장소 카드 -->
        <section class="places-section">
          <div class="places-grid">
            <div v-for="place in places" :key="place.placeId" class="place-card-wrapper">
              <div class="place-card">
                <!-- 이미지 -->
                <div class="place-image">
                  <img v-if="place.imageUrl && place.imageUrl !== 'NO_IMAGE'" :src="place.imageUrl" :alt="place.name" />
                  <div v-else class="place-image-placeholder">🏖️</div>
                </div>

                <!-- 정보 -->
                <div class="place-info">
                  <h6 class="place-name">{{ place.name }}</h6>
                  <div class="place-region-wrapper">
                    <span class="place-region">{{ place.region }}</span>
                    <button 
                      class="region-community-btn"
                      @click="goToCommunity(place.region)"
                      title="커뮤니티에서 {{ place.region }} 게시물 보기"
                    >
                      커뮤니티 보기
                    </button>
                  </div>

                  <!-- 태그 -->
                  <div class="place-tags">
                    <span v-for="tag in place.tags" :key="tag" class="place-tag">{{ tag }}</span>
                  </div>

                  <!-- 추천 이유 -->
                  <div class="reason-box">
                    <div class="reason-title">📝 추천이유</div>
                    <div class="reason-text">{{ place.reasonText || '당신의 예산과 일정에 완벽하게 맞습니다' }}</div>
                  </div>

                  <!-- 버튼 -->
                  <div class="place-actions">
                    <button class="btn-review" @click="openReviewModal(place)">
                      <span>⭐</span> 리뷰 보기
                    </button>
                    <button class="btn-select" @click="goSchedule(place)">
                      <span>✓</span> 이 장소로 일정 만들기
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>
      </div>
    </div>

    <!-- 리뷰 모달 -->
    <PlaceReviewModal
      v-model="reviewModalOpen"
      :place-name="selectedPlace?.name"
      :region="selectedPlace?.region"
    />
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { usePlaceRecommendationStore } from '@/stores/placeRecommendation'
import { useRecommendationStore } from '@/stores/recommendation'
import { useScheduleStore } from '@/stores/schedule'
import { useThemeStore } from '@/stores/theme'
import PlaceReviewModal from '@/components/PlaceReviewModal.vue'

const router = useRouter()
const placeStore = usePlaceRecommendationStore()
const recommendationStore = useRecommendationStore()
const scheduleStore = useScheduleStore()
const themeStore = useThemeStore()

const isDarkMode = computed(() => themeStore.isDarkMode)

// 리뷰 모달 상태
const reviewModalOpen = ref(false)
const selectedPlace = ref(null)

function openReviewModal(place) {
  selectedPlace.value = place
  reviewModalOpen.value = true
}

const places = computed(() => {
  const result = placeStore.result || []
  // 최대 6개만 표시
  return result.slice(0, 6)
})
const selection = computed(() => placeStore.selection)
const selectedPeriod = computed(() => recommendationStore.selection.selectedPeriod)

const periodText = computed(() => {
  if (!selectedPeriod.value) return '기간 미선택'
  return `${formatDate(selectedPeriod.value.startDate)} ~ ${formatDate(selectedPeriod.value.endDate)}`
})

// 백엔드 값을 한글로 변환
const displayStyle = computed(() => {
  const styleMap = {
    'NATURE': '힐링',
    'CAFE': '작업몰입',
    'ACTIVITY': '액티비티'
  }
  return styleMap[selection.value.style] || selection.value.style
})

const displayBudget = computed(() => {
  const budgetMap = {
    'LOW': '가성비',
    'MID': '적당한',
    'HIGH': '프리미엄'
  }
  return budgetMap[selection.value.budget] || selection.value.budget
})

function formatDate(dateString) {
  if (!dateString) return ''
  const date = new Date(dateString)
  const month = date.getMonth() + 1
  const day = date.getDate()
  return `${month}월 ${day}일`
}

// 지역명을 PostListView 필터 형식으로 변환
function convertRegionToFilterFormat(regionName) {
  const regionMap = {
    '경기도': '경기',
    '강원도': '강원',
    '강원특별자치도': '강원',
    '경상남도': '경남',
    '경상북도': '경북',
    '광주광역시': '광주',
    '대구광역시': '대구',
    '대전광역시': '대전',
    '부산광역시': '부산',
    '서울특별시': '서울',
    '울산광역시': '울산',
    '인천광역시': '인천',
    '전라남도': '전남',
    '전라북도': '전북',
    '전북특별자치도': '전북',
    '제주특별자치도': '제주',
    '충청남도': '충남',
    '충청북도': '충북',
    '세종특별자치시': '세종'
  }
  return regionMap[regionName] || regionName
}

// 커뮤니티 페이지로 이동 (지역 필터 적용)
function goToCommunity(regionName) {
  const filterRegion = convertRegionToFilterFormat(regionName)
  router.push({
    path: '/posts',
    query: { region: filterRegion }
  })
}

function goSchedule(place) {
  // 선택한 장소와 기간 정보를 scheduleStore에 저장
  const period = selectedPeriod.value
  scheduleStore.setPrefilledPlace(place, period)
  
  // 일정 페이지로 이동 (자동으로 모달이 열림)
  router.push('/schedule')
}
</script>

<style scoped>
.place-result-wrapper {
  background: var(--color-background);
  min-height: calc(100vh - 64px);
  padding-top: 4rem;
  padding-bottom: 4rem;
  width: 100%;
  transition: background 0.3s ease;
}

.dark.place-result-wrapper {
  background: #0f172a;
}

.container-custom {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 1.5rem;
  width: 100%;
  box-sizing: border-box;
}

.step-indicator {
  display: inline-block;
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
  color: white;
  font-size: 0.75rem;
  font-weight: 600;
  padding: 0.5rem 1.25rem;
  border-radius: 20px;
  margin-bottom: 1.25rem;
  letter-spacing: 0.8px;
  text-transform: uppercase;
}

.page-title {
  font-size: clamp(1.75rem, 5vw, 2.5rem);
  font-weight: 800;
  color: #0f172a;
  margin-bottom: 1rem;
  letter-spacing: -0.02em;
  transition: color 0.3s ease;
}

.dark .page-title {
  color: #ffffff !important;
}

.page-subtitle {
  font-size: 1.0625rem;
  color: #64748b;
  margin-bottom: 0;
  font-weight: 400;
  transition: color 0.3s ease;
}

.dark .page-subtitle {
  color: #e2e8f0 !important;
}

/* Loading */
.loading-wrapper {
  text-align: center;
  padding: 5rem 0;
}

.loading-spinner {
  width: 60px;
  height: 60px;
  border: 4px solid #e2e8f0;
  border-top-color: #1e293b;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1.5rem;
}

.loading-text {
  font-size: 1.125rem;
  color: #64748b;
  font-weight: 500;
  transition: color 0.3s ease;
}

.dark .loading-text {
  color: #e2e8f0 !important;
}

/* Error & Empty */
.error-card,
.empty-card {
  background: white;
  border: 2px solid #e2e8f0;
  border-radius: 16px;
  padding: 4rem 2rem;
  text-align: center;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  max-width: 500px;
  margin: 0 auto;
  transition: all 0.3s ease;
}

.dark .error-card,
.dark .empty-card {
  background: rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 2px solid rgba(255, 255, 255, 0.15);
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.3);
}

.error-icon,
.empty-icon {
  width: 80px;
  height: 80px;
  font-size: 3rem;
  margin: 0 auto 1.5rem;
  background: #f8fafc;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 16px;
  border: 1px solid #e2e8f0;
  transition: all 0.3s ease;
}

.dark .error-icon,
.dark .empty-icon {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.error-title,
.empty-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #0f172a;
  margin-bottom: 0.75rem;
  letter-spacing: -0.01em;
  transition: color 0.3s ease;
}

.dark .error-title,
.dark .empty-title {
  color: #ffffff !important;
}

.error-message,
.empty-message {
  color: #64748b;
  margin-bottom: 2rem;
  font-size: 0.9375rem;
  transition: color 0.3s ease;
}

.dark .error-message,
.dark .empty-message {
  color: #e2e8f0 !important;
}

.btn-retry {
  appearance: none;
  border: 2px solid #1e293b;
  background: white;
  color: #1e293b;
  font-size: 1rem;
  font-weight: 600;
  padding: 0.875rem 2rem;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-retry:hover {
  background: #1e293b;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(30, 41, 59, 0.3);
}

/* Results */
.results-wrapper {
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
  padding: 0 1rem;
}

/* Summary Card */
.summary-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.4);
  border-radius: 16px;
  padding: 1.5rem 2rem;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.15);
  display: flex;
  flex-wrap: wrap;
  gap: 2rem;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.dark .summary-card {
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.15);
  box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.3);
}

.summary-item {
  text-align: center;
}

.summary-label {
  font-size: 0.8125rem;
  color: #94a3b8;
  margin-bottom: 0.375rem;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  transition: color 0.3s ease;
}

.dark .summary-label {
  color: #cbd5e1 !important;
}

.summary-value {
  font-size: 1.0625rem;
  font-weight: 700;
  color: #0f172a;
  letter-spacing: -0.01em;
  transition: color 0.3s ease;
}

.dark .summary-value {
  color: #ffffff !important;
}

.summary-divider {
  width: 1px;
  height: 40px;
  background: #e2e8f0;
  transition: background 0.3s ease;
}

.dark .summary-divider {
  background: rgba(255, 255, 255, 0.1);
}

/* Places Section */
.places-section {
  margin-top: 2.5rem;
  width: 100%;
  max-width: 100%;
}

.places-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1.5rem;
  width: 100%;
  max-width: 100%;
  box-sizing: border-box;
}

.place-card-wrapper {
  width: 100%;
  min-width: 0;
  box-sizing: border-box;
}

.place-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.4);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.15);
  transition: all 0.3s ease;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.dark .place-card {
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.15);
  box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.3);
}

.place-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.08);
  border-color: #cbd5e1;
}

.dark .place-card:hover {
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.5);
  border-color: rgba(255, 255, 255, 0.25);
}

.place-image {
  position: relative;
  aspect-ratio: 16/9;
  background: #f3f4f6;
  overflow: hidden;
}

.place-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.place-image-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 3rem;
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
}


.place-info {
  padding: 1.5rem;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.place-name {
  font-size: 1.125rem;
  font-weight: 700;
  color: #0f172a;
  margin-bottom: 0.375rem;
  letter-spacing: -0.01em;
  transition: color 0.3s ease;
}

.dark .place-name {
  color: #ffffff !important;
}

.place-region-wrapper {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1rem;
}

.place-region {
  font-size: 0.875rem;
  color: #64748b;
  transition: color 0.3s ease;
}

.dark .place-region {
  color: #cbd5e1 !important;
}

.region-community-btn {
  font-size: 0.75rem;
  font-weight: 500;
  color: #6366f1;
  background: rgba(99, 102, 241, 0.1);
  border: 1px solid rgba(99, 102, 241, 0.2);
  border-radius: 8px;
  padding: 0.375rem 0.75rem;
  cursor: pointer;
  transition: all 0.2s ease;
  white-space: nowrap;
}

.region-community-btn:hover {
  background: rgba(99, 102, 241, 0.15);
  border-color: rgba(99, 102, 241, 0.3);
  color: #4f46e5;
}

.dark .region-community-btn {
  color: #818cf8;
  background: rgba(99, 102, 241, 0.15);
  border-color: rgba(99, 102, 241, 0.3);
}

.dark .region-community-btn:hover {
  background: rgba(99, 102, 241, 0.25);
  border-color: rgba(99, 102, 241, 0.4);
  color: #a5b4fc;
}

.place-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.place-tag {
  background: #f8fafc;
  color: #334155;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 0.25rem 0.75rem;
  font-size: 0.75rem;
  font-weight: 500;
  transition: all 0.3s ease;
}

.dark .place-tag {
  background: rgba(255, 255, 255, 0.1);
  color: #e2e8f0 !important;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.reason-box {
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 0.875rem 1rem;
  margin-bottom: 1.25rem;
  flex: 1;
  transition: all 0.3s ease;
}

.dark .reason-box {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.reason-title {
  font-size: 0.8125rem;
  font-weight: 600;
  color: #0f172a;
  margin-bottom: 0.5rem;
  transition: color 0.3s ease;
}

.dark .reason-title {
  color: #ffffff !important;
}

.reason-text {
  font-size: 0.875rem;
  color: #64748b;
  line-height: 1.6;
  transition: color 0.3s ease;
  word-wrap: break-word;
  white-space: normal;
}

.dark .reason-text {
  color: #e2e8f0 !important;
}

.place-actions {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  width: 100%;
}

.btn-review {
  appearance: none;
  border: 2px solid #1e293b;
  background: white;
  color: #1e293b;
  font-size: 0.9375rem;
  font-weight: 600;
  padding: 0.875rem 1.25rem;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  letter-spacing: -0.01em;
}

.btn-review:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(30, 41, 59, 0.2);
  background: #f8fafc;
}

.dark .btn-review {
  background: rgba(255, 255, 255, 0.05);
  border-color: rgba(255, 255, 255, 0.3);
  color: #ffffff;
}

.dark .btn-review:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: rgba(255, 255, 255, 0.4);
}

.btn-select {
  appearance: none;
  border: none;
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
  color: white;
  font-size: 0.9375rem;
  font-weight: 600;
  padding: 0.875rem 1.25rem;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  letter-spacing: -0.01em;
}

.btn-select:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(30, 41, 59, 0.3);
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

@media (max-width: 992px) {
  .place-result-wrapper {
    padding-top: 3rem;
    padding-bottom: 3rem;
  }
  
  .summary-card {
    gap: 1.5rem;
    padding: 1.25rem 1.5rem;
  }
  
  .summary-divider {
    display: none;
  }
  
  .places-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 1.25rem;
  }
}

@media (max-width: 576px) {
  .place-result-wrapper {
    padding-top: 2rem;
    padding-bottom: 2rem;
  }
  
  .summary-card {
    gap: 1rem;
    padding: 1rem;
  }
  
  .place-image-placeholder {
    font-size: 2rem;
  }
  
  .places-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
}
</style>
