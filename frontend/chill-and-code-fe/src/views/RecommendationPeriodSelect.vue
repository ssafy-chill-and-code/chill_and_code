<template>
  <!-- 문서 전제: 정적 UI + 라우팅만 허용되는 단계 -->
  <div class="period-opt-bg">
    <div class="container py-4">
      <!-- 상단 영역: 돌아가기 + 스타일 다시 선택 -->
      <header class="mb-4">
        <div class="d-flex align-items-center justify-content-between mb-2">
          <RouterLink class="text-decoration-none small" to="/recommend">← 스타일 다시 선택</RouterLink>
          <div class="h5 mb-0">추천받기</div>
          <span class="opacity-0">placeholder</span>
        </div>
      </header>

      <!-- 질문 문구 -->
      <section class="mb-3">
        <div class="fw-semibold mb-1">이번 워케이션 희망 기간은?</div>
        <div class="text-muted small">최소 기간과 최대 기간을 설정해주세요</div>
      </section>

      <!-- 2컬럼 입력 영역: 최소/최대 기간 (정적 UI) -->
      <section class="row g-3 mb-3">
        <div class="col-12 col-md-6">
          <div class="mb-1 small">최소 기간</div>
          <div class="d-flex align-items-center gap-2">
            <input type="number" class="form-control" placeholder="2" aria-label="최소 기간" />
            <span class="text-muted">일</span>
          </div>
          <div class="small text-muted mt-1">1~30일 범위</div>
        </div>
        <div class="col-12 col-md-6">
          <div class="mb-1 small">최대 기간</div>
          <div class="d-flex align-items-center gap-2">
            <input type="number" class="form-control" placeholder="5" aria-label="최대 기간" />
            <span class="text-muted">일</span>
          </div>
          <div class="small text-muted mt-1">1~30일 범위</div>
        </div>
      </section>

      <!-- 안내 배너: 정적 텍스트만 -->
      <section class="mb-4">
        <div class="alert alert-light border small mb-0">2 ~ 5일 범위에서 추천받습니다</div>
      </section>

      <!-- CTA: full-width, 라우팅만 수행 -->
      <section class="mb-5">
        <CButton block @click="goResult">추천받기</CButton>
      </section>
    </div>
  </div>
</template>

<script setup>
// 정적 UI + 라우팅만
import CButton from '@/components/common/CButton.vue'
import { useRouter } from 'vue-router'

const router = useRouter()
function goResult() {
  // /recommend/period-result 로 라우팅만 수행
  router.push('/recommend/period-result')
}
</script>

<style scoped>
.period-opt-bg { background: #f5f6f8; }
</style>
