<template>
  <div class="container py-5">
    <!-- Hero: 컨셉 전달만, 기능·CTA 강조 금지 -->
    <section class="text-center mb-5">
      <h1 class="display-6 fw-semibold mb-3">일과 휴식을 함께 고려하는 추천 서비스</h1>
      <p class="lead text-muted mb-0">Chill & Code는 여행 정보 나열이 아닌, 추천과 선택을 돕는 서비스입니다.</p>
    </section>

    <!-- What First: 사용자가 먼저 이해해야 할 것 -->
    <section class="mb-5">
      <h2 class="h5 mb-3">우리가 먼저 전하는 것</h2>
      <ul class="list-unstyled small text-muted mb-0">
        <li class="mb-2">• 일(work)과 휴식(chill)을 함께 고려합니다.</li>
        <li class="mb-2">• 여행 정보를 나열하지 않습니다.</li>
        <li class="mb-2">• 추천과 결정을 돕는 것이 목적입니다.</li>
      </ul>
    </section>

    <!-- So What: 탐색의 시작점(텍스트 링크 수준) -->
    <section>
      <h2 class="h6 mb-2">탐색을 시작해 보세요</h2>
      <p class="mb-0">
        <RouterLink class="link-primary me-3" to="/recommend">기간 추천 보기</RouterLink>
        <RouterLink class="link-primary" to="/recommend/place">장소 추천 보기</RouterLink>
      </p>
    </section>
  </div>
</template>
