<template>
  <div class="home-wrapper">
    <div class="w-full px-6 lg:px-16 xl:px-24 py-8 lg:py-16">
      <!-- Hero Section -->
      <section class="hero-section mb-16 lg:mb-24">
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-12 lg:gap-20 xl:gap-32 items-center">
          <!-- 좌측 텍스트 영역 -->
          <div class="hero-content order-2 lg:order-1">
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
              <RouterLink to="/recommend" class="btn-primary">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
                </svg>
                <span>워케이션 가능 기간 분석</span>
              </RouterLink>
            </div>
          </div>

          <!-- 우측 이미지 콜라주 영역 -->
          <div class="collage-wrapper order-1 lg:order-2">
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
      <section class="features-section">
        <div class="max-w-[1600px] mx-auto">
          <div class="section-header text-center mb-12 lg:mb-16">
            <div class="section-badge">Features</div>
            <h2 class="section-title">워케이션을 더 스마트하게</h2>
            <p class="section-subtitle">AI가 당신의 일정과 선호도를 분석하여 최적의 장소를 추천합니다</p>
          </div>
          <div class="grid grid-cols-1 md:grid-cols-3 gap-6 lg:gap-8">
            <div class="feature-card">
              <div class="feature-icon">
                <svg class="w-12 h-12" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
              </div>
              <h3 class="feature-title">스마트 기간 추천</h3>
              <p class="feature-text">캘린더 일정을 분석하여 최적의 워케이션 기간을 제안합니다</p>
            </div>
            <div class="feature-card">
              <div class="feature-icon">
                <svg class="w-12 h-12" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                </svg>
              </div>
              <h3 class="feature-title">맞춤형 장소 추천</h3>
              <p class="feature-text">당신의 스타일과 예산에 맞는 워케이션 장소를 찾아드립니다</p>
            </div>
            <div class="feature-card">
              <div class="feature-icon">
                <svg class="w-12 h-12" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                </svg>
              </div>
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
  background: #ffffff;
  min-height: calc(100vh - 64px);
}

/* Hero Section */
.hero-section {
  position: relative;
  padding: 2rem 0;
  overflow: hidden;
  /* 부모의 padding을 벗어나 화면 전체 너비 사용 */
  margin-left: -1.5rem;
  margin-right: -1.5rem;
  padding-left: 1.5rem;
  padding-right: 1.5rem;
}

@media (min-width: 1024px) {
  .hero-section {
    margin-left: -4rem;
    margin-right: -4rem;
    padding-left: 4rem;
    padding-right: 4rem;
  }
}

@media (min-width: 1280px) {
  .hero-section {
    margin-left: -6rem;
    margin-right: -6rem;
    padding-left: 6rem;
    padding-right: 6rem;
  }
}

/* 미묘한 그라데이션 배경 */
.hero-section::before {
  content: '';
  position: absolute;
  top: -10%;
  left: 0;
  right: 0;
  bottom: -10%;
  background: 
    radial-gradient(ellipse 1400px 900px at 15% 40%, rgba(148, 163, 184, 0.04) 0%, transparent 65%),
    radial-gradient(ellipse 1200px 800px at 85% 65%, rgba(71, 85, 105, 0.05) 0%, transparent 65%),
    radial-gradient(ellipse 1000px 600px at 50% 5%, rgba(203, 213, 225, 0.035) 0%, transparent 55%),
    linear-gradient(to bottom, rgba(248, 250, 252, 0.7) 0%, rgba(255, 255, 255, 0) 100%);
  z-index: 0;
  pointer-events: none;
}

/* 미묘한 dot 패턴 */
.hero-section::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: 
    radial-gradient(circle, rgba(148, 163, 184, 0.08) 1px, transparent 1px);
  background-size: 24px 24px;
  z-index: 0;
  opacity: 0.4;
  pointer-events: none;
}

/* 컨텐츠를 배경 위로 */
.hero-section > * {
  position: relative;
  z-index: 1;
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
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
  color: white;
  font-size: 0.75rem;
  font-weight: 700;
  letter-spacing: 0.5px;
  padding: 0.5rem 1.25rem;
  border-radius: 24px;
  margin-bottom: 1.5rem;
  box-shadow: 0 2px 8px rgba(30, 41, 59, 0.2);
}

.hero-title {
  font-size: clamp(2rem, 5vw, 3rem);
  font-weight: 800;
  line-height: 1.2;
  margin-bottom: 1.5rem;
  color: #0f172a;
  letter-spacing: -0.02em;
}

