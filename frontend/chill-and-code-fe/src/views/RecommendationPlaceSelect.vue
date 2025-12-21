<template>
  <!-- 문서 전제: 정적 UI + 라우팅만 허용 -->
  <div class="container py-4">
    <!-- 헤더 -->
    <header class="mb-3">
      <h1 class="h5 mb-1">당신의 선호도를 알려주세요</h1>
      <p class="text-muted small mb-0">12월 15일 ~ 12월 22일의 워케이션을 위한 옵션을 선택하세요.</p>
    </header>

    <!-- 섹션 1: 워케이션 스타일 선택 -->
    <section class="mb-4">
      <div class="fw-semibold mb-2">⚡ 워케이션 스타일 <span class="text-danger">*</span></div>
      <div class="row g-3">
        <div class="col-12 col-md-4">
          <button type="button" class="option-card w-100" aria-pressed="false">
            <div class="icon">🌿</div>
            <div class="title">힐링</div>
            <div class="desc small text-muted">자연 속에서 마음을 편안하게 하며 일하는 스타일</div>
          </button>
        </div>
        <div class="col-12 col-md-4">
          <button type="button" class="option-card w-100" aria-pressed="false">
            <div class="icon">💻</div>
            <div class="title">작업몰입</div>
            <div class="desc small text-muted">집중력 있는 업무 환경에서 생산성을 높이는 스타일</div>
          </button>
        </div>
        <div class="col-12 col-md-4">
          <button type="button" class="option-card w-100" aria-pressed="false">
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
        <div class="h6 mb-2">150만원</div>
        <input type="range" class="form-range" min="0" max="100" value="50" aria-label="월간 예산 슬라이더" />
        <div class="d-flex align-items-center justify-content-between small text-muted mt-1">
          <span>50만원</span>
          <span>500만원</span>
        </div>
      </div>
    </section>

    <!-- 섹션 3: 이동수단 선택 (선택) -->
    <section class="mb-4">
      <div class="fw-semibold mb-2">🚗 이동수단 (선택)</div>
      <select class="form-select" aria-label="이동수단 선택">
        <option selected disabled>선호하는 이동수단을 선택하세요</option>
      </select>
    </section>

    <!-- 섹션 4: 선호 지역 선택 (선택) -->
    <section class="mb-4">
      <div class="fw-semibold mb-2">📍 선호 지역 (선택)</div>
      <div class="d-flex flex-wrap gap-2">
        <button type="button" class="tag">제주</button>
        <button type="button" class="tag">강릉</button>
        <button type="button" class="tag">부산</button>
        <button type="button" class="tag">경주</button>
        <button type="button" class="tag">속초</button>
        <button type="button" class="tag">전주</button>
        <button type="button" class="tag">여수</button>
        <button type="button" class="tag">춘천</button>
        <button type="button" class="tag">남해</button>
        <button type="button" class="tag">통영</button>
      </div>
    </section>

    <!-- CTA 영역 -->
    <section class="mb-2">
      <CButton block @click="goResult">장소 추천받기</CButton>
      <button type="button" class="btn btn-link btn-sm mt-2 px-0" @click="goBack">이전으로</button>
    </section>
  </div>
  
</template>

<script setup>
// 정적 UI + 라우팅만 허용 — 상태/바인딩/계산 로직 없음
import CButton from '@/components/common/CButton.vue'
import { useRouter } from 'vue-router'

const router = useRouter()
function goResult() {
  // 장소 추천 결과 화면으로 라우팅만 수행
  router.push('/recommend/place/result')
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
}
.option-card:focus-visible { outline: 2px solid #80bdff; outline-offset: 2px; }
.option-card:hover { border-color: #d1d5db; }
.option-card .icon { font-size: 24px; margin-bottom: 6px; }
.option-card .title { font-weight: 600; margin-bottom: 4px; }

.tag {
  appearance: none;
  border: 1px solid #e5e7eb;
  background: #fff;
  border-radius: 999px;
  padding: 6px 12px;
  font-size: .9rem;
}
.tag:focus-visible { outline: 2px solid #80bdff; outline-offset: 2px; }
.tag:hover { border-color: #d1d5db; }
</style>
