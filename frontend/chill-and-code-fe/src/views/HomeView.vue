<template>
  <div>
    <section class="p-5 mb-4 bg-light rounded-4 shadow-sm text-center">
      <div class="container py-4">
        <h2 class="fw-bold mb-2">일과 휴식의 완벽한 균형</h2>
        <h3 class="h5 text-muted mb-4">Chill & Code로 스마트한 워케이션 계획</h3>
        <div class="d-flex justify-content-center gap-2">
          <RouterLink class="btn btn-primary btn-lg" to="/schedule">
            <i class="bi bi-calendar-event me-1"></i> 캘린더 시작하기
          </RouterLink>
          <RouterLink class="btn btn-outline-secondary btn-lg disabled" to="#" aria-disabled="true">
            <i class="bi bi-geo-alt"></i> 추천 장소(준비중)
          </RouterLink>
        </div>
      </div>
    </section>

    <section class="my-4">
      <div class="row g-3">
        <div class="col-12 col-md-4">
          <div class="card h-100 shadow-sm rounded-3">
            <div class="card-body">
              <div class="d-flex align-items-center gap-2 mb-2">
                <i class="bi bi-check2-square"></i>
                <strong>스마트 일정 관리</strong>
              </div>
              <p class="mb-0 text-muted">개인/업무/워케이션 일정을 한눈에.</p>
            </div>
          </div>
        </div>
        <div class="col-12 col-md-4">
          <div class="card h-100 shadow-sm rounded-3">
            <div class="card-body">
              <div class="d-flex align-items-center gap-2 mb-2">
                <i class="bi bi-graph-up"></i>
                <strong>데이터 기반 분석</strong>
              </div>
              <p class="mb-0 text-muted">패턴 분석으로 최적 기간 추천.</p>
            </div>
          </div>
        </div>
        <div class="col-12 col-md-4">
          <div class="card h-100 shadow-sm rounded-3">
            <div class="card-body">
              <div class="d-flex align-items-center gap-2 mb-2">
                <i class="bi bi-people"></i>
                <strong>커뮤니티 공유</strong>
              </div>
              <p class="mb-0 text-muted">경험 공유와 소통(기능 유지).</p>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>
