<template>
  <!-- 문서 전제: 정적 UI + 라우팅만 허용 -->
  <div class="container py-4">
    <!-- 상단 좌측: 이전으로 -->
    <div class="mb-2">
      <button type="button" class="btn btn-link btn-sm px-0" @click="goBack"> ← 이전으로</button>
    </div>
    <!-- 헤더 -->
    <header class="mb-3">
      <h1 class="h5 mb-1">당신의 선호도를 알려주세요</h1>
      <p class="text-muted small mb-0">{{ periodText }}의 워케이션을 위한 옵션을 선택하세요.</p>
    </header>

    <!-- 섹션 1: 워케이션 스타일 선택 -->
    <section class="mb-4">
      <div class="fw-semibold mb-2">⚡ 워케이션 스타일 <span class="text-danger">*</span></div>
      <div class="row g-3">
        <div class="col-12 col-md-4">
          <button type="button" class="option-card w-100" :class="{ 'selected': selectedStyle === '힐링' }" @click="selectStyle('힐링')">
            <div class="icon">🌿</div>
            <div class="title">힐링</div>
            <div class="desc small text-muted">자연 속에서 마음을 편안하게 하며 일하는 스타일</div>
          </button>
        </div>
        <div class="col-12 col-md-4">
          <button type="button" class="option-card w-100" :class="{ 'selected': selectedStyle === '작업몰입' }" @click="selectStyle('작업몰입')">
            <div class="icon">💻</div>
            <div class="title">작업몰입</div>
            <div class="desc small text-muted">집중력 있는 업무 환경에서 생산성을 높이는 스타일</div>
          </button>
        </div>
        <div class="col-12 col-md-4">
          <button type="button" class="option-card w-100" :class="{ 'selected': selectedStyle === '액티비티' }" @click="selectStyle('액티비티')">
            <div class="icon">🏃‍♂️</div>
            <div class="title">액티비티</div>
            <div class="desc small text-muted">다양한 활동과 경험을 즐기며 일하는 스타일</div>
          </button>
        </div>
      </div>
    </section>

    <!-- 섹션 2: 월간 예산 선택 -->
    <section class="mb-4">
      <div class="fw-semibold mb-2">💲 월간 예산</div>
      <div class="p-3 border rounded bg-white">
        <div class="h6 mb-2">{{ budget }}만원</div>
        <input type="range" v-model.number="budget" class="form-range" min="50" max="500" step="10" aria-label="월간 예산 슬라이더" />
        <div class="d-flex align-items-center justify-content-between small text-muted mt-1">
          <span>50만원</span>
          <span>500만원</span>
        </div>
      </div>
    </section>

    <!-- 섹션 3: 이동수단 선택 (선택) -->
    <section class="mb-4">
      <div class="fw-semibold mb-2">🚗 이동수단 (선택)</div>
      <select v-model="transport" class="form-select" aria-label="이동수단 선택">
        <option value="">선호하는 이동수단을 선택하세요</option>
        <option value="자동차">자동차</option>
        <option value="기차">기차</option>
        <option value="비행기">비행기</option>
        <option value="버스">버스</option>
      </select>
    </section>

    <!-- 섹션 4: 선호 지역 선택 (선택) -->
    <section class="mb-4">
      <div class="fw-semibold mb-2">📍 선호 지역 (선택)</div>
      <div class="d-flex flex-wrap gap-2">
        <button 
          v-for="region in regions" 
          :key="region" 
          type="button" 
          class="tag" 
          :class="{ 'active': selectedRegions.includes(region) }"
          @click="toggleRegion(region)"
        >
          {{ region }}
        </button>
      </div>
    </section>

    <!-- 에러 메시지 -->
    <section v-if="errorMessage" class="mb-3">
      <div class="alert alert-danger small mb-0">{{ errorMessage }}</div>
    </section>

    <!-- CTA 영역 -->
    <section class="mb-2">
      <CButton block @click="goResult" :disabled="placeStore.loading">
        {{ placeStore.loading ? '추천 중...' : '장소 추천받기' }}
      </CButton>
    </section>
  </div>
  
</template>

<script setup>
import { ref, computed } from 'vue'
import CButton from '@/components/common/CButton.vue'
import { useRouter } from 'vue-router'
import { usePlaceRecommendationStore } from '@/stores/placeRecommendation'
import { useRecommendationStore } from '@/stores/recommendation'

const router = useRouter()
const placeStore = usePlaceRecommendationStore()
const recommendationStore = useRecommendationStore()

const selectedStyle = ref('')
const budget = ref(150)
const transport = ref('')
const selectedRegions = ref([])
const errorMessage = ref('')

const regions = ['제주', '강릉', '부산', '경주', '속초', '전주', '여수', '춘천', '남해', '통영']

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

function selectStyle(style) {
  selectedStyle.value = style
}

function toggleRegion(region) {
  const index = selectedRegions.value.indexOf(region)
  if (index > -1) {
    selectedRegions.value.splice(index, 1)
  } else {
    selectedRegions.value.push(region)
  }
}

async function goResult() {
  // 유효성 검사
  if (!selectedStyle.value) {
    errorMessage.value = '워케이션 스타일을 선택해주세요.'
    return
  }
  
  errorMessage.value = ''
  
  // 스토어에 선택 저장
  placeStore.updateSelection({
    style: selectedStyle.value,
    budget: `${budget.value}만원`,
    region: selectedRegions.value.join(','),
    transport: transport.value
  })
  
  try {
    // API 호출
    await placeStore.fetchPlaceRecommendation()
    // 성공 시 결과 페이지로 이동
    router.push('/recommend/place/result')
  } catch (e) {
    errorMessage.value = placeStore.error || '장소 추천을 불러오는데 실패했습니다.'
  }
}

function goBack() {
  router.back()
}
</script>

<style scoped>
.option-card {
  appearance: none;
  border: 1px solid #e5e7eb;
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  text-align: left;
  transition: border-color .15s ease, box-shadow .15s ease;
  cursor: pointer;
}
.option-card:focus-visible { outline: 2px solid #80bdff; outline-offset: 2px; }
.option-card:hover { border-color: #d1d5db; }
.option-card.selected { border-color: #0d6efd; border-width: 2px; background: #f0f7ff; }
.option-card .icon { font-size: 24px; margin-bottom: 6px; }
.option-card .title { font-weight: 600; margin-bottom: 4px; }

.tag {
  appearance: none;
  border: 1px solid #e5e7eb;
  background: #fff;
  border-radius: 999px;
  padding: 6px 12px;
  font-size: .9rem;
  cursor: pointer;
  transition: all .15s ease;
}
.tag:focus-visible { outline: 2px solid #80bdff; outline-offset: 2px; }
.tag:hover { border-color: #d1d5db; }
.tag.active { background: #0d6efd; color: #fff; border-color: #0d6efd; }
</style>
