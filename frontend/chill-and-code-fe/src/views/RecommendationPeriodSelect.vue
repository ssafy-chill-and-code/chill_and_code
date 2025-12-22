<template>
  <div class="period-select-wrapper">
    <div class="container py-5">
      <!-- 헤더 -->
      <header class="text-center mb-5">
        <RouterLink class="back-link" to="/recommend">
          <span>←</span> 이전 단계로
        </RouterLink>
        <div class="header-content">
          <div class="step-indicator">STEP 2 of 3</div>
          <h1 class="page-title">이번 워케이션 희망 기간은?</h1>
          <p class="page-subtitle">최소 기간과 최대 기간을 설정해주세요</p>
        </div>
      </header>

      <!-- 입력 폼 -->
      <section class="form-wrapper">
        <div class="period-grid">
          <!-- 최소 기간 -->
          <div class="input-card">
            <label class="input-label">최소 기간</label>
            <div class="input-group-custom">
              <input 
                type="number" 
                v-model.number="minDays" 
                class="form-control-custom" 
                placeholder="2" 
                min="1" 
                max="30" 
              />
              <span class="input-suffix">일</span>
            </div>
            <div class="input-hint">1~30일 범위 내에서 입력하세요</div>
          </div>

          <!-- 최대 기간 -->
          <div class="input-card">
            <label class="input-label">최대 기간</label>
            <div class="input-group-custom">
              <input 
                type="number" 
                v-model.number="maxDays" 
                class="form-control-custom" 
                placeholder="5" 
                min="1" 
                max="30" 
              />
              <span class="input-suffix">일</span>
            </div>
            <div class="input-hint">1~30일 범위 내에서 입력하세요</div>
          </div>
        </div>

        <!-- 원격 근무 옵션 -->
        <div class="option-card">
          <div class="option-header">
            <label class="option-label" for="remoteToggle">
              <svg class="option-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.75 17L9 20l-1 1h8l-1-1-.75-3M3 13h18M5 17h14a2 2 0 002-2V5a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
              </svg>
              <span class="option-label-text">원격 근무 가능 일정도 워케이션 기간으로 고려</span>
            </label>
            <label class="toggle-switch">
              <input 
                id="remoteToggle"
                type="checkbox" 
                v-model="remoteWorkAllowed"
              />
              <span class="toggle-slider"></span>
            </label>
          </div>
          <p class="option-hint">활성화하면 원격 근무가 가능한 업무 일정도 워케이션 가능 기간으로 포함합니다</p>
        </div>

        <!-- 안내 배너 -->
        <div class="info-banner">
          <svg class="info-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          <span class="info-text">{{ minDays }}일 ~ {{ maxDays }}일 범위에서 AI가 최적의 기간을 분석합니다</span>
        </div>

        <!-- 에러 메시지 -->
        <div v-if="errorMessage" class="error-alert">
          <svg class="error-icon-svg" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          {{ errorMessage }}
        </div>

        <!-- CTA 버튼 -->
        <div class="cta-wrapper">
          <button 
            class="btn-primary-custom" 
            @click="goResult" 
            :disabled="recommendationStore.loading"
          >
            <span v-if="recommendationStore.loading" class="spinner"></span>
            <svg v-else class="btn-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
            </svg>
            {{ recommendationStore.loading ? '분석 중...' : '기간 추천받기' }}
          </button>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useRecommendationStore } from '@/stores/recommendation'

const router = useRouter()
const recommendationStore = useRecommendationStore()

const minDays = ref(2)
const maxDays = ref(5)
const remoteWorkAllowed = ref(false)
const errorMessage = ref('')

