<template>
  <div class="home-wrapper">
    <div class="container py-5">
      <!-- Hero Section -->
      <section class="hero-section row g-5 align-items-center mb-5">
        <!-- 좌측 텍스트 영역 -->
        <div class="col-12 col-lg-6">
          <div class="hero-content">
            <span class="badge-label">Chill & Code</span>
            <h1 class="hero-title">
              일과 휴식을 함께<br />
              고려하는 <span class="gradient-text">워케이션 추천</span>
            </h1>
            <p class="hero-description">
              이 서비스는 여행 정보를 나열하는 곳이 아닙니다.<br />
              추천과 선택을 돕는, 컨셉 중심의 워케이션 가이드입니다.
            </p>
            <div class="cta-buttons">
              <RouterLink to="/recommend" class="btn btn-primary btn-lg">
                <span>🎯</span> 워케이션 추천 시작하기
              </RouterLink>
            </div>
          </div>
        </div>

        <!-- 우측 이미지 콜라주 영역 -->
        <div class="col-12 col-lg-6">
          <div class="collage-wrapper">
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
        </div>
      </section>

      <!-- Features Section -->
      <section class="features-section mb-5">
        <div class="text-center mb-4">
          <h2 class="section-title">워케이션을 더 스마트하게</h2>
          <p class="section-subtitle">AI가 당신의 일정과 선호도를 분석하여 최적의 장소를 추천합니다</p>
        </div>
        <div class="row g-4">
          <div class="col-12 col-md-4">
            <div class="feature-card">
              <div class="feature-icon">📅</div>
              <h3 class="feature-title">스마트 기간 추천</h3>
              <p class="feature-text">캘린더 일정을 분석하여 최적의 워케이션 기간을 제안합니다</p>
            </div>
          </div>
          <div class="col-12 col-md-4">
            <div class="feature-card">
              <div class="feature-icon">🏖️</div>
              <h3 class="feature-title">맞춤형 장소 추천</h3>
              <p class="feature-text">당신의 스타일과 예산에 맞는 워케이션 장소를 찾아드립니다</p>
            </div>
          </div>
          <div class="col-12 col-md-4">
            <div class="feature-card">
              <div class="feature-icon">✨</div>
              <h3 class="feature-title">일정 관리</h3>
              <p class="feature-text">워케이션 일정을 체계적으로 관리하고 공유할 수 있습니다</p>
            </div>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import heroMain from '@/assets/hero/hero-main.webp'
import heroSubLeft from '@/assets/hero/hero-sub-left.webp'
import heroSubRight from '@/assets/hero/hero-sub-right.webp'
</script>

<style scoped>
.home-wrapper {
  background: linear-gradient(180deg, #f8f9fa 0%, #ffffff 100%);
  min-height: calc(100vh - 64px);
}

/* Hero Section */
.hero-section {
  padding: 3rem 0;
}

.hero-content {
  animation: fadeInUp 0.8s ease-out;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.badge-label {
  display: inline-block;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 0.75rem;
  font-weight: 600;
  letter-spacing: 0.5px;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  margin-bottom: 1.5rem;
  box-shadow: 0 4px 6px rgba(102, 126, 234, 0.2);
}

.hero-title {
  font-size: 2.5rem;
  font-weight: 700;
  line-height: 1.2;
  margin-bottom: 1.5rem;
  color: #1a202c;
}

.gradient-text {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-description {
  font-size: 1.1rem;
  line-height: 1.7;
  color: #64748b;
  margin-bottom: 2rem;
}

.cta-buttons {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  padding: 0.875rem 2rem;
  font-weight: 600;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

.btn-primary span {
  font-size: 1.2rem;
}

/* Collage */
.collage-wrapper {
  animation: fadeIn 1s ease-out 0.3s both;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.collage {
  position: relative;
  width: 100%;
  height: 400px;
}

@media (max-width: 992px) {
  .collage { height: 350px; }
}

@media (max-width: 576px) {
  .collage { height: 300px; }
}

.img-card {
  position: absolute;
  border-radius: 16px;
  overflow: hidden;
  background: #f8f9fa;
  border: 3px solid white;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.img-card:hover {
  transform: scale(1.05);
  z-index: 10;
}

.img-card.main {
  width: 56%;
  height: 70%;
  left: 22%;
  top: 5%;
  z-index: 2;
}

.img-card.sub.left {
  width: 38%;
  height: 48%;
  left: 4%;
  bottom: 4%;
  z-index: 1;
}

.img-card.sub.right {
  width: 38%;
  height: 48%;
  right: 4%;
  bottom: 6%;
  z-index: 1;
}

.img-ph { 
  width: 100%; 
  height: 100%; 
  object-fit: cover; 
  display: block; 
}

/* Features Section */
.features-section {
  padding: 3rem 0;
}

.section-title {
  font-size: 2rem;
  font-weight: 700;
  color: #1a202c;
  margin-bottom: 0.75rem;
}

.section-subtitle {
  font-size: 1.1rem;
  color: #64748b;
}

.feature-card {
  background: white;
  border-radius: 16px;
  padding: 2rem;
  text-align: center;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  height: 100%;
}

.feature-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.1);
}

.feature-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.feature-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1a202c;
  margin-bottom: 0.75rem;
}

.feature-text {
  color: #64748b;
  line-height: 1.6;
  margin-bottom: 0;
}

@media (max-width: 992px) {
  .hero-title {
    font-size: 2rem;
  }
  
  .hero-description {
    font-size: 1rem;
  }
}
</style>
