<template>
  <div class="style-select-wrapper">
    <div class="container py-5">
      <!-- 헤더 -->
      <header class="text-center mb-5">
        <RouterLink class="back-link" to="/">
          <span>←</span> 홈으로 돌아가기
        </RouterLink>
        <div class="header-content">
          <div class="step-indicator">STEP 1 of 3</div>
          <h1 class="page-title">이번 워케이션 스타일은?</h1>
          <p class="page-subtitle">당신의 워케이션 목표를 선택해주세요</p>
        </div>
      </header>

      <!-- 스타일 카드 -->
      <section class="style-cards-wrapper">
        <div class="row g-4 justify-content-center">
          <div class="col-12 col-md-6 col-lg-4">
            <button type="button" class="style-card" @click="goPeriod('RELAX')">
              <div class="card-gradient relax"></div>
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
          </div>
          <div class="col-12 col-md-6 col-lg-4">
            <button type="button" class="style-card" @click="goPeriod('WORK')">
              <div class="card-gradient work"></div>
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
          </div>
          <div class="col-12 col-md-6 col-lg-4">
            <button type="button" class="style-card" @click="goPeriod('BALANCE')">
              <div class="card-gradient balance"></div>
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
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useRecommendationStore } from '@/stores/recommendation'

const router = useRouter()
const recommendationStore = useRecommendationStore()

function goPeriod(styleType) {
  // 스타일 선택 후 기간 선택 화면으로 이동
  recommendationStore.updateSelection({ style: styleType })
  router.push('/recommend/period')
}
</script>

<style scoped>
.style-select-wrapper {
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
  font-size: 0.9rem;
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

.style-cards-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding: 3rem 0;
}

.style-card {
  position: relative;
  appearance: none;
  border: 2px solid #e2e8f0;
  background: white;
  border-radius: 16px;
  padding: 0;
  width: 100%;
  height: 100%;
  min-height: 340px;
  cursor: pointer;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.style-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.08);
  border-color: #cbd5e1;
}

.style-card:active {
  transform: translateY(-4px) scale(0.99);
}

.card-gradient {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 0;
  opacity: 0;
  transition: all 0.3s ease;
}

.style-card:hover .card-gradient {
  height: 4px;
  opacity: 1;
}

.card-gradient.relax {
  background: linear-gradient(135deg, #0ea5e9 0%, #06b6d4 100%);
}

.card-gradient.work {
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
}

.card-gradient.balance {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
}

.card-content {
  position: relative;
  padding: 2.5rem 2rem;
  text-align: center;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.icon {
  width: 72px;
  height: 72px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2.5rem;
  margin-bottom: 1.5rem;
  background: #f8fafc;
  border-radius: 16px;
  border: 1px solid #e2e8f0;
  transition: all 0.3s ease;
}

.style-card:hover .icon {
  background: #f1f5f9;
  transform: scale(1.05);
  border-color: #cbd5e1;
}

.card-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #0f172a;
  margin-bottom: 1.5rem;
  letter-spacing: -0.01em;
}

.card-features {
  list-style: none;
  padding: 0;
  margin: 0;
  text-align: left;
  width: 100%;
  max-width: 260px;
}

.card-features li {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  color: #64748b;
  font-size: 0.9375rem;
  margin-bottom: 0.875rem;
  padding: 0.625rem 0.75rem;
  border-radius: 8px;
  transition: all 0.3s ease;
  border-left: 2px solid transparent;
}

.style-card:hover .card-features li {
  background: #f8fafc;
  color: #334155;
  border-left-color: #cbd5e1;
}

.card-features li span {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 18px;
  height: 18px;
  background: #1e293b;
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
  
  .style-card {
    min-height: 300px;
  }
  
  .icon {
    width: 64px;
    height: 64px;
    font-size: 2.25rem;
  }
}

@media (max-width: 576px) {
  .style-select-wrapper {
    padding-top: 2rem;
    padding-bottom: 2rem;
  }
  
  .icon {
    width: 56px;
    height: 56px;
    font-size: 2rem;
  }
  
  .card-content {
    padding: 2rem 1.5rem;
  }
}
</style>
