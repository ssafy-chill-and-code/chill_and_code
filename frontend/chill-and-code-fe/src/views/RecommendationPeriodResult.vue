<template>
  <div class="period-result-wrapper">
    <div class="container py-5">
      <!-- 헤더 -->
      <header class="text-center mb-5">
        <div class="step-indicator">STEP 2 결과</div>
        <h1 class="page-title">추천 기간</h1>
        <p class="page-subtitle">AI가 분석한 최적의 워케이션 기간입니다</p>
      </header>

      <!-- 로딩 중 -->
      <div v-if="recommendationStore.loading" class="loading-wrapper">
        <div class="loading-spinner"></div>
        <div class="loading-text">기간을 추천하고 있습니다...</div>
      </div>

      <!-- 에러 메시지 -->
      <div v-else-if="recommendationStore.error" class="error-card">
        <div class="error-icon">😔</div>
        <div class="error-title">오류가 발생했습니다</div>
        <div class="error-message">{{ recommendationStore.error }}</div>
        <button class="btn-retry" @click="router.push('/recommend/period')">다시 시도</button>
      </div>

      <!-- 결과가 없을 때 -->
      <div v-else-if="!result || !result.primary" class="empty-card">
        <div class="empty-icon">🔍</div>
        <div class="empty-title">추천 가능한 기간이 없습니다</div>
        <div class="empty-message">조건을 변경해주세요</div>
        <button class="btn-retry" @click="router.push('/recommend/period')">조건 변경하기</button>
      </div>

      <!-- 추천 결과 -->
      <div v-else class="results-wrapper">
        <!-- 최우선 추천 -->
        <div class="result-card primary-card">
          <div class="card-badge">🌟 최우선 추천</div>
          <div class="period-info">
            <div class="date-range">
              <span class="date-text">{{ formatDate(result.primary.startDate) }}</span>
              <span class="date-separator">~</span>
              <span class="date-text">{{ formatDate(result.primary.endDate) }}</span>
            </div>
            <div class="duration">{{ result.primary.durationDays }}일간의 워케이션</div>
          </div>
          <div class="reason-section">
            <div class="reason-title">📝 추천 이유</div>
            <div class="reason-text">{{ result.reason || '이 기간이 가장 적합합니다.' }}</div>
          </div>
          <button class="btn-select primary" @click="goPlace(result.primary)">
            <span>✓</span> 이 기간으로 선택하기
          </button>
        </div>

        <!-- 대안 추천 -->
        <div v-if="result.alternatives && result.alternatives.length > 0" class="alternatives-section">
          <h3 class="section-title">다른 추천 기간</h3>
          <div class="row g-3">
            <div v-for="(alt, index) in result.alternatives" :key="index" class="col-12 col-md-6 col-lg-4">
              <div class="result-card alternative-card">
                <div class="period-info">
                  <div class="date-range-small">
                    {{ formatDate(alt.startDate) }} ~ {{ formatDate(alt.endDate) }}
                  </div>
                  <div class="duration-small">{{ alt.durationDays }}일</div>
                </div>
                <button class="btn-select secondary" @click="goPlace(alt)">
                  선택하기
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useRecommendationStore } from '@/stores/recommendation'

const router = useRouter()
const recommendationStore = useRecommendationStore()

const result = computed(() => recommendationStore.result)

