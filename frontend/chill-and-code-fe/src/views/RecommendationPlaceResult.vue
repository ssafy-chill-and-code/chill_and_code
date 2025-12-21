<template>
  <!-- 문서 전제: 정적 결과 화면 + UI 배치 검증용 -->
  <div class="container py-4">
    <!-- 상단 타이틀/서브 -->
    <header class="mb-3">
      <h1 class="h4 mb-1">당신을 위한 워케이션 장소</h1>
      <p class="text-muted small mb-0">12월 15일 ~ 12월 22일에 추천하는 최고의 워케이션 장소들입니다.</p>
    </header>

    <!-- 선택 요약 바 -->
    <section class="mb-4">
      <div class="summary-box p-3 border rounded bg-white d-flex flex-wrap gap-4">
        <div>
          <div class="small text-muted">기간</div>
          <div class="fw-semibold">12월 15일 ~ 12월 22일</div>
        </div>
        <div>
          <div class="small text-muted">스타일</div>
          <div class="fw-semibold">관광</div>
        </div>
        <div>
          <div class="small text-muted">예산</div>
          <div class="fw-semibold">150만원</div>
        </div>
        <div>
          <div class="small text-muted">추천 수</div>
          <div class="fw-semibold">6곳</div>
        </div>
      </div>
    </section>

    <!-- 장소 카드 6개 (3열 x 2행) -->
    <section class="mb-4">
      <div class="row g-3">
        <div v-for="n in 6" :key="n" class="col-12 col-md-6 col-lg-4">
          <CCard class="place-card">
            <!-- 카드 상단: 대표 이미지 + 배지/아이콘 -->
            <div class="thumb position-relative rounded overflow-hidden mb-2">
              <div class="thumb-inner d-flex align-items-center justify-content-center">🏖️</div>
              <div class="match-badge">⭐ 98%</div>
              <div class="top-icons">
                <button type="button" class="icon-btn" aria-label="찜">♡</button>
                <button type="button" class="icon-btn" aria-label="공유">⤴︎</button>
              </div>
            </div>

            <!-- 카드 본문 -->
            <div class="fw-semibold">제주 애월 카페거리</div>
            <div class="text-muted small mb-2">제주</div>
            <div class="small text-muted mb-2">바다와 인접한 카페들이 밀집해 있고, 조용한 분위기에서 일과 휴식을 모두 즐길 수 있습니다.</div>

            <!-- 태그 영역 -->
            <div class="d-flex flex-wrap gap-1 mb-2">
              <span class="hash">#카페</span>
              <span class="hash">#오션뷰</span>
              <span class="hash">#힐링</span>
            </div>

            <!-- 가격 정보 -->
            <div class="fw-semibold mb-2">월 150만원</div>

            <!-- 추천 이유 박스 -->
            <div class="reason-box small mb-3">
              <span class="fw-semibold">추천 이유</span> — 당신의 예산과 일정에 완벽하게 맞습니다
            </div>

            <!-- CTA -->
            <CButton block @click="goSchedule">이 장소로 일정 만들기</CButton>
          </CCard>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
// 정적 UI만, API/상태/바인딩 없음
import CButton from '@/components/common/CButton.vue'
import CCard from '@/components/common/CCard.vue'
import { useRouter } from 'vue-router'

const router = useRouter()
function goSchedule() {
  // 라우팅만 수행 (placeholder)
  router.push('/schedule/create')
}
</script>

<style scoped>
.summary-box { background: #fff; }

.place-card { background: #fff; }
.thumb { aspect-ratio: 16/9; background: #f3f4f6; }
.thumb-inner { width: 100%; height: 100%; font-size: 32px; }
.match-badge {
  position: absolute; left: 8px; top: 8px;
  background: rgba(0,0,0,.7); color: #fff; font-size: .85rem;
  padding: 4px 8px; border-radius: 8px;
}
.top-icons { position: absolute; right: 8px; top: 8px; display: flex; gap: 6px; }
.icon-btn {
  appearance: none; border: 1px solid rgba(255,255,255,.7); color: #fff; background: rgba(0,0,0,.35);
  border-radius: 999px; width: 28px; height: 28px; display: inline-flex; align-items: center; justify-content: center;
}

.hash {
  display: inline-block; background: #f3f4f6; color: #374151; border-radius: 999px; padding: 2px 8px; font-size: .8rem;
}
.reason-box { background: #f8f9fb; border: 1px solid #eef0f4; border-radius: 8px; padding: 8px 10px; }
</style>
