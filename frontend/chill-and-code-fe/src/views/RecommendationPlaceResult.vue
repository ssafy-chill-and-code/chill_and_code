<template>
  <div class="container py-4">
    <!-- 헤더 요약: 추천 장소 정보 우선 -->
    <section class="mb-3">
      <CCard>
        <template #header>
          <div class="d-flex align-items-center justify-content-between">
            <div class="fw-semibold">장소 추천 결과 요약</div>
            <CTag variant="success">추천</CTag>
          </div>
        </template>
        <div class="row g-3 align-items-center">
          <div class="col-12 col-md-7">
            <div class="d-flex align-items-center gap-3">
              <div class="ratio ratio-1x1" style="width:64px;">
                <div class="bg-light d-flex align-items-center justify-content-center rounded">📍</div>
              </div>
              <div>
                <div class="h5 mb-1">—</div>
                <div class="text-muted small">추천 장소명/지역은 API 확정 시 표시 <ReadyBadge /></div>
              </div>
            </div>
          </div>
          <div class="col-12 col-md-5 d-flex flex-wrap align-items-center gap-3">
            <div>
              <div class="small text-muted">적합도</div>
              <CBadge color="primary">—</CBadge>
            </div>
            <div>
              <div class="small text-muted">핵심 태그</div>
              <div class="d-flex flex-wrap gap-1">
                <CTag variant="info">와이파이</CTag>
                <CTag variant="info">조용함</CTag>
                <ReadyBadge />
              </div>
            </div>
          </div>
        </div>
      </CCard>
    </section>

    <!-- 로딩/에러 분기 -->
    <CLoading v-if="loading.result" full>결과를 준비 중…</CLoading>
    <CError v-else-if="error" :message="error">
      <template #actions>
        <CButton variant="outline-primary" size="sm" @click="retry">다시 조회</CButton>
      </template>
    </CError>

    <!-- 핵심 이유 섹션: 그룹화 -->
    <section class="mb-3">
      <h2 class="h6 mb-2">선정 이유</h2>
      <div class="row g-3">
        <div class="col-12 col-md-4" v-for="i in 3" :key="i">
          <CCard>
            <div class="d-flex align-items-start gap-2">
              <div>💡</div>
              <div>
                <div class="fw-semibold">이유 {{ i }}</div>
                <div class="small text-muted">구체 근거는 API 확정 시 표시 <ReadyBadge /></div>
              </div>
            </div>
          </CCard>
        </div>
      </div>
    </section>

    <!-- 대안 장소 섹션 -->
    <section class="mb-4">
      <div class="d-flex align-items-center justify-content-between mb-2">
        <h2 class="h6 mb-0">대안 장소</h2>
        <div class="d-flex align-items-center gap-2">
          <CTag variant="secondary">정렬/필터 <ReadyBadge /></CTag>
        </div>
      </div>
      <div v-if="hasAlternatives" class="row g-3">
        <div v-for="n in 3" :key="n" class="col-12 col-md-6 col-lg-4">
          <CCard clickable>
            <div class="fw-semibold">대안 {{ n }}</div>
            <div class="small text-muted mb-2">요약 정보 <ReadyBadge /></div>
            <div class="d-flex gap-1">
              <CTag variant="info">접근성</CTag>
              <CTag variant="info">가성비</CTag>
            </div>
          </CCard>
        </div>
      </div>
      <div v-else>
        <CEmpty>대안 정보가 아직 제공되지 않았습니다. <ReadyBadge /></CEmpty>
      </div>
    </section>

    <!-- 하단 CTA 바 -->
    <div class="cta-bar">
      <div class="container d-flex align-items-center gap-2">
        <CButton variant="outline-primary" @click="goBack">다시 선택하기</CButton>
        <div class="ms-auto d-flex align-items-center gap-2">
          <CButton variant="outline-primary" disabled>
            저장 <ReadyBadge />
          </CButton>
          <CButton variant="primary" disabled>
            공유 <ReadyBadge />
          </CButton>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { storeToRefs } from 'pinia'
import { usePlaceRecommendationStore } from '@/stores/placeRecommendation'

import CButton from '@/components/common/CButton.vue'
import CCard from '@/components/common/CCard.vue'
import CTag from '@/components/common/CTag.vue'
import CBadge from '@/components/common/CBadge.vue'
import CLoading from '@/components/common/CLoading.vue'
import CError from '@/components/common/CError.vue'
import CEmpty from '@/components/common/CEmpty.vue'
import ReadyBadge from '@/components/common/ReadyBadge.vue'

const router = useRouter()
const store = usePlaceRecommendationStore()
const { result, loading, error } = storeToRefs(store)

const hasAlternatives = computed(() => Array.isArray(result.value?.alternatives) && result.value.alternatives.length > 0)

function retry() { store.fetchResult() }
function goBack() { router.push('/recommend/place') }

onMounted(() => {
  store.fetchResult()
})
</script>

<style scoped>
.cta-bar {
  position: sticky;
  bottom: 0;
  background: rgba(255,255,255,.92);
  backdrop-filter: blur(6px);
  border-top: 1px solid #eee;
  padding: .75rem 0;
  margin-top: 1rem;
}
</style>

