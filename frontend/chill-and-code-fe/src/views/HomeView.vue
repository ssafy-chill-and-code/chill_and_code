<template>
  <div class="home-wrapper">
    <!-- Top Hero Section with 3 Images -->
    <section class="top-hero-section">
      <div class="hero-grid">
        <div class="hero-item">
          <div class="hero-overlay"></div>
          <img src="@/assets/main/main1.jpeg" alt="Chill" class="hero-image" />
          <div class="hero-text">
            <h2 class="hero-title">CHILL</h2>
          </div>
        </div>
        <div class="hero-item">
          <div class="hero-overlay"></div>
          <img src="@/assets/main/main2.jpeg" alt="And" class="hero-image" />
          <div class="hero-text">
            <h2 class="hero-title">AND</h2>
          </div>
        </div>
        <div class="hero-item">
          <div class="hero-overlay"></div>
          <img src="@/assets/main/main3.jpeg" alt="Code" class="hero-image" />
          <div class="hero-text">
            <h2 class="hero-title">CODE</h2>
          </div>
        </div>
      </div>
      <div class="scroll-indicator">
        <span>scroll</span>
        <svg class="w-6 h-6 animate-bounce" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 14l-7 7m0 0l-7-7m7 7V3" />
        </svg>
      </div>
    </section>

    <!-- Original Content -->
    <div class="main-content w-full px-6 lg:px-16 xl:px-24 py-8 lg:py-16">
      <!-- Features Section -->
      <section class="features-section">
        <div class="max-w-[1600px] mx-auto">
          <div class="section-header text-center mb-12 lg:mb-16">
            <div class="section-badge">Features</div>
            <h2 class="section-title">워케이션을 더 스마트하게</h2>
            <p class="section-subtitle">AI가 당신의 일정과 선호도를 분석하여 최적의 장소를 추천합니다</p>
          </div>
          <div class="grid grid-cols-1 md:grid-cols-3 gap-6 lg:gap-8">
            <div @click="scrollToPreview('period')" class="feature-card cursor-pointer">
              <div @click="navigateToPage($event, '/recommend')" class="feature-icon cursor-pointer">
                <svg class="w-12 h-12" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
              </div>
              <h3 class="feature-title">스마트 기간 추천</h3>
              <p class="feature-text">캘린더 일정을 분석하여 최적의 워케이션 기간을 제안합니다</p>
            </div>
            <div @click="scrollToPreview('place')" class="feature-card cursor-pointer">
              <div @click="navigateToPage($event, '/recommend')" class="feature-icon cursor-pointer">
                <svg class="w-12 h-12" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                </svg>
              </div>
              <h3 class="feature-title">맞춤형 장소 추천</h3>
              <p class="feature-text">당신의 스타일과 예산에 맞는 워케이션 장소를 찾아드립니다</p>
            </div>
            <div @click="scrollToPreview('schedule')" class="feature-card cursor-pointer">
              <div @click="navigateToPage($event, '/schedule')" class="feature-icon cursor-pointer">
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

      <!-- Preview: 스마트 기간 추천 -->
      <section id="preview-period" class="preview-section">
        <div class="max-w-[1600px] mx-auto">
          <div class="preview-header">
            <h3 class="preview-title">스마트 기간 추천</h3>
            <p class="preview-subtitle">AI가 당신의 일정을 분석하여 최적의 워케이션 기간을 찾아드립니다</p>
          </div>
          <div class="preview-content grid grid-cols-1 lg:grid-cols-2 gap-6 lg:gap-8 items-start">
            <!-- 미니 캘린더 UI -->
            <div class="calendar-preview">
              <div class="calendar-header">
                <h4 class="calendar-month">2024년 6월</h4>
                <div class="calendar-legend">
                  <span class="legend-item"><span class="dot work"></span>업무 일정</span>
                  <span class="legend-item"><span class="dot personal"></span>개인 일정</span>
                  <span class="legend-item"><span class="dot recommended"></span>추천 기간</span>
                </div>
              </div>
              <div class="calendar-grid">
                <div class="calendar-day-header">일</div>
                <div class="calendar-day-header">월</div>
                <div class="calendar-day-header">화</div>
                <div class="calendar-day-header">수</div>
                <div class="calendar-day-header">목</div>
                <div class="calendar-day-header">금</div>
                <div class="calendar-day-header">토</div>
                
                <!-- 날짜 셀들 (정적 예시) -->
                <div class="calendar-cell empty"></div>
                <div class="calendar-cell empty"></div>
                <div class="calendar-cell empty"></div>
                <div class="calendar-cell empty"></div>
                <div class="calendar-cell empty"></div>
                <div class="calendar-cell empty"></div>
                <div class="calendar-cell">1</div>
                <div class="calendar-cell">2</div>
                <div class="calendar-cell work">3</div>
                <div class="calendar-cell work">4</div>
                <div class="calendar-cell work">5</div>
                <div class="calendar-cell">6</div>
                <div class="calendar-cell">7</div>
                <div class="calendar-cell">8</div>
                <div class="calendar-cell">9</div>
                <div class="calendar-cell">10</div>
                <div class="calendar-cell personal">11</div>
                <div class="calendar-cell">12</div>
                <div class="calendar-cell">13</div>
                <div class="calendar-cell">14</div>
                <div class="calendar-cell recommended">15</div>
                <div class="calendar-cell recommended">16</div>
                <div class="calendar-cell recommended">17</div>
                <div class="calendar-cell recommended">18</div>
                <div class="calendar-cell recommended">19</div>
                <div class="calendar-cell recommended">20</div>
                <div class="calendar-cell recommended">21</div>
                <div class="calendar-cell recommended">22</div>
                <div class="calendar-cell">23</div>
                <div class="calendar-cell work">24</div>
                <div class="calendar-cell work">25</div>
                <div class="calendar-cell work">26</div>
                <div class="calendar-cell work">27</div>
                <div class="calendar-cell work">28</div>
                <div class="calendar-cell">29</div>
                <div class="calendar-cell">30</div>
              </div>
            </div>
            <!-- 추천 결과 카드 -->
            <div class="recommendation-result">
              <div class="result-icon">
                <svg class="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              </div>
              <div class="result-content">
                <h5 class="result-title">추천 워케이션 기간</h5>
                <p class="result-period">2024년 6월 15일 ~ 6월 22일 (8일)</p>
                <p class="result-reason">이 기간은 업무 일정이 적고, 날씨가 좋아 워케이션에 최적입니다.</p>
              </div>
              <RouterLink to="/recommend" class="result-cta">
                <span>내 일정 분석하기</span>
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                </svg>
              </RouterLink>
            </div>
          </div>
        </div>
      </section>

      <!-- Preview: 맞춤형 장소 추천 -->
      <section id="preview-place" class="preview-section">
        <div class="max-w-[1600px] mx-auto">
          <div class="preview-header">
            <h3 class="preview-title">맞춤형 장소 추천</h3>
            <p class="preview-subtitle">당신의 스타일과 예산에 맞는 완벽한 워케이션 장소</p>
          </div>
          <div class="preview-content">
            <div class="place-cards">
              <!-- 장소 카드 1 -->
              <div class="place-card">
                <div class="place-image" style="background-image: url('https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=600&h=400&fit=crop'); background-size: cover; background-position: center;">
                  <div class="place-badge">제주</div>
                  <div class="place-rating">
                    <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                      <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                    </svg>
                    <span>4.8</span>
                  </div>
                </div>
                <div class="place-info">
                  <h4 class="place-name">제주 오션뷰 워케이션 하우스</h4>
                  <p class="place-desc">바다가 보이는 조용한 작업 공간과 빠른 와이파이</p>
                  <div class="place-tags">
                    <span class="place-tag">조용한 환경</span>
                    <span class="place-tag">고속 인터넷</span>
                    <span class="place-tag">카페 근처</span>
                  </div>
                  <div class="place-price">
                    <span class="price-label">1박당</span>
                    <span class="price-value">89,000원</span>
                  </div>
                </div>
              </div>

              <!-- 장소 카드 2 -->
              <div class="place-card">
                <div class="place-image" style="background-image: url('https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=600&h=400&fit=crop'); background-size: cover; background-position: center;">
                  <div class="place-badge">강릉</div>
                  <div class="place-rating">
                    <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                      <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                    </svg>
                    <span>4.9</span>
                  </div>
                </div>
                <div class="place-info">
                  <h4 class="place-name">강릉 모던 코워킹 스테이</h4>
                  <p class="place-desc">전용 작업실과 미팅룸이 있는 프리미엄 숙소</p>
                  <div class="place-tags">
                    <span class="place-tag">전용 작업실</span>
                    <span class="place-tag">미팅룸</span>
                    <span class="place-tag">커피 무료</span>
                  </div>
                  <div class="place-price">
                    <span class="price-label">1박당</span>
                    <span class="place-value">125,000원</span>
                  </div>
                </div>
              </div>

              <!-- 장소 카드 3 -->
              <div class="place-card">
                <div class="place-image" style="background-image: url('https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=600&h=400&fit=crop'); background-size: cover; background-position: center;">
                  <div class="place-badge">부산</div>
                  <div class="place-rating">
                    <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                      <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                    </svg>
                    <span>4.7</span>
                  </div>
                </div>
                <div class="place-info">
                  <h4 class="place-name">부산 해운대 워크앤칠</h4>
                  <p class="place-desc">해변 접근성이 좋은 힐링형 워케이션 공간</p>
                  <div class="place-tags">
                    <span class="place-tag">해변 도보 5분</span>
                    <span class="place-tag">루프탑</span>
                    <span class="place-tag">BBQ 가능</span>
                  </div>
                  <div class="place-price">
                    <span class="price-label">1박당</span>
                    <span class="price-value">95,000원</span>
                  </div>
                </div>
              </div>
            </div>
            <div class="preview-cta-container">
              <RouterLink to="/recommend" class="preview-cta-button">
                <span>더 많은 장소 추천받기</span>
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7l5 5m0 0l-5 5m5-5H6" />
                </svg>
              </RouterLink>
            </div>
          </div>
        </div>
      </section>

      <!-- Preview: 일정 관리 -->
      <section id="preview-schedule" class="preview-section">
        <div class="max-w-[1600px] mx-auto">
          <div class="preview-header">
            <h3 class="preview-title">일정 관리</h3>
            <p class="preview-subtitle">워케이션 일정을 한눈에 관리하고 팀원들과 공유하세요</p>
          </div>
          <div class="preview-content">
            <div class="schedule-cards">
              <!-- 일정 카드 1 -->
              <div class="schedule-card work">
                <div class="schedule-header">
                  <div class="schedule-type-badge work-badge">업무</div>
                  <span class="schedule-date">6월 3일 - 6월 5일</span>
                </div>
                <h4 class="schedule-title">프로젝트 마감 스프린트</h4>
                <p class="schedule-desc">최종 QA 및 배포 준비</p>
                <div class="schedule-time">09:00 - 18:00</div>
              </div>

              <!-- 일정 카드 2 -->
              <div class="schedule-card workation">
                <div class="schedule-header">
                  <div class="schedule-type-badge workation-badge">워케이션</div>
                  <span class="schedule-date">6월 15일 - 6월 22일</span>
                </div>
                <h4 class="schedule-title">제주 워케이션</h4>
                <p class="schedule-desc">오전 업무 + 오후 여유 시간</p>
                <div class="schedule-location">
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                  </svg>
                  <span>제주 오션뷰 워케이션 하우스</span>
                </div>
              </div>

              <!-- 일정 카드 3 -->
              <div class="schedule-card personal">
                <div class="schedule-header">
                  <div class="schedule-type-badge personal-badge">개인</div>
                  <span class="schedule-date">6월 11일</span>
                </div>
                <h4 class="schedule-title">병원 진료</h4>
                <p class="schedule-desc">정기 건강검진</p>
                <div class="schedule-time">14:00 - 15:00</div>
              </div>
            </div>
            <div class="schedule-summary">
              <div class="summary-card">
                <div class="summary-icon work-icon">
                  <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                  </svg>
                </div>
                <div class="summary-content">
                  <p class="summary-label">업무 일정</p>
                  <p class="summary-value">15일 / 60h</p>
                </div>
              </div>
              <div class="summary-card">
                <div class="summary-icon personal-icon">
                  <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                  </svg>
                </div>
                <div class="summary-content">
                  <p class="summary-label">개인 일정</p>
                  <p class="summary-value">3일</p>
                </div>
              </div>
              <div class="summary-card highlight">
                <div class="summary-icon workation-icon">
                  <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z" />
                  </svg>
                </div>
                <div class="summary-content">
                  <p class="summary-label">워케이션 가능</p>
                  <p class="summary-value">12일</p>
                </div>
              </div>
            </div>
            <div class="preview-cta-container">
              <RouterLink to="/schedule" class="preview-cta-button">
                <span>내 일정 관리하기</span>
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7l5 5m0 0l-5 5m5-5H6" />
                </svg>
              </RouterLink>
            </div>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
