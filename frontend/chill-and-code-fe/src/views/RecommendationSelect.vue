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

.style-cards-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem 0;
}

.style-card {
  position: relative;
  appearance: none;
  border: none;
  background: white;
  border-radius: 20px;
  padding: 0;
  width: 100%;
  height: 100%;
  min-height: 320px;
  cursor: pointer;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.style-card:hover {
  transform: translateY(-12px) scale(1.02);
  box-shadow: 0 20px 40px rgba(102, 126, 234, 0.2);
}

.style-card:active {
  transform: translateY(-8px) scale(0.98);
}

.card-gradient {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 140px;
  opacity: 0.8;
  transition: opacity 0.3s ease;
}

.style-card:hover .card-gradient {
  opacity: 1;
}

.card-gradient.relax {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.card-gradient.work {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.card-gradient.balance {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
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
  font-size: 4rem;
  margin-bottom: 1rem;
  animation: bounce 2s infinite;
}

@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

.style-card:hover .icon {
  animation: none;
  transform: scale(1.1);
  transition: transform 0.3s ease;
}

.card-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #1a202c;
  margin-bottom: 1.5rem;
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
  gap: 0.75rem;
  color: #64748b;
  font-size: 0.95rem;
  margin-bottom: 0.75rem;
  padding: 0.5rem;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.style-card:hover .card-features li {
  background: #f8f9fa;
  color: #1a202c;
}

.card-features li span {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 20px;
  height: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 50%;
  font-size: 0.7rem;
  font-weight: bold;
  flex-shrink: 0;
}

@media (max-width: 992px) {
  .page-title {
    font-size: 2rem;
  }
  
  .style-card {
    min-height: 280px;
  }
}

@media (max-width: 576px) {
  .page-title {
    font-size: 1.75rem;
  }
  
  .icon {
    font-size: 3rem;
  }
}
</style>
