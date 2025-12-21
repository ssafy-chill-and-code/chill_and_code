<template>
  <div class="container py-4">
    <header class="mb-3">
      <h1 class="h4 mb-1">장소 추천 — 옵션 선택</h1>
      <p class="text-muted mb-0">추천 장소를 고도화하기 위한 선호/조건을 선택해 주세요. 각 항목에 이유 설명을 제공합니다.</p>
    </header>

    <CLoading v-if="loading.options" full>옵션을 불러오는 중…</CLoading>
    <CError v-else-if="error" :message="error">
      <template #actions>
        <CButton variant="outline-primary" size="sm" @click="retry">다시 시도</CButton>
      </template>
    </CError>

    <template v-else>
      <section v-if="options.length > 0" class="row g-3">
        <div v-for="(opt, idx) in options" :key="idx" class="col-12 col-md-6 col-lg-4">
          <CCard>
            <template #header>
              <div class="d-flex align-items-center justify-content-between">
                <div class="fw-semibold">{{ opt.title }}</div>
                <CTag variant="info">필수</CTag>
              </div>
            </template>
            <div class="small text-muted mb-2">{{ opt.description }}</div>
            <ReadyBadge />
          </CCard>
        </div>
      </section>

      <section v-else>
        <CCard>
          <div class="d-flex align-items-center justify-content-between">
            <div>
              <div class="fw-semibold mb-1">장소 옵션 정보 준비 중</div>
              <div class="text-muted">백엔드 API 명세가 확정되면 자동으로 표시됩니다.</div>
            </div>
            <ReadyBadge />
          </div>
          <CEmpty class="mb-0">
            옵션 목록이 아직 제공되지 않았습니다.
            <template #cta>
              <CButton variant="outline-primary" size="sm" @click="retry">새로고침</CButton>
            </template>
          </CEmpty>
        </CCard>
      </section>
    </template>

    <div class="cta-bar">
      <div class="container d-flex align-items-center gap-2">
        <div class="flex-grow-1 small text-muted">
          선택 요약은 API 스키마 확정 시 표시됩니다. <ReadyBadge />
        </div>
        <CButton :disabled="!isReadyForSubmit" :loading="false">다음</CButton>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { usePlaceRecommendationStore } from '@/stores/placeRecommendation'

import CButton from '@/components/common/CButton.vue'
import CCard from '@/components/common/CCard.vue'
import CTag from '@/components/common/CTag.vue'
import CLoading from '@/components/common/CLoading.vue'
import CEmpty from '@/components/common/CEmpty.vue'
import CError from '@/components/common/CError.vue'
import ReadyBadge from '@/components/common/ReadyBadge.vue'

const store = usePlaceRecommendationStore()
const { options, loading, error, isReadyForSubmit } = storeToRefs(store)

function retry() { store.fetchOptions() }

onMounted(() => {
  store.fetchOptions()
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

