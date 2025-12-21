<template>
  <!-- 문서 전제: 정적 UI + 라우팅만 허용되는 단계 -->
  <div class="period-opt-bg">
    <div class="container py-4">
      <!-- 상단바: 좌측 돌아가기, 중앙 제목 -->
      <header class="mb-4">
        <div class="d-flex align-items-center justify-content-between">
          <RouterLink class="text-decoration-none small" to="/">← 돌아가기</RouterLink>
          <div class="h5 mb-0">추천받기</div>
          <span class="opacity-0">placeholder</span>
        </div>
      </header>

      <!-- 메인 질문: 스타일 선택 -->
      <section class="mb-2">
        <div class="fw-semibold mb-1">이번 워케이션 스타일은?</div>
        <div class="text-muted small">당신의 워케이션 목표를 선택해주세요</div>
      </section>

      <!-- 카드 3개: 클릭 시 /recommend/period 로 이동 -->
      <section class="row g-3 mb-4">
        <div class="col-12 col-md-4">
          <button type="button" class="style-card w-100" @click="goPeriod">
            <div class="icon">🏝️</div>
            <div class="pill">휴양형</div>
            <ul class="desc">
              <li>휴식/리프레시 우선</li>
              <li>여유로운 일정 구성</li>
            </ul>
          </button>
        </div>
        <div class="col-12 col-md-4">
          <button type="button" class="style-card w-100" @click="goPeriod">
            <div class="icon">💻</div>
            <div class="pill">업무형</div>
            <ul class="desc">
              <li>집중 업무 환경 중시</li>
              <li>시간 관리 최우선</li>
            </ul>
          </button>
        </div>
        <div class="col-12 col-md-4">
          <button type="button" class="style-card w-100" @click="goPeriod">
            <div class="icon">⚖️</div>
            <div class="pill">밸런스형</div>
            <ul class="desc">
              <li>일·휴식 균형 추구</li>
              <li>유연한 스케줄</li>
            </ul>
          </button>
        </div>
      </section>
    </div>
  </div>
  
</template>

<script setup>
// 문서 전제: API/상태/바인딩/검증/로직 없음 — 정적 UI만
import { useRouter } from 'vue-router'

const router = useRouter()
function goPeriod() {
  // 문서 규칙: /recommend/period로 라우팅만 수행 (데이터 전달/상태 변경 없음)
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
  text-align: left;
  transition: border-color .15s ease;
}
.style-card:focus-visible { outline: 2px solid #80bdff; outline-offset: 2px; }
.style-card:hover { border-color: #d1d5db; }
.style-card .icon { font-size: 28px; margin-bottom: 8px; }
.style-card .pill {
  display: inline-block;
  border: 2px solid #e5e7eb;
  border-radius: 999px;
  padding: 4px 10px;
  font-weight: 600;
  margin-bottom: 8px;
}
.style-card .desc { padding-left: 18px; margin: 0; color: #6b7280; font-size: 0.9rem; }
.style-card .desc li { margin-bottom: 4px; }
</style>
