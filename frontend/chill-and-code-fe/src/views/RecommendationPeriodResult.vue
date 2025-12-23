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
          <div class="button-group">
            <button class="btn-select primary" @click="goPlace(result.primary)">
              <span>✓</span> 이 기간으로 선택하기
            </button>
            <button class="btn-select edit" @click="showEditModal = true">
              <svg class="btn-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
              </svg>
              기간 수정하기
            </button>
          </div>
        </div>

        <!-- 대안 추천 -->
        <div v-if="result.alternatives && result.alternatives.length > 0" class="alternatives-section">
          <h3 class="section-title">다른 추천 기간</h3>
          <div class="alternatives-grid">
            <div v-for="(alt, index) in result.alternatives" :key="index" class="result-card alternative-card">
              <div class="period-info">
                <div class="date-range-alt">
                  <span class="date-text-alt">{{ formatDate(alt.startDate) }}</span>
                  <span class="date-separator-alt">~</span>
                  <span class="date-text-alt">{{ formatDate(alt.endDate) }}</span>
                </div>
                <div class="duration-alt">{{ alt.durationDays }}일간</div>
              </div>
              <button class="btn-select secondary" @click="goPlace(alt)">
                <svg class="btn-icon-small" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                </svg>
                이 기간 선택
              </button>
            </div>
          </div>
        </div>

      </div>
    </div>

    <!-- 기간 수정 모달 -->
    <div v-if="showEditModal" class="modal-overlay" @click.self="showEditModal = false">
      <div class="modal-content">
        <div class="modal-header">
          <h2 class="modal-title">기간 수정</h2>
          <button class="modal-close" @click="showEditModal = false">
            <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        
        <div class="modal-body">
          <div class="form-group">
            <label class="form-label">시작 날짜 <span class="required">*</span></label>
            <input 
              v-model="editedStartDate" 
              type="date" 
              class="form-input"
              :min="minDate"
            />
          </div>
          
          <div class="form-group">
            <label class="form-label">종료 날짜 <span class="required">*</span></label>
            <input 
              v-model="editedEndDate" 
              type="date" 
              class="form-input"
              :min="editedStartDate || minDate"
            />
          </div>

          <div v-if="editedDuration" class="duration-preview">
            총 {{ editedDuration }}일간의 워케이션
          </div>
        </div>

        <div class="modal-footer">
          <button class="btn-cancel" @click="showEditModal = false">
            이전으로
          </button>
          <button class="btn-continue" @click="handleContinue" :disabled="!isValidPeriod">
            이 기간으로 계속하기
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, watch, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useRecommendationStore } from '@/stores/recommendation'

const router = useRouter()
const recommendationStore = useRecommendationStore()

const showEditModal = ref(false)
const editedStartDate = ref('')
const editedEndDate = ref('')

// 오늘 날짜를 YYYY-MM-DD 형식으로
const minDate = new Date().toISOString().split('T')[0]

const result = computed(() => {
  const res = recommendationStore.result
  console.log('=== 기간 추천 결과 디버깅 ===')
  console.log('전체 result:', res)
  console.log('primary:', res?.primary)
  console.log('alternatives:', res?.alternatives)
  console.log('alternatives 개수:', res?.alternatives?.length)
  console.log('========================')
  return res
})

// result가 변경될 때마다 로그 출력
watch(result, (newVal) => {
  console.log('🔄 result 변경됨:', newVal)
}, { deep: true, immediate: true })

// 모달 열 때 현재 추천 기간으로 초기화
watch(showEditModal, (isOpen) => {
  if (isOpen && result.value?.primary) {
    const primary = result.value.primary
    editedStartDate.value = formatDateForInput(primary.startDate)
    editedEndDate.value = formatDateForInput(primary.endDate)
  }
})

// 수정된 기간의 일수 계산
const editedDuration = computed(() => {
  if (!editedStartDate.value || !editedEndDate.value) return null
  const start = new Date(editedStartDate.value)
  const end = new Date(editedEndDate.value)
  const diffTime = Math.abs(end - start)
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1
  return diffDays
})