.gradient-text {
  background: linear-gradient(135deg, #1e293b 0%, #475569 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-description {
  font-size: 1.125rem;
  line-height: 1.8;
  color: #64748b;
  margin-bottom: 2.5rem;
  font-weight: 400;
}

.cta-buttons {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.btn-primary {
  background: #1e293b;
  color: white;
  border: none;
  padding: 1rem 2rem;
  font-weight: 600;
  font-size: 1rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(30, 41, 59, 0.25);
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  gap: 0.625rem;
  text-decoration: none;
}

.btn-primary:hover {
  background: #0f172a;
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(30, 41, 59, 0.35);
}

.btn-primary:active {
  transform: translateY(0);
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

@media (max-width: 1024px) {
  .collage { height: 350px; }
}

@media (max-width: 640px) {
  .collage { height: 300px; }
}

.img-card {
  position: absolute;
  border-radius: 20px;
  overflow: hidden;
  background: #ffffff;
  border: 1px solid rgba(226, 232, 240, 0.8);
  box-shadow: 
    0 1px 3px rgba(0, 0, 0, 0.05),
    0 10px 40px rgba(0, 0, 0, 0.08),
    0 0 0 1px rgba(255, 255, 255, 0.1) inset;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.img-card:hover {
  transform: translateY(-4px) scale(1.02);
  box-shadow: 
    0 4px 6px rgba(0, 0, 0, 0.05),
    0 20px 60px rgba(0, 0, 0, 0.12),
    0 0 0 1px rgba(255, 255, 255, 0.2) inset;
  border-color: rgba(203, 213, 225, 0.9);
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
  position: relative;
  padding: 4rem 0;
  background: linear-gradient(to bottom, rgba(255, 255, 255, 0) 0%, rgba(248, 250, 252, 0.4) 50%, rgba(255, 255, 255, 0) 100%);
}

/* 상단 구분선 효과 */
.features-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 90%;
  max-width: 1200px;
  height: 1px;
  background: linear-gradient(to right, transparent 0%, rgba(203, 213, 225, 0.5) 50%, transparent 100%);
}

/* Section Header */
.section-header {
  position: relative;
  padding: 2.5rem 2rem;
  margin-bottom: 1rem;
}

.section-header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 100%;
  max-width: 800px;
  height: 100%;
  background: 
    radial-gradient(ellipse 600px 300px at 50% 50%, rgba(148, 163, 184, 0.04) 0%, transparent 70%);
  border-radius: 24px;
  z-index: 0;
  pointer-events: none;
}

.section-header > * {
  position: relative;
  z-index: 1;
}

.section-badge {
  display: inline-block;
  background: linear-gradient(135deg, rgba(30, 41, 59, 0.05) 0%, rgba(51, 65, 85, 0.08) 100%);
  color: #475569;
  font-size: 0.6875rem;
  font-weight: 700;
  letter-spacing: 0.1em;
  text-transform: uppercase;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  margin-bottom: 1.25rem;
  border: 1px solid rgba(203, 213, 225, 0.4);
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.03);
}

.section-title {
  font-size: clamp(1.75rem, 4vw, 2.5rem);
  font-weight: 800;
  color: #0f172a;
  margin-bottom: 1rem;
  letter-spacing: -0.02em;
}

.section-subtitle {
  font-size: 1.125rem;
  color: #64748b;
  font-weight: 400;
  max-width: 600px;
  margin: 0 auto;
}

.feature-card {
  background: white;
  border-radius: 16px;
  padding: 2.5rem 2rem;
  text-align: center;
  transition: all 0.3s ease;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  border: 1px solid #e2e8f0;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.feature-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.1);
  border-color: #cbd5e1;
}

.feature-icon {
  width: 80px;
  height: 80px;
  background: #ffffff;
  border: 2px solid #e2e8f0;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 1.5rem;
  color: #1e293b;
  transition: all 0.3s ease;
}

.feature-card:hover .feature-icon {
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
  color: white;
  transform: scale(1.05);
}

.feature-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: #0f172a;
  margin-bottom: 1rem;
}

.feature-text {
  color: #64748b;
  line-height: 1.7;
  margin-bottom: 0;
  font-size: 0.9375rem;
}

@media (max-width: 1024px) {
  .hero-title {
    font-size: 2.25rem;
  }
  
  .hero-description {
    font-size: 1rem;
  }
  
  .section-title {
    font-size: 2rem;
  }
}

@media (max-width: 640px) {
  .hero-section {
    padding: 1rem 0;
  }
  
  .features-section {
    padding: 2rem 0;
  }
  
  .feature-card {
    padding: 2rem 1.5rem;
  }
}
</style>
