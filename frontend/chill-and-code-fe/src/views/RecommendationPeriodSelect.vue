<template>
  <div class="period-select-wrapper">
    <div class="container py-5">
      <!-- 헤더 -->
      <header class="mb-5">
        <RouterLink class="back-link" to="/recommend">
          <img src="@/assets/arrow/back_arrow.png" alt="뒤로 가기" class="back-arrow-img" />
        </RouterLink>
        <div class="header-content text-center">
          <div class="step-indicator">STEP 2 of 3</div>
          <h1 class="page-title">이번 워케이션 희망 기간은?</h1>
          <p class="page-subtitle">최소 기간과 최대 기간을 설정해주세요</p>
        </div>
      </header>

      <!-- 입력 폼 -->
      <div class="form-container">
        <!-- 기간 입력 -->
        <section class="form-section">
          <div class="section-header">
            <h3 class="section-title">워케이션 기간 <span class="required">*</span></h3>
          </div>
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
        </section>

        <!-- 원격 근무 옵션 -->
        <section class="form-section">
          <div class="section-header">
            <h3 class="section-title">원격 근무 옵션 <span class="optional">(선택)</span></h3>
          </div>
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
        </section>

        <!-- 안내 배너 -->
        <section class="form-section">
          <div class="info-banner">
            <svg class="info-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <span class="info-text">{{ minDays }}일 ~ {{ maxDays }}일 범위에서 AI가 최적의 기간을 분석합니다</span>
          </div>
        </section>

        <!-- 에러 메시지 -->
        <div v-if="errorMessage" class="error-alert">
          <svg class="error-icon-svg" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          {{ errorMessage }}
        </div>

        <!-- CTA 버튼 -->
        <div class="submit-section">
          <button 
            class="btn-submit" 
            @click="goResult" 
            :disabled="recommendationStore.loading"
          >
            <span v-if="recommendationStore.loading" class="flex items-center gap-2">
              <svg class="animate-spin h-5 w-5" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              분석 중...
            </span>
            <span v-else>기간 추천받기</span>
          </button>
        </div>
      </div>
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
  background: var(--color-background);
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
  padding: 0.5rem;
  border-radius: 8px;
  transition: all 0.3s ease;
  margin-bottom: 2rem;
  cursor: pointer;
}

.back-link:hover {
  background: #f8fafc;
  transform: translateX(-4px);
}

.back-arrow-img {
  width: 24px;
  height: 24px;
  transition: all 0.3s ease;
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
  padding: 0.375rem 1rem;
  border-radius: 0.375rem;
  margin-bottom: 1.25rem;
  letter-spacing: 0.05em;
  text-transform: uppercase;
  box-shadow: 0 2px 8px rgba(30, 41, 59, 0.2);
}

.page-title {
  font-size: clamp(1.75rem, 5vw, 2.5rem);
  font-weight: 800;
  background: linear-gradient(135deg, #1e293b 0%, #475569 100%);
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
  background: linear-gradient(90deg, #6366f1 0%, #1e293b 100%);
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

.period-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.input-card {
  background: #f9fafb;
  border: 1px solid #e5e7eb;
  border-radius: 0.75rem;
  padding: 1.5rem;
  transition: all 0.2s ease;
}

.input-card:hover {
  border-color: #d1d5db;
  background: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
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
  border-color: #6366f1;
  background: white;
  box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);
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
  background: #f9fafb;
  border: 1px solid #e5e7eb;
  border-radius: 0.75rem;
  padding: 1.5rem;
  transition: all 0.2s ease;
}

.option-card:hover {
  border-color: #d1d5db;
  background: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
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
  background: linear-gradient(135deg, #6366f1 0%, #4f46e5 100%);
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
  gap: 0.75rem;
  background: linear-gradient(135deg, #f8fafc 0%, #ffffff 100%);
  padding: 1rem 1.25rem;
  border-radius: 0.75rem;
  border: 1px solid #e5e7eb;
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

.submit-section {
  text-align: center;
  padding-top: 1rem;
}

.btn-submit {
  appearance: none;
  border: none;
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
  color: white;
  font-size: 1rem;
  font-weight: 600;
  padding: 1rem 2rem;
  border-radius: 0.75rem;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 4px 12px rgba(30, 41, 59, 0.25);
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
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
  box-shadow: 0 8px 20px rgba(30, 41, 59, 0.35);
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
  .period-select-wrapper {
    padding-top: 3rem;
    padding-bottom: 3rem;
  }
  
  .form-section {
    padding: 1.5rem;
  }
  
  .period-grid {
    gap: 1rem;
  }
  
  .input-card {
    padding: 1.25rem;
  }
}

@media (max-width: 576px) {
  .period-select-wrapper {
    padding-top: 2rem;
    padding-bottom: 2rem;
  }
  
  .form-section {
    padding: 1.25rem;
  }
  
  .period-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
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
  
  .btn-submit {
    width: 100%;
    min-width: unset;
  }
}
</style>
