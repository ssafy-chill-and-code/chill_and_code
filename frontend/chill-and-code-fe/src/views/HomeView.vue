<template>
  <div class="container py-5">
    <!-- Hero: 추천 플로우 진입 동선 우선 노출 -->
    <section class="text-center mb-5">
      <h1 class="display-6 fw-semibold mb-3">데이터 기반 추천으로 워케이션을 더 똑똑하게</h1>
      <p class="lead text-muted mb-4">백엔드 API 기준으로 설계된 추천·결정 UX. 지금 바로 시작하세요.</p>
      <div class="d-flex flex-wrap justify-content-center gap-2">
        <RouterLink class="btn btn-primary btn-lg" to="/recommend">🗓️ 기간 추천 시작하기</RouterLink>
        <RouterLink class="btn btn-outline-primary btn-lg" to="/recommend/place">📍 장소 추천 시작하기</RouterLink>
      </div>
    </section>

    <!-- 추천 기능 하이라이트: 공통 컴포넌트 기반 구조 -->
    <section class="mb-5">
      <div class="text-center mb-4">
        <h2 class="h4 mb-2">추천 · 결정 핵심 기능</h2>
        <p class="text-muted mb-0">정보 우선순위와 시각적 그룹화로 빠르게 결정할 수 있도록 돕습니다.</p>
      </div>

      <div class="row g-3">
        <div class="col-12 col-md-6 col-lg-3">
          <div class="card h-100">
            <div class="card-body">
              <div class="fw-semibold mb-1">기간 추천</div>
              <div class="small text-muted mb-3">업무/일정 패턴을 반영해 최적 기간을 제안합니다.</div>
              <RouterLink class="btn btn-sm btn-outline-primary" to="/recommend">바로가기</RouterLink>
            </div>
          </div>
        </div>
        <div class="col-12 col-md-6 col-lg-3">
          <div class="card h-100">
            <div class="card-body">
              <div class="fw-semibold mb-1">장소 추천</div>
              <div class="small text-muted mb-3">업무환경/취향을 고려한 장소를 큐레이션합니다.</div>
              <RouterLink class="btn btn-sm btn-outline-primary" to="/recommend/place">바로가기</RouterLink>
            </div>
          </div>
        </div>
        <div class="col-12 col-md-6 col-lg-3">
          <div class="card h-100">
            <div class="card-body">
              <div class="fw-semibold mb-1">저장 · 공유</div>
              <div class="small text-muted mb-2">추천 결과 저장/공유는 백엔드 연동 후 제공됩니다.</div>
              <span class="badge text-bg-secondary">준비중 (COMING SOON)</span>
            </div>
          </div>
        </div>
        <div class="col-12 col-md-6 col-lg-3">
          <div class="card h-100">
            <div class="card-body">
              <div class="fw-semibold mb-1">통합 일정</div>
              <div class="small text-muted mb-2">캘린더 연동은 추후 안내 예정입니다.</div>
              <span class="badge text-bg-secondary">준비중 (COMING SOON)</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 최근 추천/활동: 동적 데이터 의존 → 준비중 명시 -->
    <section class="mb-5">
      <div class="d-flex align-items-center justify-content-between mb-2">
        <h2 class="h6 mb-0">최근 추천 기록</h2>
        <span class="badge text-bg-secondary" title="백엔드 API 제공 전까지 정적 처리">준비중 (COMING SOON)</span>
      </div>
      <div class="card">
        <div class="card-body text-muted">
          기록 데이터는 백엔드 API 확정 시 표시됩니다.
        </div>
      </div>
    </section>

    <!-- CTA Section -->
    <section>
      <div class="card bg-light border-0">
        <div class="card-body text-center py-5">
          <h3 class="h4 mb-3">지금 바로 추천을 받아보세요</h3>
          <div class="d-flex flex-wrap justify-content-center gap-2">
            <RouterLink class="btn btn-success btn-lg" to="/recommend">기간 추천</RouterLink>
            <RouterLink class="btn btn-outline-success btn-lg" to="/recommend/place">장소 추천</RouterLink>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>
