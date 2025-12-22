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

        <!-- 예산 -->
        <section class="form-section">
          <div class="section-header">
            <span class="section-icon">💰</span>
            <h3 class="section-title">예산</h3>
          </div>
          <div class="row g-3">
            <div class="col-12 col-md-4">
              <button 
                type="button" 
                class="budget-option" 
                :class="{ 'selected': budget === '가성비' }" 
                @click="selectBudget('가성비')"
              >
                <div class="option-icon">💵</div>
                <div class="option-title">가성비</div>
              </button>
            </div>
            <div class="col-12 col-md-4">
              <button 
                type="button" 
                class="budget-option" 
                :class="{ 'selected': budget === '적당한' }" 
                @click="selectBudget('적당한')"
              >
                <div class="option-icon">💳</div>
                <div class="option-title">적당한</div>
              </button>
            </div>
            <div class="col-12 col-md-4">
              <button 
                type="button" 
                class="budget-option" 
                :class="{ 'selected': budget === '프리미엄' }" 
                @click="selectBudget('프리미엄')"
              >
                <div class="option-icon">💎</div>
                <div class="option-title">프리미엄</div>
              </button>
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
            <h3 class="section-title">선호 지역 <span class="optional">(최대 3개 선택)</span></h3>
          </div>
          <div class="region-selection-info">
            <span class="selection-count">{{ selectedRegions.length }} / 3 선택됨</span>
          </div>
          <div class="region-tags">
            <button 
              v-for="region in regions" 
              :key="region" 
              type="button" 
              class="region-tag" 
              :class="{ 'active': selectedRegions.includes(region), 'disabled': !selectedRegions.includes(region) && selectedRegions.length >= 3 }"
              :disabled="!selectedRegions.includes(region) && selectedRegions.length >= 3"
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
const budget = ref('적당한')
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

function selectBudget(budgetLevel) {
  budget.value = budgetLevel
}

function toggleRegion(region) {
  const index = selectedRegions.value.indexOf(region)
  if (index > -1) {
    // 이미 선택된 지역이면 제거
    selectedRegions.value.splice(index, 1)
  } else {
    // 최대 3개까지만 선택 가능
    if (selectedRegions.value.length < 3) {
      selectedRegions.value.push(region)
    }
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

function convertBudgetToBackend(budgetText) {
  const budgetMap = {
    '가성비': 'LOW',
    '적당한': 'MID',
    '프리미엄': 'HIGH'
  }
  return budgetMap[budgetText] || 'MID'
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
  background: linear-gradient(180deg, #f8f9fa 0%, #ffffff 100%);
  min-height: calc(100vh - 64px);
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
  background: white;
  color: #667eea;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.header-content {
  margin-top: 2rem;
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

.form-container {
  max-width: 900px;
  margin: 0 auto;
}

.form-section {
  background: white;
  border-radius: 20px;
  padding: 2rem;
  margin-bottom: 1.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  transition: all 0.3s ease;
}

.form-section:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.section-header {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1.5rem;
}

.section-icon {
  font-size: 1.5rem;
}

.section-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: #1a202c;
  margin: 0;
}

.required {
  color: #ef4444;
  font-size: 0.9rem;
}

.optional {
  color: #94a3b8;
  font-size: 0.85rem;
  font-weight: 500;
}

/* Style Options */
.style-option {
  appearance: none;
  border: 2px solid #e5e7eb;
  background: white;
  border-radius: 16px;
  padding: 1.5rem;
  width: 100%;
  cursor: pointer;
  transition: all 0.3s ease;
  text-align: center;
}

.style-option:hover {
  border-color: #667eea;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
}

.style-option.selected {
  border-color: #667eea;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2);
}

.option-icon {
  font-size: 2.5rem;
  margin-bottom: 0.75rem;
}

.option-title {
  font-size: 1.1rem;
  font-weight: 700;
  color: #1a202c;
  margin-bottom: 0.5rem;
}

.option-desc {
  font-size: 0.9rem;
  color: #64748b;
}

/* Budget Options */
.budget-option {
  appearance: none;
  border: 2px solid #e5e7eb;
  background: white;
  border-radius: 16px;
  padding: 1.5rem;
  width: 100%;
  cursor: pointer;
  transition: all 0.3s ease;
  text-align: center;
}

.budget-option:hover {
  border-color: #667eea;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
}

.budget-option.selected {
  border-color: #667eea;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2);
}

/* Select */
.form-select-custom {
  width: 100%;
  padding: 1rem 1.25rem;
  font-size: 1rem;
  border: 2px solid #e5e7eb;
  border-radius: 12px;
  background: white;
  cursor: pointer;
  transition: all 0.3s ease;
}

.form-select-custom:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
}

/* Region Tags */
.region-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.75rem;
}

.region-tag {
  appearance: none;
  border: 2px solid #e5e7eb;
  background: white;
  color: #64748b;
  padding: 0.625rem 1.25rem;
  border-radius: 24px;
  font-size: 0.95rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.region-tag:hover {
  border-color: #667eea;
  color: #667eea;
}

.region-tag.active {
  border-color: #667eea;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
}

.region-tag.disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.region-selection-info {
  margin-bottom: 1rem;
  text-align: center;
}

.selection-count {
  display: inline-block;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
  color: #667eea;
  font-size: 0.9rem;
  font-weight: 600;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  border: 1px solid #667eea;
}

/* Error Alert */
.error-alert {
  background: #fee2e2;
  border: 2px solid #fecaca;
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
}

/* Submit */
.submit-section {
  text-align: center;
  padding-top: 1rem;
}

.btn-submit {
  appearance: none;
  border: none;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 1.1rem;
  font-weight: 600;
  padding: 1.25rem 3rem;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
  display: inline-flex;
  align-items: center;
  gap: 0.75rem;
  min-width: 240px;
  justify-content: center;
}

.btn-submit:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

.btn-submit:disabled {
  opacity: 0.7;
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
  .page-title {
    font-size: 2rem;
  }
  
  .budget-amount {
    font-size: 2.5rem;
  }
}

@media (max-width: 576px) {
  .page-title {
    font-size: 1.75rem;
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
}
</style>
