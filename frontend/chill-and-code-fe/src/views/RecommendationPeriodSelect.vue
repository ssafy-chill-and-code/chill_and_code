<template>
  <!-- 문서 전제: 정적 UI + 라우팅만 허용되는 단계 -->
  <div class="period-opt-bg">
    <div class="container py-4">
      <!-- 상단 영역: 돌아가기 + 스타일 다시 선택 -->
      <header class="mb-4">
        <div class="d-flex align-items-center justify-content-between mb-2">
          <RouterLink class="text-decoration-none small" to="/recommend">← 스타일 다시 선택</RouterLink>
          <span class="opacity-0">placeholder</span>
        </div>
      </header>

      <!-- 질문 문구 -->
      <section class="mb-3 text-center">
        <div class="fw-semibold mb-1">이번 워케이션 희망 기간은?</div>
        <div class="text-muted small">최소 기간과 최대 기간을 설정해주세요</div>
      </section>

      <!-- 2컬럼 입력 영역: 최소/최대 기간 -->
      <section class="row g-3 mb-3">
        <div class="col-12 col-md-6">
          <div class="mb-1 small">최소 기간</div>
          <div class="d-flex align-items-center gap-2">
            <input type="number" v-model.number="minDays" class="form-control" placeholder="2" aria-label="최소 기간" min="1" max="30" />
            <span class="text-muted">일</span>
          </div>
          <div class="small text-muted mt-1">1~30일 범위</div>
        </div>
        <div class="col-12 col-md-6">
          <div class="mb-1 small">최대 기간</div>
          <div class="d-flex align-items-center gap-2">
            <input type="number" v-model.number="maxDays" class="form-control" placeholder="5" aria-label="최대 기간" min="1" max="30" />
            <span class="text-muted">일</span>
          </div>
          <div class="small text-muted mt-1">1~30일 범위</div>
        </div>
      </section>

      <!-- 안내 배너 -->
      <section class="mb-3">
        <div class="alert alert-light border small mb-0">{{ minDays }} ~ {{ maxDays }}일 범위에서 추천받습니다</div>
      </section>

      <!-- 에러 메시지 -->
      <section v-if="errorMessage" class="mb-3">
        <div class="alert alert-danger small mb-0">{{ errorMessage }}</div>
      </section>

      <!-- CTA -->
      <section class="mb-5">
        <CButton block @click="goResult" :disabled="recommendationStore.loading">
          {{ recommendationStore.loading ? '추천 중...' : '추천받기' }}
        </CButton>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import CButton from '@/components/common/CButton.vue'
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
.period-opt-bg { background: #f5f6f8; }
</style>
