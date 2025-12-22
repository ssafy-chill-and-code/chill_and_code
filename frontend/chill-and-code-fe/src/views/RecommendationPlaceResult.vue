<template>
  <div class="container-fluid px-4 py-4" style="max-width: 1400px;">
    <!-- 상단 타이틀/서브 -->
    <header class="mb-4">
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
            <div class="fw-semibold">{{ displayStyle }}</div>
          </div>
          <div>
            <div class="small text-muted">예산</div>
            <div class="fw-semibold">{{ displayBudget }}</div>
          </div>
          <div>
            <div class="small text-muted">추천 수</div>
            <div class="fw-semibold">{{ places.length }}곳</div>
          </div>
        </div>
      </section>

      <!-- 장소 카드 -->
      <section class="mb-4">
        <div class="row row-cols-1 row-cols-sm-2 row-cols-lg-3 g-3">
          <div v-for="place in places" :key="place.placeId" class="col">
            <CCard class="place-card h-100">
              <!-- 카드 상단: 대표 이미지 + 배지 -->
              <div class="thumb position-relative rounded overflow-hidden mb-3">
                <img v-if="place.imageUrl && place.imageUrl !== 'NO_IMAGE'" :src="place.imageUrl" :alt="place.name" class="thumb-img" />
                <div v-else class="thumb-inner d-flex align-items-center justify-content-center">🏖️</div>
                <div class="match-badge">⭐ {{ Math.round(place.trendScore) }}%</div>
              </div>

              <!-- 카드 본문 -->
              <div class="card-body-content">
                <h6 class="fw-bold mb-1">{{ place.name }}</h6>
                <div class="text-muted small mb-2">{{ place.region }}</div>

                <!-- 태그 영역 -->
                <div class="d-flex flex-wrap gap-1 mb-2">
                  <span v-for="tag in place.tags" :key="tag" class="hash">{{ tag }}</span>
                </div>

                <!-- 가격 정보 -->
                <div class="price-text mb-2">월 {{ formatPrice(place.score) }}</div>

                <!-- 추천 이유 박스 -->
                <div class="reason-box small mb-3">
                  <div class="fw-semibold mb-1">추천이유</div>
                  <div class="reason-text">{{ place.reasonText || '당신의 예산과 일정에 완벽하게 맞습니다' }}</div>
                </div>

                <!-- CTA -->
                <CButton block @click="goSchedule(place)">이 장소로 일정 만들기</CButton>
              </div>
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

const places = computed(() => {
  const result = placeStore.result || []
  // 최대 6개만 표시
  return result.slice(0, 6)
})
const selection = computed(() => placeStore.selection)
const selectedPeriod = computed(() => recommendationStore.selection.selectedPeriod)

const periodText = computed(() => {
  if (!selectedPeriod.value) return '기간 미선택'
  return `${formatDate(selectedPeriod.value.startDate)} ~ ${formatDate(selectedPeriod.value.endDate)}`
})

// 백엔드 값을 한글로 변환
const displayStyle = computed(() => {
  const styleMap = {
    'NATURE': '힐링',
    'CAFE': '작업몰입',
    'ACTIVITY': '액티비티'
  }
  return styleMap[selection.value.style] || selection.value.style
})

const displayBudget = computed(() => {
  const budgetMap = {
    'LOW': '가성비',
    'MID': '적당한',
    'HIGH': '프리미엄'
  }
  return budgetMap[selection.value.budget] || selection.value.budget
})

function formatDate(dateString) {
  if (!dateString) return ''
  const date = new Date(dateString)
  const month = date.getMonth() + 1
  const day = date.getDate()
  return `${month}월 ${day}일`
}

function formatPrice(score) {
  // 점수를 가격으로 변환 (예시)
  if (score >= 80) return '180만원'
  if (score >= 60) return '150만원'
  return '120만원'
}

function goSchedule(place) {
  // 선택한 장소로 일정 만들기
  router.push('/schedule/create')
}
</script>

<style scoped>
.summary-box { background: #fff; }

.place-card { 
  background: #fff; 
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.place-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.thumb {
  aspect-ratio: 16/9;
  background: #f3f4f6;
  position: relative;
  overflow: hidden;
}

.thumb-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.thumb-inner { 
  width: 100%; 
  height: 100%; 
  font-size: 48px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.match-badge {
  position: absolute; 
  left: 8px; 
  top: 8px;
  background: rgba(13, 110, 253, 0.9);
  color: #fff; 
  font-size: 0.75rem;
  font-weight: 600;
  padding: 4px 10px; 
  border-radius: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.card-body-content {
  padding: 0 4px;
}

.hash {
  display: inline-block; 
  background: #e7f3ff; 
  color: #0d6efd; 
  border-radius: 12px; 
  padding: 3px 10px; 
  font-size: 0.75rem;
  font-weight: 500;
}

.price-text {
  font-size: 1.1rem;
  font-weight: 600;
  color: #0d6efd;
}

.reason-box { 
  background: #f8f9fb; 
  border: 1px solid #e9ecef; 
  border-radius: 8px; 
  padding: 10px 12px;
}

.reason-text {
  color: #6c757d;
  font-size: 0.875rem;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 반응형 - 모바일에서는 1열, 태블릿 2열, 데스크톱 3열 */
@media (max-width: 767px) {
  .col-12 {
    margin-bottom: 1rem;
  }
}
</style>