// 미리보기 섹션으로 스크롤
const scrollToPreview = (previewId) => {
  const element = document.getElementById(`preview-${previewId}`)
  if (element) {
    const navbarHeight = 64 // NavBar 높이
    const elementPosition = element.getBoundingClientRect().top + window.pageYOffset
    const offsetPosition = elementPosition - navbarHeight - 32 // 여유 공간 32px
    
    window.scrollTo({
      top: offsetPosition,
      behavior: 'smooth'
    })
  }
}

// 아이콘 클릭 시 페이지 이동 (이벤트 전파 방지)
const navigateToPage = (event, path) => {
  event.stopPropagation()
  window.location.href = path
}
</script>

<style scoped>
.home-wrapper {
  background: var(--brand-50);
  min-height: 100vh;
  position: relative;
  overflow-x: hidden;
  width: 100%;
}

.main-content {
  position: relative;
  z-index: 1;
  margin-top: 100vh;
  background: var(--brand-50);
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

/* 미묘한 그라데이션 배경 - 톤앤톤 적용 */
.hero-section::before {
  content: '';
  position: absolute;
  top: -10%;
  left: 0;
  right: 0;
  bottom: -10%;
  background: 
    radial-gradient(ellipse 1400px 900px at 15% 40%, rgba(168, 188, 198, 0.08) 0%, transparent 65%),
    radial-gradient(ellipse 1200px 800px at 85% 65%, rgba(107, 136, 150, 0.1) 0%, transparent 65%),
    radial-gradient(ellipse 1000px 600px at 50% 5%, rgba(201, 215, 222, 0.07) 0%, transparent 55%),
    linear-gradient(to bottom, rgba(238, 243, 245, 0.8) 0%, rgba(255, 255, 255, 0) 100%);
  z-index: 0;
  pointer-events: none;
}

/* 미묘한 dot 패턴 - 톤앤톤 적용 */
.hero-section::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: 
    radial-gradient(circle, rgba(138, 162, 174, 0.12) 1px, transparent 1px);
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
  background: linear-gradient(135deg, var(--brand-500) 0%, var(--brand-600) 100%);
  color: white;
  font-size: 0.75rem;
  font-weight: 700;
  letter-spacing: 0.5px;
  padding: 0.5rem 1.25rem;
  border-radius: 24px;
  margin-bottom: 1.5rem;
  box-shadow: 0 2px 8px rgba(107, 136, 150, 0.3);
}

