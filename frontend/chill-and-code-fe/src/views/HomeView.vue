<template>
  <div class="home-wrapper">
    <div class="container">
      <!-- Hero Section -->
      <section class="hero-section row align-items-center">
        <!-- 좌측 텍스트 영역 -->
        <div class="col-12 col-lg-5">
          <div class="hero-content">
            <h1 class="hero-title">
              Chill & Code <span class="emoji">🌴💻</span>
            </h1>
            <h2 class="hero-subtitle">
              Work From <span class="highlight">Paradise</span>, Not Just Home
            </h2>
            <p class="hero-description">
              일과 휴식을 함께 고려하는<br />
              워케이션 추천 서비스
            </p>
          </div>
        </div>

        <!-- 우측 이미지 콜라주 영역 -->
        <div class="col-12 col-lg-7">
          <div class="collage-wrapper">
            <div class="collage">
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
  background: #f0f4f5;
  min-height: calc(100vh - 64px);
  padding: 0;
}

/* Hero Section */
.hero-section {
  min-height: calc(100vh - 64px);
  padding: 4rem 0;
  display: flex;
  align-items: center;
}

.hero-content {
  padding-right: 2rem;
}

.hero-title {
  font-size: 3rem;
  font-weight: 700;
  color: #17a2b8;
  margin-bottom: 1rem;
  line-height: 1.2;
}

.emoji {
  font-size: 2.5rem;
}

.hero-subtitle {
  font-size: 1.5rem;
  font-weight: 400;
  color: #2c3e50;
  margin-bottom: 1.5rem;
  line-height: 1.4;
}

.highlight {
  color: #17a2b8;
  font-weight: 500;
}

.hero-description {
  font-size: 1rem;
  color: #6c757d;
  line-height: 1.6;
  margin-bottom: 0;
}

/* Collage */
.collage-wrapper {
  position: relative;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.collage {
  position: relative;
  width: 100%;
  height: 500px;
  max-width: 800px;
}

.img-card {
  position: absolute;
  border-radius: 20px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
  transition: transform 0.3s ease;
}

.img-card:hover {
  transform: scale(1.02);
  z-index: 10;
}

/* 오른쪽 상단 - 자연 배경 */
.img-card.main {
  width: 55%;
  height: 50%;
  right: 0;
  top: 0;
  z-index: 3;
}

/* 왼쪽 하단 - 실내 작업 */
.img-card.sub.left {
  width: 45%;
  height: 48%;
  left: 0;
  bottom: 0;
  z-index: 2;
}

/* 오른쪽 하단 - 도시 뷰 */
.img-card.sub.right {
  width: 45%;
  height: 48%;
  right: 5%;
  bottom: 0;
  z-index: 1;
}

.img-ph { 
  width: 100%; 
  height: 100%; 
  object-fit: cover; 
  display: block; 
}

/* Responsive */
@media (max-width: 992px) {
  .hero-section {
    padding: 3rem 0;
  }
  
  .hero-title {
    font-size: 2.25rem;
  }
  
  .hero-subtitle {
    font-size: 1.25rem;
  }
  
  .collage {
    height: 400px;
    margin-top: 3rem;
  }
}

@media (max-width: 768px) {
  .hero-title {
    font-size: 2rem;
  }
  
  .emoji {
    font-size: 2rem;
  }
  
  .hero-subtitle {
    font-size: 1.1rem;
  }
  
  .collage {
    height: 350px;
  }
  
  .hero-content {
    padding-right: 0;
    margin-bottom: 2rem;
  }
}

@media (max-width: 576px) {
  .hero-title {
    font-size: 1.75rem;
  }
  
  .hero-subtitle {
    font-size: 1rem;
  }
  
  .collage {
    height: 300px;
  }
}
</style>
