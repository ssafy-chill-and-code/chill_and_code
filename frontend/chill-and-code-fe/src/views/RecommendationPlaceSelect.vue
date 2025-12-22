<template>
  <div class="place-select-wrapper">
    <div class="container py-5">
      <!-- 헤더 -->
      <header class="text-center mb-5">
        <button class="back-link" @click="goBack">
          <span>←</span> 이전 단계로
        </button>
        <div class="header-content">
          <div class="step-indicator">STEP 3 of 3</div>
          <h1 class="page-title">당신의 선호도를 알려주세요</h1>
          <p class="page-subtitle">{{ periodText }}의 워케이션을 위한 옵션을 선택하세요</p>
        </div>
      </header>

      <!-- 폼 래퍼 -->
      <div class="form-container">
        <!-- 워케이션 스타일 -->
        <section class="form-section">
          <div class="section-header">
            <span class="section-icon">⚡</span>
            <h3 class="section-title">워케이션 스타일 <span class="required">*</span></h3>
          </div>
          <div class="row g-3">
            <div class="col-12 col-md-4">
              <button 
                type="button" 
                class="style-option" 
                :class="{ 'selected': selectedStyle === '힐링' }" 
                @click="selectStyle('힐링')"
              >
                <div class="option-icon">🌿</div>
                <div class="option-title">힐링</div>
                <div class="option-desc">자연 속에서 마음을 편안하게</div>
              </button>
            </div>
            <div class="col-12 col-md-4">
              <button 
                type="button" 
                class="style-option" 
                :class="{ 'selected': selectedStyle === '작업몰입' }" 
                @click="selectStyle('작업몰입')"
              >
                <div class="option-icon">💻</div>
                <div class="option-title">작업몰입</div>
                <div class="option-desc">집중력 있는 업무 환경</div>
              </button>
            </div>
            <div class="col-12 col-md-4">
              <button 
                type="button" 
                class="style-option" 
                :class="{ 'selected': selectedStyle === '액티비티' }" 
                @click="selectStyle('액티비티')"
              >
                <div class="option-icon">🏃‍♂️</div>
                <div class="option-title">액티비티</div>
                <div class="option-desc">다양한 활동과 경험</div>
              </button>
            </div>
          </div>
        </section>

        <!-- 월간 예산 -->
        <section class="form-section">
          <div class="section-header">
            <span class="section-icon">💰</span>
            <h3 class="section-title">월간 예산</h3>
          </div>
          <div class="budget-card">
            <div class="budget-display">
              <span class="budget-amount">{{ budget }}</span>
              <span class="budget-unit">만원</span>
            </div>
            <input 
              type="range" 
              v-model.number="budget" 
              class="budget-slider" 
              min="50" 
              max="500" 
              step="10" 
            />
            <div class="budget-labels">
              <span>50만원</span>
              <span>500만원</span>
            </div>
          </div>
        </section>

        <!-- 이동수단 -->
        <section class="form-section">
          <div class="section-header">
            <span class="section-icon">🚗</span>
            <h3 class="section-title">이동수단 <span class="optional">(선택)</span></h3>
          </div>
          <select v-model="transport" class="form-select-custom">
            <option value="">선호하는 이동수단을 선택하세요</option>
            <option value="자동차">🚗 자동차</option>
            <option value="기차">🚄 기차</option>
            <option value="비행기">✈️ 비행기</option>
            <option value="버스">🚌 버스</option>
          </select>
        </section>

        <!-- 선호 지역 -->
        <section class="form-section">
          <div class="section-header">
            <span class="section-icon">📍</span>
            <h3 class="section-title">선호 지역 <span class="optional">(선택)</span></h3>
          </div>
          <div class="region-tags">
            <button 
              v-for="region in regions" 
              :key="region" 
              type="button" 
              class="region-tag" 
              :class="{ 'active': selectedRegions.includes(region) }"
              @click="toggleRegion(region)"
            >
              {{ region }}
            </button>
          </div>
        </section>

        <!-- 에러 메시지 -->
        <div v-if="errorMessage" class="error-alert">
          <span>⚠️</span> {{ errorMessage }}
        </div>

        <!-- 제출 버튼 -->
        <div class="submit-section">
          <button 
            class="btn-submit" 
            @click="goResult" 
            :disabled="placeStore.loading"
          >
            <span v-if="!placeStore.loading">🎯</span>
            <span v-if="placeStore.loading" class="spinner"></span>
            {{ placeStore.loading ? '추천 중...' : '장소 추천받기' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { usePlaceRecommendationStore } from '@/stores/placeRecommendation'
import { useRecommendationStore } from '@/stores/recommendation'

const router = useRouter()
const placeStore = usePlaceRecommendationStore()
const recommendationStore = useRecommendationStore()

const selectedStyle = ref('')
const budget = ref(150)
const transport = ref('')
const selectedRegions = ref([])
const errorMessage = ref('')

const regions = [
  '경기도',
  '강원도',
  '강원특별자치도',
  '경상남도',
  '경상북도',
  '광주광역시',
  '대구광역시',
  '대전광역시',
  '부산광역시',
  '서울특별시',
  '울산광역시',
  '인천광역시',
  '전라남도',
  '전라북도',
  '전북특별자치도',
  '제주특별자치도',
  '충청남도',
  '충청북도'
]

const selectedPeriod = computed(() => recommendationStore.selection.selectedPeriod)
const periodText = computed(() => {
  if (!selectedPeriod.value) return '기간 미선택'
  return `${formatDate(selectedPeriod.value.startDate)} ~ ${formatDate(selectedPeriod.value.endDate)}`
})

function formatDate(dateString) {
  if (!dateString) return ''
  const date = new Date(dateString)
  const month = date.getMonth() + 1
  const day = date.getDate()
  return `${month}월 ${day}일`
}

function selectStyle(style) {
  selectedStyle.value = style
}

function toggleRegion(region) {
  const index = selectedRegions.value.indexOf(region)
  if (index > -1) {
    selectedRegions.value.splice(index, 1)
  } else {
    selectedRegions.value.push(region)
  }
}

function convertStyleToBackend(style) {
  const styleMap = {
    '힐링': 'NATURE',
    '작업몰입': 'CAFE',
    '액티비티': 'ACTIVITY'
  }
  return styleMap[style] || 'ACTIVITY'
}

function convertBudgetToBackend(budgetValue) {
  if (budgetValue <= 150) return 'LOW'
  if (budgetValue <= 300) return 'MID'
  return 'HIGH'
}

async function goResult() {
  if (!selectedStyle.value) {
    errorMessage.value = '워케이션 스타일을 선택해주세요.'
    return
  }
  
  errorMessage.value = ''
  
  const backendStyle = convertStyleToBackend(selectedStyle.value)
  const backendBudget = convertBudgetToBackend(budget.value)
  
  placeStore.updateSelection({
    style: backendStyle,
    budget: backendBudget,
    region: selectedRegions.value.length > 0 ? selectedRegions.value.join(',') : '',
    transport: transport.value
  })
  
  try {
    await placeStore.fetchPlaceRecommendation()
    router.push('/recommend/place/result')
  } catch (e) {
    errorMessage.value = placeStore.error || '장소 추천을 불러오는데 실패했습니다.'
  }
}

function goBack() {
  router.back()
}
</script>

<style scoped>
.place-select-wrapper {
  background: white;
  min-height: calc(100vh - 64px);
  padding-top: 4rem;
  padding-bottom: 4rem;
}

.back-link {
  appearance: none;
  border: none;
  background: transparent;
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  color: #64748b;
  text-decoration: none;
  font-size: 0.9rem;
  font-weight: 500;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  transition: all 0.3s ease;
  margin-bottom: 2rem;
  cursor: pointer;
}

.back-link:hover {
  background: #f8fafc;
  color: #1e293b;
}

.header-content {
  margin-top: 2rem;
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

.form-container {
  max-width: 900px;
  margin: 0 auto;
}

.form-section {
  background: white;
  border: 2px solid #e2e8f0;
  border-radius: 16px;
  padding: 2rem;
  margin-bottom: 1.5rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.form-section:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.06);
  border-color: #cbd5e1;
}

.section-header {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1.5rem;
}

.section-icon {
  font-size: 1.5rem;
  background: #f8fafc;
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 10px;
  border: 1px solid #e2e8f0;
}

.section-title {
  font-size: 1.1875rem;
  font-weight: 700;
  color: #0f172a;
  margin: 0;
  letter-spacing: -0.01em;
}

.required {
  color: #ef4444;
  font-size: 0.875rem;
}

.optional {
  color: #94a3b8;
  font-size: 0.8125rem;
  font-weight: 500;
}

/* Style Options */
.style-option {
  appearance: none;
  border: 2px solid #e2e8f0;
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  width: 100%;
  cursor: pointer;
  transition: all 0.3s ease;
  text-align: center;
}

.style-option:hover {
  border-color: #1e293b;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.style-option.selected {
  border-color: #1e293b;
  background: #f8fafc;
  box-shadow: 0 4px 12px rgba(30, 41, 59, 0.12);
}

.option-icon {
  font-size: 2.5rem;
  margin-bottom: 0.75rem;
}

.option-title {
  font-size: 1.0625rem;
  font-weight: 700;
  color: #0f172a;
  margin-bottom: 0.5rem;
  letter-spacing: -0.01em;
}

.option-desc {
  font-size: 0.875rem;
  color: #64748b;
}

/* Budget Card */
.budget-card {
  background: #f8fafc;
  padding: 2rem;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}

.budget-display {
  text-align: center;
  margin-bottom: 1.5rem;
}

.budget-amount {
  font-size: 3rem;
  font-weight: 800;
  color: #1e293b;
  letter-spacing: -0.02em;
}

.budget-unit {
  font-size: 1.5rem;
  color: #64748b;
  margin-left: 0.5rem;
  font-weight: 600;
}

.budget-slider {
  width: 100%;
  height: 6px;
  border-radius: 3px;
  background: #e2e8f0;
  outline: none;
  -webkit-appearance: none;
  margin-bottom: 0.75rem;
}

.budget-slider::-webkit-slider-thumb {
  -webkit-appearance: none;
  appearance: none;
  width: 22px;
  height: 22px;
  border-radius: 50%;
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(30, 41, 59, 0.4);
}

.budget-slider::-moz-range-thumb {
  width: 22px;
  height: 22px;
  border-radius: 50%;
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
  cursor: pointer;
  border: none;
  box-shadow: 0 2px 8px rgba(30, 41, 59, 0.4);
}

.budget-labels {
  display: flex;
  justify-content: space-between;
  font-size: 0.875rem;
  color: #94a3b8;
  font-weight: 500;
}

/* Select */
.form-select-custom {
  width: 100%;
  padding: 1rem 1.25rem;
  font-size: 1rem;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  background: white;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #0f172a;
  font-weight: 500;
}

.form-select-custom:focus {
  outline: none;
  border-color: #1e293b;
  box-shadow: 0 0 0 4px rgba(30, 41, 59, 0.1);
}

/* Region Tags */
.region-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.625rem;
}

.region-tag {
  appearance: none;
  border: 2px solid #e2e8f0;
  background: white;
  color: #64748b;
  padding: 0.625rem 1.125rem;
  border-radius: 20px;
  font-size: 0.9375rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.region-tag:hover {
  border-color: #1e293b;
  color: #1e293b;
  background: #f8fafc;
}

.region-tag.active {
  border-color: #1e293b;
  background: #1e293b;
  color: white;
  box-shadow: 0 2px 8px rgba(30, 41, 59, 0.3);
}

/* Error Alert */
.error-alert {
  background: #fef2f2;
  border: 1px solid #fecaca;
  color: #991b1b;
  padding: 1rem 1.5rem;
  border-radius: 12px;
  text-align: center;
  margin-bottom: 1.5rem;
  font-weight: 500;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  font-size: 0.9375rem;
}

/* Submit */
.submit-section {
  text-align: center;
  padding-top: 1rem;
}

.btn-submit {
  appearance: none;
  border: none;
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
  color: white;
  font-size: 1.0625rem;
  font-weight: 600;
  padding: 1.125rem 2.5rem;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(30, 41, 59, 0.3);
  display: inline-flex;
  align-items: center;
  gap: 0.75rem;
  min-width: 240px;
  justify-content: center;
  letter-spacing: -0.01em;
}

.btn-submit:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(30, 41, 59, 0.4);
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
}

.btn-submit:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.spinner {
  width: 20px;
  height: 20px;
  border: 3px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

@media (max-width: 992px) {
  .place-select-wrapper {
    padding-top: 3rem;
    padding-bottom: 3rem;
  }
  
  .budget-amount {
    font-size: 2.5rem;
  }
  
  .section-icon {
    width: 40px;
    height: 40px;
    font-size: 1.375rem;
  }
}

@media (max-width: 576px) {
  .place-select-wrapper {
    padding-top: 2rem;
    padding-bottom: 2rem;
  }
  
  .form-section {
    padding: 1.5rem;
  }
  
  .option-icon {
    font-size: 2rem;
  }
  
  .budget-amount {
    font-size: 2rem;
  }
  
  .section-icon {
    width: 36px;
    height: 36px;
    font-size: 1.25rem;
  }
}
</style>