.hero-title {
  font-size: clamp(2rem, 5vw, 3rem);
  font-weight: 800;
  line-height: 1.2;
  margin-bottom: 1.5rem;
  color: var(--brand-700);
  letter-spacing: -0.02em;
}

.gradient-text {
  background: linear-gradient(135deg, var(--brand-600) 0%, var(--brand-400) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-description {
  font-size: 1.125rem;
  line-height: 1.8;
  color: var(--brand-400);
  margin-bottom: 2.5rem;
  font-weight: 400;
}

.cta-buttons {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.btn-primary {
  background: var(--brand-500);
  color: white;
  border: none;
  padding: 1rem 2rem;
  font-weight: 600;
  font-size: 1rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(107, 136, 150, 0.3);
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  gap: 0.625rem;
  text-decoration: none;
}

.btn-primary:hover {
  background: var(--brand-600);
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
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(15px);
  -webkit-backdrop-filter: blur(15px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  box-shadow: 
    0 8px 32px 0 rgba(31, 38, 135, 0.2),
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
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-radius: 16px;
  padding: 2.5rem 2rem;
  text-align: center;
  transition: all 0.3s ease;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.15);
  border: 1px solid rgba(255, 255, 255, 0.4);
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

/* Preview Sections */
.preview-section {
  position: relative;
  padding: 5rem 0;
  background: linear-gradient(to bottom, rgba(248, 250, 252, 0.3) 0%, rgba(255, 255, 255, 0) 100%);
  border-top: 1px solid rgba(226, 232, 240, 0.6);
}

.preview-section:last-of-type {
  padding-bottom: 6rem;
}

.preview-header {
  text-align: center;
  margin-bottom: 3rem;
}

.preview-title {
  font-size: clamp(1.5rem, 3vw, 2rem);
  font-weight: 800;
  color: #0f172a;
  margin-bottom: 0.75rem;
  letter-spacing: -0.01em;
}

.preview-subtitle {
  font-size: 1rem;
  color: #64748b;
  font-weight: 400;
  max-width: 500px;
  margin: 0 auto;
}

.preview-content {
  /* grid layout is applied via Tailwind classes in template */
}

/* Calendar Preview */
.calendar-preview {
  background: white;
  border-radius: 18px;
  padding: 1.5rem;
  border: 1px solid #e2e8f0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  height: 100%;
  display: flex;
  flex-direction: column;
  max-width: 100%;
}

.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  flex-wrap: wrap;
  gap: 0.75rem;
}

.calendar-month {
  font-size: 1.0625rem;
  font-weight: 700;
  color: #0f172a;
}

.calendar-legend {
  display: flex;
  gap: 0.625rem;
  flex-wrap: wrap;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  font-size: 0.75rem;
  color: #64748b;
}

.legend-item .dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
}

.dot.work { background: #64748b; }
.dot.personal { background: #3b82f6; }
.dot.recommended { background: #10b981; }

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 0.375rem;
}

.calendar-day-header {
  padding: 0.5rem 0.25rem;
  text-align: center;
  font-size: 0.6875rem;
  font-weight: 700;
  color: #94a3b8;
  text-transform: uppercase;
  letter-spacing: 0.025em;
}

.calendar-cell {
  aspect-ratio: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.8125rem;
  font-weight: 500;
  color: #1e293b;
  border-radius: 6px;
  transition: all 0.2s ease;
}

.calendar-cell:not(.empty):hover {
  background: #f1f5f9;
  transform: scale(1.05);
  cursor: pointer;
}

.calendar-cell.empty {
  visibility: hidden;
}

.calendar-cell.work {
  background: #f1f5f9;
  color: #475569;
  font-weight: 600;
}

.calendar-cell.personal {
  background: #dbeafe;
  color: #1e40af;
  font-weight: 600;
}

.calendar-cell.recommended {
  background: linear-gradient(135deg, #d1fae5 0%, #a7f3d0 100%);
  color: #047857;
  font-weight: 700;
  box-shadow: 0 2px 4px rgba(16, 185, 129, 0.2);
}

/* Recommendation Result */
.recommendation-result {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
  background: linear-gradient(135deg, #f0fdf4 0%, #dcfce7 100%);
  padding: 1.5rem;
  border-radius: 18px;
  border: 1px solid #bbf7d0;
  max-width: 100%;
  align-self: center;
}

.result-icon {
  flex-shrink: 0;
  width: 44px;
  height: 44px;
  background: white;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #10b981;
  box-shadow: 0 2px 8px rgba(16, 185, 129, 0.15);
}

.result-content {
  flex: 1;
}

.result-title {
  font-size: 0.8125rem;
  font-weight: 600;
  color: #065f46;
  margin-bottom: 0.75rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.result-period {
  font-size: 1.375rem;
  font-weight: 800;
  color: #047857;
  margin-bottom: 0.875rem;
  line-height: 1.3;
}

.result-reason {
  font-size: 0.875rem;
  color: #059669;
  line-height: 1.65;
  margin-bottom: 0.75rem;
}

.result-cta {
  align-self: flex-start;
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  background: white;
  color: #047857;
  padding: 0.75rem 1.25rem;
  border-radius: 12px;
  font-weight: 600;
  font-size: 0.875rem;
  border: 1px solid #86efac;
  transition: all 0.3s ease;
  text-decoration: none;
}

.result-cta:hover {
  background: #047857;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(4, 120, 87, 0.25);
}

/* Place Cards */
.place-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
}

.place-card {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border-radius: 16px;
  overflow: hidden;
  border: 1px solid rgba(255, 255, 255, 0.4);
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.15);
  transition: all 0.3s ease;
}

.place-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  border-color: #cbd5e1;
}

.place-image {
  position: relative;
  height: 200px;
  background: linear-gradient(135deg, #e2e8f0 0%, #cbd5e1 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.place-badge {
  position: absolute;
  top: 12px;
  left: 12px;
  background: rgba(30, 41, 59, 0.9);
  color: white;
  padding: 0.375rem 0.875rem;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: 600;
  backdrop-filter: blur(8px);
}

.place-rating {
  position: absolute;
  top: 12px;
  right: 12px;
  background: rgba(255, 255, 255, 0.95);
  padding: 0.375rem 0.75rem;
  border-radius: 20px;
  display: flex;
  align-items: center;
  gap: 0.25rem;
  font-size: 0.875rem;
  font-weight: 600;
  color: #0f172a;
  backdrop-filter: blur(8px);
}

.place-rating svg {
  color: #fbbf24;
}

.place-info {
  padding: 1.5rem;
}

.place-name {
  font-size: 1.125rem;
  font-weight: 700;
  color: #0f172a;
  margin-bottom: 0.5rem;
  line-height: 1.3;
}

.place-desc {
  font-size: 0.875rem;
  color: #64748b;
  line-height: 1.6;
  margin-bottom: 1rem;
}

.place-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.place-tag {
  background: #f1f5f9;
  color: #475569;
  padding: 0.375rem 0.75rem;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 500;
}

.place-price {
  display: flex;
  align-items: baseline;
  gap: 0.5rem;
  padding-top: 1rem;
  border-top: 1px solid #f1f5f9;
}

.price-label {
  font-size: 0.875rem;
  color: #94a3b8;
}

.price-value {
  font-size: 1.25rem;
  font-weight: 700;
  color: #0f172a;
}

/* Schedule Cards */
.schedule-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.schedule-card {
  background: white;
  border-radius: 16px;
  padding: 1.5rem;
  border: 1px solid #e2e8f0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  transition: all 0.3s ease;
}

.schedule-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
}

.schedule-card.work {
  border-left: 4px solid #64748b;
}

.schedule-card.workation {
  border-left: 4px solid #0ea5e9;
  background: linear-gradient(to bottom right, #f0f9ff 0%, white 100%);
}

.schedule-card.personal {
  border-left: 4px solid #3b82f6;
}

.schedule-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.schedule-type-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
}

.work-badge {
  background: #f1f5f9;
  color: #475569;
}

.workation-badge {
  background: #e0f2fe;
  color: #0369a1;
}

.personal-badge {
  background: #dbeafe;
  color: #1e40af;
}

.schedule-date {
  font-size: 0.75rem;
  color: #94a3b8;
  font-weight: 500;
}

.schedule-title {
  font-size: 1.125rem;
  font-weight: 700;
  color: #0f172a;
  margin-bottom: 0.5rem;
}

.schedule-desc {
  font-size: 0.875rem;
  color: #64748b;
  margin-bottom: 0.75rem;
  line-height: 1.5;
}

.schedule-time {
  font-size: 0.875rem;
  color: #94a3b8;
  font-weight: 500;
}

.schedule-location {
  display: flex;
  align-items: center;
  gap: 0.375rem;
  font-size: 0.875rem;
  color: #0ea5e9;
  font-weight: 500;
}

.schedule-location svg {
  flex-shrink: 0;
}

/* Schedule Summary */
.schedule-summary {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.summary-card {
  background: white;
  border-radius: 16px;
  padding: 1.5rem;
  border: 1px solid #e2e8f0;
  display: flex;
  align-items: center;
  gap: 1rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  transition: all 0.3s ease;
}

.summary-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
}

.summary-card.highlight {
  background: linear-gradient(135deg, #fef3c7 0%, #fde68a 100%);
  border-color: #fbbf24;
}

.summary-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.work-icon {
  background: #f1f5f9;
  color: #475569;
}

.personal-icon {
  background: #dbeafe;
  color: #1e40af;
}

.workation-icon {
  background: white;
  color: #d97706;
}

.summary-content {
  flex: 1;
}

.summary-label {
  font-size: 0.875rem;
  color: #64748b;
  margin-bottom: 0.25rem;
}

.summary-card.highlight .summary-label {
  color: #92400e;
}

.summary-value {
  font-size: 1.5rem;
  font-weight: 800;
  color: #0f172a;
}

.summary-card.highlight .summary-value {
  color: #78350f;
}

/* CTA Container */
.preview-cta-container {
  display: flex;
  justify-content: center;
  margin-top: 2rem;
}

.preview-cta-button {
  display: inline-flex;
  align-items: center;
  gap: 0.75rem;
  background: #1e293b;
  color: white;
  padding: 1rem 2rem;
  border-radius: 12px;
  font-weight: 600;
  font-size: 1rem;
  text-decoration: none;
  box-shadow: 0 4px 12px rgba(30, 41, 59, 0.25);
  transition: all 0.3s ease;
}

.preview-cta-button:hover {
  background: #0f172a;
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(30, 41, 59, 0.35);
}

.preview-cta-button:active {
  transform: translateY(0);
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
  
  .place-cards,
  .schedule-cards {
    grid-template-columns: 1fr;
  }
  
  .calendar-preview {
    padding: 1.5rem;
  }
  
  .recommendation-result {
    flex-direction: column;
  }
  
  .result-cta {
    width: 100%;
    justify-content: center;
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
  
  .preview-section {
    padding: 3rem 0;
  }
  
  .calendar-grid {
    gap: 0.25rem;
  }
  
  .calendar-cell {
    font-size: 0.75rem;
  }
  
  .schedule-summary {
    grid-template-columns: 1fr;
  }
}

/* Top Hero Section Styles */
.top-hero-section {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  margin: 0;
  padding: 0;
  z-index: 0;
}

.hero-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  height: 100%;
  width: 100%;
  margin: 0;
  padding: 0;
  gap: 0;
}

.hero-item {
  position: relative;
  overflow: hidden;
  height: 100%;
  width: 100%;
}

.hero-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.hero-item:hover .hero-image {
  transform: scale(1.05);
}

.hero-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 1;
  transition: background 0.3s ease;
}

.hero-item:hover .hero-overlay {
  background: rgba(0, 0, 0, 0.4);
}

.hero-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 2;
  text-align: center;
  color: white;
  width: 100%;
  padding: 2rem;
}

.hero-title {
  font-size: clamp(3rem, 8vw, 7rem);
  font-weight: 900;
  letter-spacing: 0.05em;
  text-transform: uppercase;
  margin: 0;
  color: white;
  text-shadow: 0 4px 20px rgba(0, 0, 0, 0.5);
  animation: fadeInUp 0.8s ease-out;
}

.hero-subtitle {
  font-size: clamp(2rem, 6vw, 5rem);
  font-weight: 900;
  letter-spacing: 0.05em;
  text-transform: uppercase;
  margin: 0;
  text-shadow: 0 4px 20px rgba(0, 0, 0, 0.5);
  animation: fadeInUp 1s ease-out;
}

.scroll-indicator {
  position: fixed;
  bottom: 2rem;
  left: 50%;
  transform: translateX(-50%);
  z-index: 10;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  color: white;
  font-size: 0.875rem;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.1em;
  animation: fadeIn 1.5s ease-out;
}

/* 모바일 대응 */
@media (max-width: 768px) {
  .hero-grid {
    grid-template-columns: 1fr;
    grid-template-rows: repeat(3, 1fr);
  }
  
  .hero-title {
    font-size: clamp(2rem, 12vw, 4rem);
  }
  
  .hero-subtitle {
    font-size: clamp(1.5rem, 10vw, 3rem);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
</style>
