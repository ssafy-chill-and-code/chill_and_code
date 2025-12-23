<template>
  <div class="style-select-wrapper">
    <div class="container py-5">
      <!-- 헤더 -->
      <header class="mb-5">
        <RouterLink class="back-link" to="/">
          <img src="@/assets/arrow/back_arrow.png" alt="뒤로 가기" class="back-arrow-img" />
        </RouterLink>
        <div class="header-content text-center">
          <div class="step-indicator">STEP 1 of 3</div>
          <h1 class="page-title">이번 워케이션 스타일은?</h1>
          <p class="page-subtitle">당신의 워케이션 목표를 선택해주세요</p>
        </div>
      </header>

      <!-- 스타일 카드 -->
      <div class="form-container">
        <section class="form-section">
          <div class="section-header">
            <h3 class="section-title">워케이션 스타일 <span class="required">*</span></h3>
          </div>
          <div class="cards-grid">
            <button 
              type="button" 
              class="style-card" 
              :class="{ 'selected': selectedStyle === 'RELAX' }"
              @click="goPeriod('RELAX')"
            >
              <div class="option-check">
                <svg v-if="selectedStyle === 'RELAX'" class="check-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7" />
                </svg>
              </div>
              <div class="card-content">
                <div class="icon">🏝️</div>
                <h3 class="card-title">휴양형</h3>
                <ul class="card-features">
                  <li><span>✓</span> 휴식/리프레시 우선</li>
                  <li><span>✓</span> 여유로운 일정 구성</li>
                  <li><span>✓</span> 자연과 함께하는 힐링</li>
                </ul>
              </div>
            </button>
            <button 
              type="button" 
              class="style-card" 
              :class="{ 'selected': selectedStyle === 'WORK' }"
              @click="goPeriod('WORK')"
            >
              <div class="option-check">
                <svg v-if="selectedStyle === 'WORK'" class="check-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7" />
                </svg>
              </div>
              <div class="card-content">
                <div class="icon">💻</div>
                <h3 class="card-title">업무형</h3>
                <ul class="card-features">
                  <li><span>✓</span> 집중 업무 환경 중시</li>
                  <li><span>✓</span> 시간 관리 최우선</li>
                  <li><span>✓</span> 생산성 극대화</li>
                </ul>
              </div>
            </button>
            <button 
              type="button" 
              class="style-card" 
              :class="{ 'selected': selectedStyle === 'BALANCE' }"
              @click="goPeriod('BALANCE')"
            >
              <div class="option-check">
                <svg v-if="selectedStyle === 'BALANCE'" class="check-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7" />
                </svg>
              </div>
              <div class="card-content">
                <div class="icon">⚖️</div>
                <h3 class="card-title">밸런스형</h3>
                <ul class="card-features">
                  <li><span>✓</span> 일·휴식 균형 추구</li>
                  <li><span>✓</span> 유연한 스케줄</li>
                  <li><span>✓</span> 적절한 업무와 여가</li>
                </ul>
              </div>
            </button>
          </div>
        </section>
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

const selectedStyle = ref('')

function goPeriod(styleType) {
  selectedStyle.value = styleType
  // 스타일 선택 후 기간 선택 화면으로 이동
  recommendationStore.updateSelection({ style: styleType })
  router.push('/recommend/period')
}
</script>

<style scoped>
.style-select-wrapper {
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

.cards-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1rem;
}

.style-card {
  position: relative;
  appearance: none;
  border: 1px solid rgba(255, 255, 255, 0.4);
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-radius: 0.75rem;
  padding: 0;
  width: 100%;
  height: 100%;
  min-height: 320px;
  cursor: pointer;
  overflow: hidden;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  transition: all 0.2s ease;
}

.style-card:hover {
  border-color: #1e293b;
  background: #f9fafb;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  transform: translateY(-2px);
}

.style-card:active {
  transform: translateY(0);
}

.option-check {
  position: absolute;
  top: 1rem;
  right: 1rem;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: linear-gradient(135deg, #6366f1 0%, #4f46e5 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.2s ease;
  box-shadow: 0 2px 8px rgba(99, 102, 241, 0.3);
  z-index: 10;
}

.style-card.selected .option-check {
  opacity: 1;
}

.check-icon {
  width: 16px;
  height: 16px;
  color: white;
}

.card-content {
  position: relative;
  padding: 2rem 1.5rem;
  text-align: center;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.icon {
  width: 64px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2.25rem;
  margin-bottom: 1.25rem;
  background: #f8fafc;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
  transition: all 0.2s ease;
  filter: grayscale(0.2);
}

.style-card:hover .icon {
  background: #f1f5f9;
  transform: scale(1.05);
  border-color: #cbd5e1;
  filter: grayscale(0);
}

.style-card.selected .icon {
  filter: grayscale(0);
}

.card-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: #111827;
  margin-bottom: 1.25rem;
  letter-spacing: -0.01em;
}

.card-features {
  list-style: none;
  padding: 0;
  margin: 0;
  text-align: left;
  width: 100%;
  max-width: 240px;
}

.card-features li {
  display: flex;
  align-items: center;
  gap: 0.625rem;
  color: #64748b;
  font-size: 0.875rem;
  margin-bottom: 0.75rem;
  padding: 0.5rem 0.625rem;
  border-radius: 6px;
  transition: all 0.2s ease;
}

.style-card:hover .card-features li {
  background: #f8fafc;
  color: #334155;
}

.style-card.selected {
  border-color: #6366f1;
  background: linear-gradient(135deg, #eef2ff 0%, #ffffff 100%);
  box-shadow: 0 0 0 2px rgba(99, 102, 241, 0.15), 0 4px 12px rgba(99, 102, 241, 0.1);
}

.style-card.selected .card-features li {
  color: #475569;
}

.card-features li span {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 16px;
  height: 16px;
  background: #6366f1;
  color: white;
  border-radius: 50%;
  font-size: 0.625rem;
  font-weight: 700;
  flex-shrink: 0;
}

@media (max-width: 992px) {
  .style-select-wrapper {
    padding-top: 3rem;
    padding-bottom: 3rem;
  }
  
  .form-section {
    padding: 1.5rem;
  }
  
  .cards-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 1rem;
  }
  
  .style-card {
    min-height: 300px;
  }
  
  .icon {
    width: 56px;
    height: 56px;
    font-size: 2rem;
  }
}

@media (max-width: 576px) {
  .style-select-wrapper {
    padding-top: 2rem;
    padding-bottom: 2rem;
  }
  
  .form-section {
    padding: 1.25rem;
  }
  
  .cards-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  
  .icon {
    width: 52px;
    height: 52px;
    font-size: 1.875rem;
  }
  
  .card-content {
    padding: 1.75rem 1.25rem;
  }
  
  .card-title {
    font-size: 1.125rem;
    margin-bottom: 1rem;
  }
  
  .card-features li {
    font-size: 0.8125rem;
  }
}
</style>
