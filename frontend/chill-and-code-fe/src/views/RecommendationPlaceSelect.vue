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
            <h3 class="section-title">워케이션 스타일 <span class="required">*</span></h3>
          </div>
          <div class="style-grid">
            <button 
              type="button" 
              class="style-option" 
              :class="{ 'selected': selectedStyle === '힐링' }" 
              @click="selectStyle('힐링')"
            >
              <div class="option-check">
                <svg v-if="selectedStyle === '힐링'" class="check-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7" />
                </svg>
              </div>
              <div class="option-emoji">🌿</div>
              <div class="option-title">힐링</div>
              <div class="option-desc">자연 속에서 마음을 편안하게</div>
            </button>
            <button 
              type="button" 
              class="style-option" 
              :class="{ 'selected': selectedStyle === '작업몰입' }" 
              @click="selectStyle('작업몰입')"
            >
              <div class="option-check">
                <svg v-if="selectedStyle === '작업몰입'" class="check-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7" />
                </svg>
              </div>
              <div class="option-emoji">💻</div>
              <div class="option-title">작업몰입</div>
              <div class="option-desc">집중력 있는 업무 환경</div>
            </button>
            <button 
              type="button" 
              class="style-option" 
              :class="{ 'selected': selectedStyle === '액티비티' }" 
              @click="selectStyle('액티비티')"
            >
              <div class="option-check">
                <svg v-if="selectedStyle === '액티비티'" class="check-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7" />
                </svg>
              </div>
              <div class="option-emoji">🏃‍♂️</div>
              <div class="option-title">액티비티</div>
              <div class="option-desc">다양한 활동과 경험</div>
            </button>
          </div>
        </section>

        <!-- 월간 예산 -->
        <section class="form-section">
          <div class="section-header">
            <h3 class="section-title">💰 월간 예산</h3>
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
          <svg class="error-icon-svg" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          {{ errorMessage }}
        </div>

        <!-- 제출 버튼 -->
        <div class="submit-section">
          <button 
            class="btn-submit" 
            @click="goResult" 
            :disabled="placeStore.loading"
          >
            <span v-if="placeStore.loading" class="flex items-center gap-2">
              <svg class="animate-spin h-5 w-5" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              추천 중...
            </span>
            <span v-else>장소 추천받기</span>
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
  background: linear-gradient(135deg, #0ea5e9 0%, #0284c7 100%);
  color: white;
  font-size: 0.6875rem;
  font-weight: 600;
  padding: 0.375rem 1rem;
  border-radius: 0.375rem;
  margin-bottom: 1.25rem;
  letter-spacing: 0.05em;
  text-transform: uppercase;
  box-shadow: 0 2px 4px rgba(14, 165, 233, 0.2);
}

.page-title {
  font-size: clamp(1.75rem, 5vw, 2.5rem);
  font-weight: 800;
  background: linear-gradient(135deg, #0ea5e9 0%, #1e293b 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
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
  border: 1px solid #e5e7eb;
  border-radius: 1rem;
  padding: 2rem;
  margin-bottom: 1.5rem;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  transition: all 0.2s ease;
}

.form-section:hover {
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  border-color: #d1d5db;
}

.section-header {
  margin-bottom: 1.25rem;
  padding-bottom: 0.75rem;
  border-bottom: 1px solid #f1f5f9;
  position: relative;
}

.section-header::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  width: 48px;
  height: 2px;
  background: linear-gradient(90deg, #0ea5e9 0%, #64748b 100%);
}

.section-title {
  font-size: 0.875rem;
  font-weight: 700;
  color: #111827;
  margin: 0;
  letter-spacing: 0.025em;
  text-transform: uppercase;
}

.required {
  color: #ef4444;
  font-size: 0.875rem;
}

.optional {
  color: #9ca3af;
  font-size: 0.75rem;
  font-weight: 500;
  text-transform: none;
}

/* Style Grid */
.style-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1rem;
}

/* Style Options */
.style-option {
  appearance: none;
  border: 1px solid #d1d5db;
  background: white;
  border-radius: 0.75rem;
  padding: 1.25rem;
  width: 100%;
  cursor: pointer;
  transition: all 0.2s ease;
  text-align: left;
  position: relative;
}