async function goResult() {
  // 유효성 검사
  if (!minDays.value || !maxDays.value) {
    errorMessage.value = '최소 기간과 최대 기간을 모두 입력해주세요.'
    return
  }
  
  if (minDays.value < 1 || minDays.value > 30 || maxDays.value < 1 || maxDays.value > 30) {
    errorMessage.value = '기간은 1~30일 범위 내에서 입력해주세요.'
    return
  }
  
  if (minDays.value > maxDays.value) {
    errorMessage.value = '최소 기간은 최대 기간보다 클 수 없습니다.'
    return
  }
  
  errorMessage.value = ''
  
  // 스토어에 선택 저장
  recommendationStore.updateSelection({
    minDays: minDays.value,
    maxDays: maxDays.value,
    remoteWorkAllowed: remoteWorkAllowed.value
  })
  
  try {
    // API 호출
    await recommendationStore.fetchPeriodRecommendation()
    // 성공 시 결과 페이지로 이동
    router.push('/recommend/period-result')
  } catch (e) {
    errorMessage.value = recommendationStore.error || '기간 추천을 불러오는데 실패했습니다.'
  }
}
</script>

<style scoped>
.period-select-wrapper {
  background: white;
  min-height: calc(100vh - 64px);
  padding-top: 4rem;
  padding-bottom: 4rem;
}

.back-link {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  color: #64748b;
  text-decoration: none;
  font-size: 0.875rem;
  font-weight: 500;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  transition: all 0.3s ease;
  margin-bottom: 2rem;
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
  font-size: 0.6875rem;
  font-weight: 600;
  padding: 0.4375rem 1rem;
  border-radius: 16px;
  margin-bottom: 1.5rem;
  letter-spacing: 0.05em;
  text-transform: uppercase;
}

.page-title {
  font-size: clamp(1.875rem, 5vw, 2.25rem);
  font-weight: 800;
  color: #0f172a;
  margin-bottom: 0.875rem;
  letter-spacing: -0.03em;
}

.page-subtitle {
  font-size: 1rem;
  color: #64748b;
  margin-bottom: 0;
  font-weight: 400;
  letter-spacing: -0.01em;
}

.form-wrapper {
  max-width: 720px;
  margin: 0 auto;
  padding: 3.5rem 0;
}

.period-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.input-card {
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 1.75rem 1.5rem;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.04);
  transition: all 0.25s ease;
}

.input-card:hover {
  border-color: #cbd5e1;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.06);
}

.input-label {
  display: block;
  font-size: 0.875rem;
  font-weight: 600;
  color: #334155;
  margin-bottom: 1rem;
  letter-spacing: -0.01em;
}

.input-group-custom {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 0.75rem;
  margin-bottom: 0.75rem;
}

.form-control-custom {
  width: 80px;
  height: 48px;
  font-size: 1.75rem;
  font-weight: 700;
  text-align: center;
  border: 1px solid #e2e8f0;
  border-radius: 10px;
  background: #f8fafc;
  color: #0f172a;
  transition: all 0.25s ease;
  letter-spacing: -0.02em;
}

.form-control-custom:focus {
  outline: none;
  border-color: #1e293b;
  background: white;
  box-shadow: 0 0 0 3px rgba(30, 41, 59, 0.08);
}

.form-control-custom::placeholder {
  color: #cbd5e1;
}

.input-suffix {
  font-size: 1.125rem;
  font-weight: 600;
  color: #64748b;
}

.input-hint {
  font-size: 0.75rem;
  color: #94a3b8;
  padding-left: 0.125rem;
}

/* 원격 근무 옵션 */
.option-card {
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.04);
  transition: all 0.25s ease;
  margin-bottom: 2rem;
}

.option-card:hover {
  border-color: #cbd5e1;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.06);
}

.option-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  margin-bottom: 0.75rem;
}

.option-label {
  display: flex;
  align-items: center;
  gap: 0.625rem;
  cursor: pointer;
  flex: 1;
}

.option-icon {
  width: 20px;
  height: 20px;
  color: #475569;
  flex-shrink: 0;
}

.option-label-text {
  font-size: 0.875rem;
  font-weight: 600;
  color: #334155;
  letter-spacing: -0.01em;
}