function formatDate(dateString) {
  if (!dateString) return ''
  const date = new Date(dateString)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}.${month}.${day}`
}

function goPlace(period) {
  // 선택한 기간을 스토어에 저장하고 장소 선택 화면으로 이동
  recommendationStore.updateSelection({
    selectedPeriod: period
  })
  router.push('/recommend/place')
}
</script>

<style scoped>
.period-result-wrapper {
  background: linear-gradient(180deg, #f8f9fa 0%, #ffffff 100%);
  min-height: calc(100vh - 64px);
}

.step-indicator {
  display: inline-block;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 0.75rem;
  font-weight: 600;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  margin-bottom: 1rem;
  letter-spacing: 0.5px;
}

.page-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #1a202c;
  margin-bottom: 0.75rem;
}

.page-subtitle {
  font-size: 1.1rem;
  color: #64748b;
  margin-bottom: 0;
}

/* Loading */
.loading-wrapper {
  text-align: center;
  padding: 5rem 0;
}

.loading-spinner {
  width: 60px;
  height: 60px;
  border: 6px solid rgba(102, 126, 234, 0.2);
  border-top-color: #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1.5rem;
}

.loading-text {
  font-size: 1.2rem;
  color: #64748b;
}

/* Error & Empty */
.error-card,
.empty-card {
  background: white;
  border-radius: 24px;
  padding: 4rem 2rem;
  text-align: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  max-width: 500px;
  margin: 0 auto;
}

.error-icon,
.empty-icon {
  font-size: 4rem;
  margin-bottom: 1.5rem;
}

.error-title,
.empty-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #1a202c;
  margin-bottom: 0.75rem;
}

.error-message,
.empty-message {
  color: #64748b;
  margin-bottom: 2rem;
}

.btn-retry {
  appearance: none;
  border: 2px solid #667eea;
  background: white;
  color: #667eea;
  font-size: 1rem;
  font-weight: 600;
  padding: 0.875rem 2rem;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-retry:hover {
  background: #667eea;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

/* Results */
.results-wrapper {
  max-width: 1000px;
  margin: 0 auto;
}

.result-card {
  background: white;
  border-radius: 24px;
  padding: 2.5rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.result-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.1);
}

.primary-card {
  border: 3px solid #667eea;
  margin-bottom: 3rem;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.05) 0%, rgba(118, 75, 162, 0.05) 100%);
}

.card-badge {
  display: inline-block;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 0.9rem;
  font-weight: 600;
  padding: 0.5rem 1.25rem;
  border-radius: 20px;
  margin-bottom: 2rem;
}

.period-info {
  text-align: center;
  margin-bottom: 2rem;
}

.date-range {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  margin-bottom: 1rem;
}

.date-text {
  font-size: 1.75rem;
  font-weight: 700;
  color: #1a202c;
  background: white;
  padding: 0.75rem 1.5rem;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.date-separator {
  font-size: 1.5rem;
  color: #94a3b8;
  font-weight: 600;
}

.duration {
  font-size: 1.1rem;
  color: #667eea;
  font-weight: 600;
}

.reason-section {
  background: white;
  padding: 1.5rem;
  border-radius: 16px;
  margin-bottom: 2rem;
  border: 2px solid #f1f5f9;
}

.reason-title {
  font-size: 1rem;
  font-weight: 600;
  color: #1a202c;
  margin-bottom: 0.75rem;
}

.reason-text {
  font-size: 1.05rem;
  color: #64748b;
  line-height: 1.7;
}

.btn-select {
  appearance: none;
  border: none;
  width: 100%;
  font-size: 1.1rem;
  font-weight: 600;
  padding: 1.25rem;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.75rem;
}

.btn-select.primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.btn-select.primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

.btn-select.secondary {
  background: white;
  color: #667eea;
  border: 2px solid #667eea;
  font-size: 1rem;
  padding: 1rem;
}

.btn-select.secondary:hover {
  background: #667eea;
  color: white;
  transform: translateY(-2px);
}

/* Alternatives */
.alternatives-section {
  margin-top: 3rem;
}

.section-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #1a202c;
  margin-bottom: 1.5rem;
  text-align: center;
}

.alternative-card {
  padding: 1.5rem;
}

.date-range-small {
  font-size: 1rem;
  font-weight: 600;
  color: #1a202c;
  margin-bottom: 0.5rem;
}

.duration-small {
  font-size: 0.9rem;
  color: #64748b;
  margin-bottom: 1rem;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

@media (max-width: 992px) {
  .page-title {
    font-size: 2rem;
  }
  
  .date-text {
    font-size: 1.25rem;
    padding: 0.5rem 1rem;
  }
}

@media (max-width: 576px) {
  .page-title {
    font-size: 1.75rem;
  }
  
  .result-card {
    padding: 1.5rem;
  }
  
  .date-range {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .date-separator {
    transform: rotate(90deg);
  }
}
</style>