.style-option:hover {
  border-color: #1e293b;
  background: #f9fafb;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.style-option.selected {
  border-color: #0ea5e9;
  background: linear-gradient(135deg, #f0f9ff 0%, #ffffff 100%);
  box-shadow: 0 0 0 2px rgba(14, 165, 233, 0.15), 0 4px 6px rgba(14, 165, 233, 0.1);
}

.option-emoji {
  font-size: 1.75rem;
  margin-bottom: 0.5rem;
  filter: grayscale(0.3);
  transition: filter 0.2s ease;
}

.style-option.selected .option-emoji {
  filter: grayscale(0);
}

.option-check {
  position: absolute;
  top: 1rem;
  right: 1rem;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: linear-gradient(135deg, #0ea5e9 0%, #0284c7 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.2s ease;
  box-shadow: 0 2px 4px rgba(14, 165, 233, 0.3);
}

.style-option.selected .option-check {
  opacity: 1;
}

.check-icon {
  width: 16px;
  height: 16px;
  color: white;
}

.option-title {
  font-size: 1rem;
  font-weight: 600;
  color: #111827;
  margin-bottom: 0.375rem;
  letter-spacing: -0.01em;
}

.option-desc {
  font-size: 0.8125rem;
  color: #6b7280;
  line-height: 1.5;
}

/* Budget Card */
.budget-card {
  background: linear-gradient(135deg, #f9fafb 0%, #ffffff 100%);
  padding: 1.75rem;
  border-radius: 0.75rem;
  border: 1px solid #e5e7eb;
  position: relative;
  overflow: hidden;
}

.budget-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, #0ea5e9 0%, #64748b 50%, #0ea5e9 100%);
  opacity: 0.4;
}

.budget-display {
  text-align: center;
  margin-bottom: 1.5rem;
}

.budget-amount {
  font-size: 2.5rem;
  font-weight: 700;
  background: linear-gradient(135deg, #0f172a 0%, #475569 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: -0.02em;
}

.budget-unit {
  font-size: 1.25rem;
  color: #6b7280;
  margin-left: 0.5rem;
  font-weight: 600;
}

.budget-slider {
  width: 100%;
  height: 6px;
  border-radius: 3px;
  background: linear-gradient(90deg, #e5e7eb 0%, #cbd5e1 50%, #e5e7eb 100%);
  outline: none;
  -webkit-appearance: none;
  margin-bottom: 0.75rem;
  position: relative;
}

.budget-slider::-webkit-slider-thumb {
  -webkit-appearance: none;
  appearance: none;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: linear-gradient(135deg, #0ea5e9 0%, #0284c7 100%);
  cursor: pointer;
  box-shadow: 0 2px 4px rgba(14, 165, 233, 0.4);
  transition: all 0.2s ease;
  border: 2px solid white;
}

.budget-slider::-webkit-slider-thumb:hover {
  box-shadow: 0 3px 10px rgba(14, 165, 233, 0.5);
  transform: scale(1.15);
}

.budget-slider::-moz-range-thumb {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: linear-gradient(135deg, #0ea5e9 0%, #0284c7 100%);
  cursor: pointer;
  border: 2px solid white;
  box-shadow: 0 2px 4px rgba(14, 165, 233, 0.4);
  transition: all 0.2s ease;
}

.budget-slider::-moz-range-thumb:hover {
  box-shadow: 0 3px 10px rgba(14, 165, 233, 0.5);
  transform: scale(1.15);
}

.budget-labels {
  display: flex;
  justify-content: space-between;
  font-size: 0.8125rem;
  color: #9ca3af;
  font-weight: 500;
}

/* Select */
.form-select-custom {
  width: 100%;
  padding: 1rem 1.25rem;
  font-size: 0.9375rem;
  border: 1px solid #d1d5db;
  border-radius: 0.75rem;
  background: white;
  cursor: pointer;
  transition: all 0.2s ease;
  color: #111827;
  font-weight: 400;
}

.form-select-custom:focus {
  outline: none;
  border-color: #1e293b;
  box-shadow: 0 0 0 3px rgba(30, 41, 59, 0.1);
}

/* Region Tags */
.region-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.region-tag {
  appearance: none;
  border: 1px solid #d1d5db;
  background: white;
  color: #6b7280;
  padding: 0.5rem 1rem;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.region-tag:hover {
  border-color: #0ea5e9;
  color: #0ea5e9;
  background: #f0f9ff;
}

.region-tag.active {
  border-color: #0ea5e9;
  background: linear-gradient(135deg, #0ea5e9 0%, #0284c7 100%);
  color: white;
  box-shadow: 0 2px 4px rgba(14, 165, 233, 0.3);
}

/* Error Alert */
.error-alert {
  background: #fef2f2;
  border: 1px solid #fecaca;
  color: #991b1b;
  padding: 0.75rem 1rem;
  border-radius: 0.5rem;
  text-align: left;
  margin-bottom: 1.5rem;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
}

.error-icon-svg {
  width: 18px;
  height: 18px;
  flex-shrink: 0;
}

/* Submit */
.submit-section {
  text-align: center;
  padding-top: 1rem;
}

.btn-submit {
  appearance: none;
  border: none;
  background: linear-gradient(135deg, #0ea5e9 0%, #0284c7 100%);
  color: white;
  font-size: 1rem;
  font-weight: 600;
  padding: 1rem 2rem;
  border-radius: 0.75rem;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 4px 6px rgba(14, 165, 233, 0.2);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 200px;
  position: relative;
  overflow: hidden;
}

.btn-submit::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.15), transparent);
  transition: left 0.5s ease;
}

.btn-submit:hover:not(:disabled)::before {
  left: 100%;
}

.btn-submit:hover:not(:disabled) {
  background: linear-gradient(135deg, #0284c7 0%, #0369a1 100%);
  box-shadow: 0 6px 12px rgba(14, 165, 233, 0.3);
  transform: translateY(-2px);
}

.btn-submit:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.animate-spin {
  animation: spin 1s linear infinite;
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
  
  .style-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .budget-amount {
    font-size: 2.25rem;
  }
}

@media (max-width: 576px) {
  .place-select-wrapper {
    padding-top: 2rem;
    padding-bottom: 2rem;
  }
  
  .form-section {
    padding: 1.25rem;
  }
  
  .style-grid {
    grid-template-columns: 1fr;
  }
  
  .budget-amount {
    font-size: 2rem;
  }
  
  .btn-submit {
    width: 100%;
    min-width: unset;
  }
}
</style>
