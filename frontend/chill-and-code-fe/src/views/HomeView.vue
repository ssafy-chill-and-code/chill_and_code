<template>
  <div class="container py-5">
    <!-- Hero: 좌 텍스트 / 우 이미지 콜라주 (문서 4-2) -->
    <section class="row g-4 align-items-center">
      <!-- 좌측 텍스트 영역 -->
      <div class="col-12 col-md-6">
        <div class="mb-3 small text-uppercase text-muted">Chill & Code</div>
        <h1 class="display-6 fw-semibold mb-3">일과 휴식을 함께 고려하는 워케이션 추천</h1>
        <p class="lead text-muted mb-0">
          이 서비스는 여행 정보를 나열하는 곳이 아닙니다. 추천과 선택을 돕는, 컨셉 중심의 워케이션 가이드입니다.
        </p>
      </div>

      <!-- 우측 이미지 콜라주 영역 -->
      <div class="col-12 col-md-6">
        <div class="collage" aria-hidden="true">
          <div class="img-card main">
            <img class="img-ph" :src="heroMain" alt="" role="presentation" fetchpriority="high" decoding="async" />
          </div>
          <div class="img-card sub left">
            <img class="img-ph" :src="heroSubLeft" alt="" role="presentation" loading="lazy" decoding="async" />
          </div>
          <div class="img-card sub right">
            <img class="img-ph" :src="heroSubRight" alt="" role="presentation" loading="lazy" decoding="async" />
          </div>
        </div>
      </div>
    </section>
  </div>
  
</template>

<script setup>
// 정적 배치만 반영. 문서 기준: API/동적 데이터/CTA 강조 없음.
import heroMain from '@/assets/hero/hero-main.webp'
import heroSubLeft from '@/assets/hero/hero-sub-left.webp'
import heroSubRight from '@/assets/hero/hero-sub-right.webp'
</script>

<style scoped>
/* 콜라주 컨테이너: 비율 고정, 내부 절대 배치 */
.collage {
  position: relative;
  width: 100%;
  height: 340px;
}

@media (max-width: 576px) {
  .collage { height: 300px; }
}

.img-card {
  position: absolute;
  border-radius: 12px;
  overflow: hidden;
  background: #f8f9fa;
  border: 1px solid #eee;
}

/* 메인 이미지: 중앙 배치 */
.img-card.main {
  width: 56%;
  height: 70%;
  left: 22%;
  top: 5%;
}

/* 보조 이미지: 좌하단 / 우하단 배치 */
.img-card.sub.left {
  width: 38%;
  height: 48%;
  left: 4%;
  bottom: 4%;
}

.img-card.sub.right {
  width: 38%;
  height: 48%;
  right: 4%;
  bottom: 6%;
}

.img-ph { width: 100%; height: 100%; object-fit: cover; display: block; }
</style>
