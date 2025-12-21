<template>
  <!-- 문서 전제: 정적 UI + 라우팅만 허용되는 단계 -->
  <div class="period-opt-bg">
    <div class="container py-4">
      <!-- 상단 타이틀 영역 -->
      <header class="mb-4 text-center">
        <h1 class="h5 fw-semibold mb-1">이번 워케이션 스타일은?</h1>
        <p class="text-muted small mb-0">당신의 워케이션 목표를 선택해주세요</p>
      </header>

      <!-- 카드 선택 영역 (가로 배치) -->
      <section class="mb-4">
        <div class="card-row">
          <div class="card-item">
            <button type="button" class="style-card w-100" @click="goPeriod('RELAX')">
              <div class="icon">🏝️</div>
              <div class="circle-badge"><span>휴양형</span></div>
              <ul class="desc">
                <li>휴식/리프레시 우선</li>
                <li>여유로운 일정 구성</li>
              </ul>
            </button>
          </div>
          <div class="card-item">
            <button type="button" class="style-card w-100" @click="goPeriod('WORK')">
              <div class="icon">💻</div>
              <div class="circle-badge"><span>업무형</span></div>
              <ul class="desc">
                <li>집중 업무 환경 중시</li>
                <li>시간 관리 최우선</li>
              </ul>
            </button>
          </div>
          <div class="card-item">
            <button type="button" class="style-card w-100" @click="goPeriod('BALANCE')">
              <div class="icon">⚖️</div>
              <div class="circle-badge"><span>밸런스형</span></div>
              <ul class="desc">
                <li>일·휴식 균형 추구</li>
                <li>유연한 스케줄</li>
              </ul>
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
.period-opt-bg { background: #f5f6f8; }

.style-card {
  appearance: none;
  border: 1px solid #e5e7eb;
  background: #fff;
  border-radius: 12px;
  padding: 20px 16px;
  text-align: center;
  cursor: pointer;
  transition: border-color .15s ease, transform .15s ease, box-shadow .15s ease;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}
.style-card:focus-visible { outline: 2px solid #80bdff; outline-offset: 2px; }
.style-card:hover { border-color: #d1d5db; transform: translateY(-2px); box-shadow: 0 6px 18px rgba(0,0,0,.06); }
.style-card .icon { font-size: 28px; margin-bottom: 8px; }
.circle-badge {
  width: 96px;
  height: 96px;
  border: 2px solid #e5e7eb;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  margin-bottom: 8px;
}
.style-card .desc { list-style: none; padding-left: 0; margin: 0; color: #6b7280; font-size: 0.9rem; text-align: center; }
.style-card .desc li { margin-bottom: 4px; }

/* 가로 카드 행: 모바일에서는 가로 스크롤, 데스크톱에서는 3등분 */
.card-row {
  display: flex;
  gap: 12px;
  overflow-x: auto;
  padding-bottom: 4px;
  scroll-snap-type: x proximity;
}
.card-item {
  flex: 0 0 80%;
  max-width: 360px;
  scroll-snap-align: start;
}

@media (min-width: 768px) {
  .card-row { overflow: visible; }
  .card-item { flex: 1 0 0; max-width: none; }
}
</style>
