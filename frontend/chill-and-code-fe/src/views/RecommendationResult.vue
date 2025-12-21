<template>
  <!-- 문서 4-1: 연한 배경의 full-width 래퍼 + 중앙 정렬 컨테이너 -->
  <div class="period-bg">
    <div class="container py-4">
      <!-- 문서 4-2: 페이지 타이틀 영역 -->
      <header class="mb-4 text-center">
        <h1 class="h4 mb-0">추천 기간</h1>
      </header>

      <!-- 문서 4-3, 5: 추천 기간 카드 리스트(세로 나열, 동일 구조, 정적 placeholder) -->
      <div class="d-flex flex-column gap-3">
        <CCard>
          <template #header>
            <div class="d-flex align-items-start justify-content-between">
              <div>
                <div class="fw-semibold">2025.01.06 ~ 2025.01.12</div>
                <div class="text-muted small">7일</div>
              </div>
              <div class="text-end">
                <div class="fw-semibold">82%</div>
                <div class="small text-muted">매칭률</div>
              </div>
            </div>
          </template>
          <div class="text-muted mb-3">업무 집중도가 높고, 주중 회의 밀도가 낮아 생산성이 기대됩니다.</div>
          <CButton block @click="goPlace">이 기간 선택</CButton>
        </CCard>

        <CCard>
          <template #header>
            <div class="d-flex align-items-start justify-content-between">
              <div>
                <div class="fw-semibold">2025.01.20 ~ 2025.01.26</div>
                <div class="text-muted small">7일</div>
              </div>
              <div class="text-end">
                <div class="fw-semibold">76%</div>
                <div class="small text-muted">매칭률</div>
              </div>
            </div>
          </template>
          <div class="text-muted mb-3">회의 일정이 분산되어 있고, 개인 일정과의 충돌이 적습니다.</div>
          <CButton block @click="goPlace">이 기간 선택</CButton>
        </CCard>

        <CCard>
          <template #header>
            <div class="d-flex align-items-start justify-content-between">
              <div>
                <div class="fw-semibold">2025.02.03 ~ 2025.02.09</div>
                <div class="text-muted small">7일</div>
              </div>
              <div class="text-end">
                <div class="fw-semibold">71%</div>
                <div class="small text-muted">매칭률</div>
              </div>
            </div>
          </template>
          <div class="text-muted mb-3">프로젝트 마감 전 여유 기간으로 집중 업무가 가능합니다.</div>
          <CButton block @click="goPlace">이 기간 선택</CButton>
        </CCard>
      </div>
    </div>
  </div>
</template>

<script setup>
// 문서 7,8: API 연동/상태/동작 구현 금지 — 정적 placeholder만 배치
import CButton from '@/components/common/CButton.vue'
import CCard from '@/components/common/CCard.vue'
import { useRouter } from 'vue-router'

const router = useRouter()
function goPlace() {
  router.push('/recommend/place')
}
</script>

<style scoped>
.period-bg { background: #f5f6f8; }
</style>
