<template>
  <div class="place-result-wrapper">
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
                  <div class="match-badge">⭐ {{ Math.round(place.trendScore) }}%</div>
                </div>

                <!-- 정보 -->
                <div class="place-info">
                  <h6 class="place-name">{{ place.name }}</h6>
                  <div class="place-region">{{ place.region }}</div>

                  <!-- 태그 -->
                  <div class="place-tags">
                    <span v-for="tag in place.tags" :key="tag" class="place-tag">{{ tag }}</span>
                  </div>

                  <!-- 가격 -->
                  <div class="place-price">월 {{ formatPrice(place.score) }}</div>

                  <!-- 추천 이유 -->
                  <div class="reason-box">
                    <div class="reason-title">📝 추천이유</div>
                    <div class="reason-text">{{ place.reasonText || '당신의 예산과 일정에 완벽하게 맞습니다' }}</div>
                  </div>

                  <!-- 버튼 -->
                  <button class="btn-select" @click="goSchedule(place)">
                    <span>✓</span> 이 장소로 일정 만들기
                  </button>
                </div>
              </div>
            </div>
          </div>
        </section>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { usePlaceRecommendationStore } from '@/stores/placeRecommendation'
import { useRecommendationStore } from '@/stores/recommendation'

const router = useRouter()
const placeStore = usePlaceRecommendationStore()
const recommendationStore = useRecommendationStore()

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

function formatPrice(score) {
  // 점수를 가격으로 변환 (예시)
  if (score >= 80) return '180만원'
  if (score >= 60) return '150만원'
  return '120만원'
}

function goSchedule(place) {
  // 선택한 장소로 일정 만들기
  router.push('/schedule/create')
}
</script>

<style scoped>
.place-result-wrapper {
  background: white;
  min-height: calc(100vh - 64px);
  padding-top: 4rem;
  padding-bottom: 4rem;
  width: 100%;
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
}

.page-subtitle {
  font-size: 1.0625rem;
  color: #64748b;
  margin-bottom: 0;
  font-weight: 400;
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
}

.error-title,
.empty-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #0f172a;
  margin-bottom: 0.75rem;
  letter-spacing: -0.01em;
}

.error-message,
.empty-message {
  color: #64748b;
  margin-bottom: 2rem;
  font-size: 0.9375rem;
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
  background: white;
  border: 2px solid #e2e8f0;
  border-radius: 16px;
  padding: 1.5rem 2rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  display: flex;
  flex-wrap: wrap;
  gap: 2rem;
  align-items: center;
  justify-content: center;
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
}

.summary-value {
  font-size: 1.0625rem;
  font-weight: 700;
  color: #0f172a;
  letter-spacing: -0.01em;
}

.summary-divider {
  width: 1px;
  height: 40px;
  background: #e2e8f0;
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
  background: white;
  border: 2px solid #e2e8f0;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.place-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.08);
  border-color: #cbd5e1;
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

.match-badge {
  position: absolute;
  left: 12px;
  top: 12px;
  background: rgba(30, 41, 59, 0.95);
  color: white;
  font-size: 0.75rem;
  font-weight: 700;
  padding: 0.375rem 0.75rem;
  border-radius: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(8px);
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
}

.place-region {
  font-size: 0.875rem;
  color: #64748b;
  margin-bottom: 1rem;
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
}

.place-price {
  font-size: 1.125rem;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 1rem;
  letter-spacing: -0.01em;
}

.reason-box {
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 0.875rem 1rem;
  margin-bottom: 1.25rem;
  flex: 1;
}

.reason-title {
  font-size: 0.8125rem;
  font-weight: 600;
  color: #0f172a;
  margin-bottom: 0.5rem;
}

.reason-text {
  font-size: 0.875rem;
  color: #64748b;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
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
