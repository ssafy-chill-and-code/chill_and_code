<template>
  <div class="period-bg">
    <div class="container py-4">
      <!-- 헤더 -->
      <header class="mb-4 text-center">
        <h1 class="h4 mb-0">추천 기간</h1>
      </header>

      <!-- 로딩 중 -->
      <div v-if="recommendationStore.loading" class="text-center py-5">
        <div class="spinner-border" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
        <div class="mt-3">기간을 추천하고 있습니다...</div>
      </div>

      <!-- 에러 메시지 -->
      <div v-else-if="recommendationStore.error" class="alert alert-danger">
        {{ recommendationStore.error }}
      </div>

      <!-- 결과가 없을 때 -->
      <div v-else-if="!result || !result.primary" class="alert alert-warning">
        추천 가능한 기간이 없습니다. 조건을 변경해주세요.
      </div>

      <!-- 추천 결과 -->
      <div v-else class="d-flex flex-column gap-3">
        <!-- 최우선 추천 -->
        <CCard>
          <template #header>
            <div class="d-flex align-items-start justify-content-between">
              <div>
                <div class="badge bg-primary mb-2">최우선 추천</div>
                <div class="fw-semibold">{{ formatDate(result.primary.startDate) }} ~ {{ formatDate(result.primary.endDate) }}</div>
                <div class="text-muted small">{{ result.primary.durationDays }}일</div>
              </div>
            </div>
          </template>
          <div class="text-muted mb-3">{{ result.reason || '이 기간이 가장 적합합니다.' }}</div>
          <CButton block @click="goPlace(result.primary)">이 기간 선택</CButton>
        </CCard>

        <!-- 대안 추천 -->
        <CCard v-for="(alt, index) in result.alternatives" :key="index">
          <template #header>
            <div class="d-flex align-items-start justify-content-between">
              <div>
                <div class="fw-semibold">{{ formatDate(alt.startDate) }} ~ {{ formatDate(alt.endDate) }}</div>
                <div class="text-muted small">{{ alt.durationDays }}일</div>
              </div>
            </div>
          </template>
          <div class="text-muted mb-3">대안 기간입니다.</div>
          <CButton block @click="goPlace(alt)">이 기간 선택</CButton>
        </CCard>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import CButton from '@/components/common/CButton.vue'
import CCard from '@/components/common/CCard.vue'
import { useRouter } from 'vue-router'
import { useRecommendationStore } from '@/stores/recommendation'

const router = useRouter()
const recommendationStore = useRecommendationStore()

const result = computed(() => recommendationStore.result)

function formatDate(dateString) {
  if (!dateString) return ''
  const date = new Date(dateString)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}.${month}.${day}`
}

function goPlace(period) {
  // 선택한 기간을 스토어에 저장하고 장소 선택 화면으로 이동
  recommendationStore.updateSelection({
    selectedPeriod: period
  })
  router.push('/recommend/place')
}
</script>

<style scoped>
.period-bg { background: #f5f6f8; }
</style>

