<template>
  <div class="container py-5">
    <!-- Hero Section -->
    <section class="text-center mb-5">
      <h1 class="display-6 fw-semibold mb-3">일과 휴식의 완벽한 균형, Chill & Code</h1>
      <p class="lead text-muted mb-4">데이터 기반 AI 분석으로 당신에게 최적화된 워케이션 시기와 장소를 추천합니다.</p>
      <div class="d-flex justify-content-center gap-2">
        <RouterLink class="btn btn-primary btn-lg" to="/schedule">📅 캘린더 시작하기</RouterLink>
        <RouterLink class="btn btn-outline-primary btn-lg" to="/recommend">📍 추천 장소 둘러보기</RouterLink>
      </div>
    </section>

    <!-- Feature Grid Section -->
    <section class="mb-5">
      <div class="text-center mb-4">
        <h2 class="h4 mb-2">왜 Chill & Code인가요?</h2>
        <p class="text-muted mb-0">스마트한 워케이션 계획을 위한 모든 핵심 기능을 제공합니다.</p>
      </div>

      <div class="row g-4">
        <div class="col-12 col-md-6 col-lg-3">
          <div class="card h-100">
            <div class="card-body">
              <div class="fw-semibold mb-2">📆 스마트 일정 관리</div>
              <p class="mb-0 text-muted">개인·업무·워케이션 일정을 한눈에, 최적의 시간을 손쉽게 찾으세요.</p>
            </div>
          </div>
        </div>
        <div class="col-12 col-md-6 col-lg-3">
          <div class="card h-100">
            <div class="card-body">
              <div class="fw-semibold mb-2">📈 데이터 기반 분석</div>
              <p class="mb-0 text-muted">AI가 일정 패턴을 분석해 최적 기간을 추천합니다.</p>
            </div>
          </div>
        </div>
        <div class="col-12 col-md-6 col-lg-3">
          <div class="card h-100">
            <div class="card-body">
              <div class="fw-semibold mb-2">📍 맞춤형 장소 추천</div>
              <p class="mb-0 text-muted">업무 환경과 취향을 반영한 워케이션 장소를 제안합니다.</p>
            </div>
          </div>
        </div>
        <div class="col-12 col-md-6 col-lg-3">
          <div class="card h-100">
            <div class="card-body">
              <div class="fw-semibold mb-2">💬 커뮤니티 공유</div>
              <p class="mb-0 text-muted">사용자들의 노하우와 경험을 공유하며 함께 성장하세요.</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA Section -->
    <section>
      <div class="card bg-light border-0">
        <div class="card-body text-center py-5">
          <h3 class="h4 mb-3">지금 바로 스마트한 워케이션을 시작하세요</h3>
          <RouterLink class="btn btn-success btn-lg" to="/schedule">무료로 시작하기</RouterLink>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
p { margin: 8px 0 12px; }
ul { margin: 8px 0 12px; padding-left: 18px; }
li { margin: 6px 0; }
.btn + .btn { margin-left: 8px; }
</style>
  
