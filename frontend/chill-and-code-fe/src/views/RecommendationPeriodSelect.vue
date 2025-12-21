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
        <div class="row g-4 justify-content-center">
          <!-- 최소 기간 -->
          <div class="col-12 col-md-6 col-lg-5">
            <div class="input-card">
              <div class="card-icon">📅</div>
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
              <div class="input-hint">1~30일 범위</div>
            </div>
          </div>

          <!-- 최대 기간 -->
          <div class="col-12 col-md-6 col-lg-5">
            <div class="input-card">
              <div class="card-icon">📆</div>
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
              <div class="input-hint">1~30일 범위</div>
            </div>
          </div>
        </div>

        <!-- 안내 배너 -->
        <div class="info-banner">
          <span class="info-icon">💡</span>
          <span class="info-text">{{ minDays }} ~ {{ maxDays }}일 범위에서 추천받습니다</span>
        </div>

        <!-- 에러 메시지 -->
        <div v-if="errorMessage" class="error-alert">
          <span>⚠️</span> {{ errorMessage }}
        </div>

        <!-- CTA 버튼 -->
        <div class="cta-wrapper">
          <button 
            class="btn-primary-custom" 
            @click="goResult" 
            :disabled="recommendationStore.loading"
          >
            <span v-if="!recommendationStore.loading">🎯</span>
            <span v-if="recommendationStore.loading" class="spinner"></span>
            {{ recommendationStore.loading ? '추천 중...' : '기간 추천받기' }}
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
    maxDays: maxDays.value
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
  background: linear-gradient(180deg, #f8f9fa 0%, #ffffff 100%);
  min-height: calc(100vh - 64px);
}

.back-link {
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

.form-wrapper {
  max-width: 900px;
  margin: 0 auto;
  padding: 2rem 0;
}

.input-card {
  background: white;
  border-radius: 20px;
  padding: 2.5rem 2rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  text-align: center;
}

.input-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.1);
}

.card-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.input-label {
  display: block;
  font-size: 1.1rem;
  font-weight: 600;
  color: #1a202c;
  margin-bottom: 1rem;
}

.input-group-custom {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  margin-bottom: 0.75rem;
}

.form-control-custom {
  width: 120px;
  height: 60px;
  font-size: 2rem;
  font-weight: 700;
  text-align: center;
  border: 3px solid #e5e7eb;
  border-radius: 16px;
  background: #f8f9fa;
  transition: all 0.3s ease;
}

.form-control-custom:focus {
  outline: none;
  border-color: #667eea;
  background: white;
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.1);
}

.input-suffix {
  font-size: 1.5rem;
  font-weight: 600;
  color: #64748b;
}

.input-hint {
  font-size: 0.9rem;
  color: #94a3b8;
}

.info-banner {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.75rem;
  background: linear-gradient(135deg, #e0e7ff 0%, #ede9fe 100%);
  padding: 1.25rem 2rem;
  border-radius: 16px;
  margin: 2.5rem auto 1.5rem;
  max-width: 600px;
  border: 2px solid rgba(102, 126, 234, 0.2);
}

.info-icon {
  font-size: 1.5rem;
}

.info-text {
  font-size: 1.1rem;
  font-weight: 600;
  color: #4c1d95;
}

.error-alert {
  background: #fee2e2;
  border: 2px solid #fecaca;
  color: #991b1b;
  padding: 1rem 1.5rem;
  border-radius: 12px;
  text-align: center;
  margin: 1.5rem auto;
  max-width: 600px;
  font-weight: 500;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.cta-wrapper {
  text-align: center;
  margin-top: 2.5rem;
}

.btn-primary-custom {
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

.btn-primary-custom:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

.btn-primary-custom:active:not(:disabled) {
  transform: translateY(0);
}

.btn-primary-custom:disabled {
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
  
  .input-card {
    padding: 2rem 1.5rem;
  }
}

@media (max-width: 576px) {
  .page-title {
    font-size: 1.75rem;
  }
  
  .form-control-custom {
    width: 100px;
    height: 50px;
    font-size: 1.5rem;
  }
  
  .input-suffix {
    font-size: 1.25rem;
  }
}
</style>