// 유효한 기간인지 확인
const isValidPeriod = computed(() => {
  if (!editedStartDate.value || !editedEndDate.value) return false
  const start = new Date(editedStartDate.value)
  const end = new Date(editedEndDate.value)
  return end >= start
})

function formatDate(dateString) {
  if (!dateString) return ''
  const date = new Date(dateString)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}.${month}.${day}`
}

function formatDateForInput(dateString) {
  if (!dateString) return ''
  const date = new Date(dateString)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

function goPlace(period) {
  console.log('📍 선택된 기간:', period)
  // 선택한 기간을 스토어에 저장하고 장소 선택 화면으로 이동
  recommendationStore.updateSelection({
    selectedPeriod: period
  })
  router.push('/recommend/place')
}

function handleContinue() {
  if (!isValidPeriod.value) return
  
  // 수정된 기간으로 PeriodCandidate 객체 생성
  const editedPeriod = {
    startDate: editedStartDate.value,
    endDate: editedEndDate.value,
    durationDays: editedDuration.value,
    type: 'CUSTOM' // 사용자 커스텀 기간
  }
  
  // 수정된 기간을 스토어에 저장하고 장소 선택 화면으로 이동
  recommendationStore.updateSelection({
    selectedPeriod: editedPeriod
  })
  
  showEditModal.value = false
  router.push('/recommend/place')
}
</script>

<style scoped>
.period-result-wrapper {
  background: white;
  min-height: calc(100vh - 64px);
  padding-top: 4rem;
  padding-bottom: 4rem;
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
  max-width: 1000px;
  margin: 0 auto;
}

.result-card {
  background: white;
  border: 2px solid #e2e8f0;
  border-radius: 16px;
  padding: 2.5rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.result-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.08);
  border-color: #cbd5e1;
}

.primary-card {
  border-color: #1e293b;
  border-width: 2px;
  margin-bottom: 3rem;
  background: linear-gradient(to bottom, #f8fafc 0%, white 100%);
}

.card-badge {
  display: inline-block;
  background: linear-gradient(135deg, #6366f1 0%, #4f46e5 100%);
  color: white;
  font-size: 0.875rem;
  font-weight: 600;
  padding: 0.5rem 1.25rem;
  border-radius: 20px;
  margin-bottom: 2rem;
  letter-spacing: 0.025em;
  box-shadow: 0 2px 8px rgba(99, 102, 241, 0.3);
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
  font-size: 1.625rem;
  font-weight: 700;
  color: #0f172a;
  background: white;
  padding: 0.75rem 1.5rem;
  border-radius: 12px;
  border: 2px solid #e2e8f0;
  letter-spacing: -0.01em;
}

.date-separator {
  font-size: 1.375rem;
  color: #94a3b8;
  font-weight: 600;
}

.duration {
  font-size: 1.0625rem;
  color: #334155;
  font-weight: 600;
}

.reason-section {
  background: #f8fafc;
  padding: 1.5rem;
  border-radius: 12px;
  margin-bottom: 2rem;
  border: 1px solid #e2e8f0;
}

.reason-title {
  font-size: 1rem;
  font-weight: 600;
  color: #0f172a;
  margin-bottom: 0.75rem;
}

.reason-text {
  font-size: 0.9375rem;
  color: #64748b;
  line-height: 1.7;
}

.btn-select {
  appearance: none;
  border: none;
  width: 100%;
  font-size: 1.0625rem;
  font-weight: 600;
  padding: 1.125rem;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.75rem;
  letter-spacing: -0.01em;
}

.btn-select.primary {
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
  color: white;
  box-shadow: 0 4px 12px rgba(30, 41, 59, 0.3);
}

.btn-select.primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(30, 41, 59, 0.4);
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
}

.button-group {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.btn-select.edit {
  background: white;
  color: #64748b;
  border: 2px solid #e2e8f0;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.04);
}

.btn-select.edit:hover {
  background: #f8fafc;
  border-color: #cbd5e1;
  color: #334155;
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.btn-icon {
  width: 18px;
  height: 18px;
  flex-shrink: 0;
}

.btn-select.secondary {
  background: white;
  color: #334155;
  border: 1px solid #e2e8f0;
  font-size: 0.9375rem;
  padding: 0.875rem 1.25rem;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.04);
}

.btn-select.secondary:hover {
  background: #f8fafc;
  border-color: #1e293b;
  color: #1e293b;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

/* Alternatives */
.alternatives-section {
  margin-top: 3rem;
}

.section-title {
  font-size: 1.375rem;
  font-weight: 700;
  color: #0f172a;
  margin-bottom: 1.5rem;
  text-align: center;
  letter-spacing: -0.02em;
}

.alternatives-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 1.25rem;
}

.alternative-card {
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.date-range-alt {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  margin-bottom: 0.625rem;
}

.date-text-alt {
  font-size: 1.125rem;
  font-weight: 700;
  color: #0f172a;
  letter-spacing: -0.01em;
}

.date-separator-alt {
  font-size: 1rem;
  color: #94a3b8;
  font-weight: 600;
}

.duration-alt {
  font-size: 0.875rem;
  color: #64748b;
  text-align: center;
  font-weight: 500;
}

.btn-icon-small {
  width: 16px;
  height: 16px;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

@media (max-width: 992px) {
  .period-result-wrapper {
    padding-top: 3rem;
    padding-bottom: 3rem;
  }
  
  .date-text {
    font-size: 1.375rem;
    padding: 0.625rem 1.25rem;
  }
}


/* 모달 스타일 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 1rem;
}

.modal-content {
  background: white;
  border-radius: 16px;
  width: 100%;
  max-width: 500px;
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
  animation: slideUp 0.3s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1.5rem 2rem;
  border-bottom: 1px solid #e2e8f0;
}

.modal-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #0f172a;
  margin: 0;
}

.modal-close {
  appearance: none;
  border: none;
  background: none;
  color: #64748b;
  cursor: pointer;
  padding: 0.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  transition: all 0.2s ease;
}

.modal-close:hover {
  background: #f1f5f9;
  color: #0f172a;
}

.modal-close svg {
  width: 24px;
  height: 24px;
}

.modal-body {
  padding: 2rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-label {
  display: block;
  font-size: 0.9375rem;
  font-weight: 600;
  color: #334155;
  margin-bottom: 0.5rem;
}

.required {
  color: #ef4444;
}

.form-input {
  width: 100%;
  padding: 0.875rem 1rem;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  font-size: 1rem;
  color: #0f172a;
  transition: all 0.2s ease;
}

.form-input:focus {
  outline: none;
  border-color: #1e293b;
  box-shadow: 0 0 0 3px rgba(30, 41, 59, 0.1);
}

.duration-preview {
  background: #f8fafc;
  padding: 1rem;
  border-radius: 12px;
  text-align: center;
  font-size: 1rem;
  font-weight: 600;
  color: #334155;
  border: 1px solid #e2e8f0;
}

.modal-footer {
  display: flex;
  gap: 1rem;
  padding: 1.5rem 2rem;
  border-top: 1px solid #e2e8f0;
}

.btn-cancel,
.btn-continue {
  flex: 1;
  appearance: none;
  border: none;
  font-size: 1rem;
  font-weight: 600;
  padding: 0.875rem 1.5rem;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-cancel {
  background: white;
  color: #64748b;
  border: 2px solid #e2e8f0;
}

.btn-cancel:hover {
  background: #f8fafc;
  border-color: #cbd5e1;
  color: #334155;
}

.btn-continue {
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
  color: white;
  box-shadow: 0 4px 12px rgba(30, 41, 59, 0.3);
}

.btn-continue:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(30, 41, 59, 0.4);
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
}

.btn-continue:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

@media (max-width: 576px) {
  .period-result-wrapper {
    padding-top: 2rem;
    padding-bottom: 2rem;
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
  
  .date-text {
    font-size: 1.25rem;
  }

  .modal-content {
    margin: 1rem;
  }

  .modal-header,
  .modal-body,
  .modal-footer {
    padding: 1.25rem 1.5rem;
  }

  .modal-footer {
    flex-direction: column;
  }

  .btn-cancel,
  .btn-continue {
    width: 100%;
  }
}
</style>
