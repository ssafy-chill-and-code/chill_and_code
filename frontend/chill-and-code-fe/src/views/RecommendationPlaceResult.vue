<template>
  <div class="container py-4">
    <!-- 상단 타이틀/서브 -->
    <header class="mb-3">
      <h1 class="h4 mb-1">당신을 위한 워케이션 장소</h1>
      <p class="text-muted small mb-0">{{ periodText }}에 추천하는 최고의 워케이션 장소들입니다.</p>
    </header>

    <!-- 로딩 중 -->
    <div v-if="placeStore.loading" class="text-center py-5">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
      <div class="mt-3">장소를 추천하고 있습니다...</div>
    </div>

    <!-- 에러 메시지 -->
    <div v-else-if="placeStore.error" class="alert alert-danger">
      {{ placeStore.error }}
    </div>

    <!-- 결과가 없을 때 -->
    <div v-else-if="!places || places.length === 0" class="alert alert-warning">
      추천 가능한 장소가 없습니다. 조건을 변경해주세요.
    </div>

    <template v-else>
      <!-- 선택 요약 바 -->
      <section class="mb-4">
        <div class="summary-box p-3 border rounded bg-white d-flex flex-wrap gap-4">
          <div>
            <div class="small text-muted">기간</div>
            <div class="fw-semibold">{{ periodText }}</div>
          </div>
          <div>
            <div class="small text-muted">스타일</div>
            <div class="fw-semibold">{{ selection.style }}</div>
          </div>
          <div>
            <div class="small text-muted">예산</div>
            <div class="fw-semibold">{{ selection.budget }}</div>
          </div>
          <div>
            <div class="small text-muted">추천 수</div>
            <div class="fw-semibold">{{ places.length }}곳</div>
          </div>
        </div>
      </section>

      <!-- 장소 카드 -->
      <section class="mb-4">
        <div class="row g-3">
          <div v-for="place in places" :key="place.placeId" class="col-12 col-md-6 col-lg-4">
            <CCard class="place-card">
              <!-- 카드 상단: 대표 이미지 + 배지/아이콘 -->
              <div class="thumb position-relative rounded overflow-hidden mb-2">
                <img v-if="place.imageUrl" :src="place.imageUrl" :alt="place.name" class="thumb-img" />
                <div v-else class="thumb-inner d-flex align-items-center justify-content-center">🏖️</div>
                <div class="match-badge">⭐ {{ Math.round(place.trendScore) }}%</div>
              </div>

              <!-- 카드 본문 -->
              <div class="fw-semibold">{{ place.name }}</div>
              <div class="text-muted small mb-2">{{ place.region }}</div>

              <!-- 태그 영역 -->
              <div class="d-flex flex-wrap gap-1 mb-2">
                <span v-for="tag in place.tags" :key="tag" class="hash">#{{ tag }}</span>
              </div>

              <!-- 점수 정보 -->
              <div class="fw-semibold mb-2">매칭 점수: {{ Math.round(place.score) }}</div>

              <!-- 추천 이유 박스 -->
              <div class="reason-box small mb-3">
                <div class="fw-semibold">추천이유</div>
                <div>{{ place.reasonText || '이 장소가 당신에게 적합합니다' }}</div>
              </div>

              <!-- CTA -->
              <CButton block @click="goSchedule(place)">이 장소로 일정 만들기</CButton>
            </CCard>
          </div>
        </div>
      </section>
    </template>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import CButton from '@/components/common/CButton.vue'
import CCard from '@/components/common/CCard.vue'
import { useRouter } from 'vue-router'
import { usePlaceRecommendationStore } from '@/stores/placeRecommendation'
import { useRecommendationStore } from '@/stores/recommendation'

const router = useRouter()
const placeStore = usePlaceRecommendationStore()
const recommendationStore = useRecommendationStore()

const places = computed(() => placeStore.result || [])
const selection = computed(() => placeStore.selection)
const selectedPeriod = computed(() => recommendationStore.selection.selectedPeriod)

const periodText = computed(() => {
  if (!selectedPeriod.value) return '기간 미선택'
  return `${formatDate(selectedPeriod.value.startDate)} ~ ${formatDate(selectedPeriod.value.endDate)}`
})

function formatDate(dateString) {
  if (!dateString) return ''
  const date = new Date(dateString)
  const month = date.getMonth() + 1
  const day = date.getDate()
  return `${month}월 ${day}일`
}

function goSchedule(place) {
  // 선택한 장소로 일정 만들기
  router.push('/schedule/create')
}
</script>

<style scoped>
.summary-box { background: #fff; }

.place-card { background: #fff; }
.thumb { 
  aspect-ratio: 16/9; 
  background: #f3f4f6; 
  position: relative;
}
.thumb-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.thumb-inner { width: 100%; height: 100%; font-size: 32px; }
.match-badge {
  position: absolute; left: 8px; top: 8px;
  background: rgba(0,0,0,.7); color: #fff; font-size: .85rem;
  padding: 4px 8px; border-radius: 8px;
}

.hash {
  display: inline-block; background: #f3f4f6; color: #374151; border-radius: 999px; padding: 2px 8px; font-size: .8rem;
}
.reason-box { background: #f8f9fb; border: 1px solid #eef0f4; border-radius: 8px; padding: 8px 10px; }
</style>