.option-hint {
  font-size: 0.75rem;
  color: #94a3b8;
  line-height: 1.5;
  margin: 0;
  padding-left: 1.875rem;
}

/* Toggle Switch */
.toggle-switch {
  position: relative;
  display: inline-block;
  width: 48px;
  height: 26px;
  flex-shrink: 0;
  cursor: pointer;
}

.toggle-switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.toggle-slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #cbd5e1;
  transition: 0.3s;
  border-radius: 26px;
}

.toggle-slider:before {
  position: absolute;
  content: "";
  height: 20px;
  width: 20px;
  left: 3px;
  bottom: 3px;
  background-color: white;
  transition: 0.3s;
  border-radius: 50%;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.toggle-switch input:checked + .toggle-slider {
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
}

.toggle-switch input:checked + .toggle-slider:before {
  transform: translateX(22px);
}

.toggle-switch input:focus + .toggle-slider {
  box-shadow: 0 0 0 3px rgba(30, 41, 59, 0.1);
}

.info-banner {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 0.625rem;
  background: #f8fafc;
  padding: 1rem 1.25rem;
  border-radius: 10px;
  margin-bottom: 1.5rem;
  border: 1px solid #e2e8f0;
}

.info-icon {
  width: 18px;
  height: 18px;
  color: #475569;
  flex-shrink: 0;
}

.info-text {
  font-size: 0.875rem;
  font-weight: 500;
  color: #475569;
  letter-spacing: -0.01em;
  line-height: 1.5;
}

.error-alert {
  background: #fef2f2;
  border: 1px solid #fecaca;
  color: #991b1b;
  padding: 0.875rem 1.25rem;
  border-radius: 10px;
  margin-bottom: 1.5rem;
  font-weight: 500;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 0.625rem;
  font-size: 0.875rem;
}

.error-icon-svg {
  width: 18px;
  height: 18px;
  flex-shrink: 0;
}

.cta-wrapper {
  text-align: center;
  margin-top: 2.5rem;
}

.btn-primary-custom {
  appearance: none;
  border: none;
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
  color: white;
  font-size: 1rem;
  font-weight: 600;
  padding: 1rem 2.25rem;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.25s ease;
  box-shadow: 0 2px 8px rgba(30, 41, 59, 0.2);
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  min-width: 220px;
  justify-content: center;
  letter-spacing: -0.01em;
}

.btn-primary-custom:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(30, 41, 59, 0.25);
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
}

.btn-primary-custom:active:not(:disabled) {
  transform: translateY(0);
}

.btn-primary-custom:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-icon {
  width: 18px;
  height: 18px;
}

.spinner {
  width: 18px;
  height: 18px;
  border: 2.5px solid rgba(255, 255, 255, 0.3);
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
  .period-select-wrapper {
    padding-top: 3rem;
    padding-bottom: 3rem;
  }
  
  .period-grid {
    gap: 1.25rem;
  }
  
  .input-card {
    padding: 1.5rem 1.25rem;
  }
}

@media (max-width: 576px) {
  .period-select-wrapper {
    padding-top: 2rem;
    padding-bottom: 2rem;
  }
  
  .period-grid {
    grid-template-columns: 1fr;
    gap: 1.25rem;
  }
  
  .form-control-custom {
    width: 72px;
    height: 44px;
    font-size: 1.5rem;
  }
  
  .input-suffix {
    font-size: 1rem;
  }
  
  .option-card {
    padding: 1.25rem;
  }
  
  .option-header {
    align-items: flex-start;
  }
  
  .option-label-text {
    font-size: 0.8125rem;
  }
  
  .option-hint {
    font-size: 0.6875rem;
    padding-left: 1.625rem;
  }
  
  .info-banner {
    padding: 0.875rem 1rem;
  }
  
  .info-text {
    font-size: 0.8125rem;
  }
}
</style>
