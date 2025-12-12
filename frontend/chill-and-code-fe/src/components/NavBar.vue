<template>
  <header class="sticky-top">
    <nav class="navbar navbar-expand-md navbar-light bg-white border-bottom shadow-sm">
      <div class="container">
        <!-- 좌측: 로고 + 서비스명 -->
        <RouterLink to="/" class="navbar-brand d-flex align-items-center">
          <span class="me-2">🌴</span>
          <span class="fw-semibold">Chill & Code</span>
        </RouterLink>

        <!-- 모바일: 햄버거 버튼 -->
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#mainNav"
          aria-controls="mainNav"
          aria-expanded="false"
          aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>

        <!-- 네비게이션 + 우측 버튼 -->
        <div class="collapse navbar-collapse" id="mainNav">
          <!-- 중앙: 주요 메뉴 -->
          <ul class="navbar-nav mx-auto mb-2 mb-md-0">
            <li class="nav-item">
              <RouterLink to="/" class="nav-link" exact-active-class="active">홈</RouterLink>
            </li>
            <li class="nav-item">
              <RouterLink to="/schedule" class="nav-link" exact-active-class="active">캘린더</RouterLink>
            </li>
            <li class="nav-item">
              <RouterLink to="/recommend" class="nav-link" exact-active-class="active">추천</RouterLink>
            </li>
            <li class="nav-item">
              <RouterLink to="/posts" class="nav-link" exact-active-class="active">커뮤니티</RouterLink>
            </li>
            <li class="nav-item">
              <RouterLink to="/mypage" class="nav-link" exact-active-class="active">마이페이지</RouterLink>
            </li>
          </ul>

          <!-- 우측: 로그인 버튼 -->
          <div class="d-flex">
            <RouterLink to="/login" class="btn btn-primary">로그인</RouterLink>
          </div>
        </div>
      </div>
    </nav>
  </header>
</template>

<script setup>
</script>

<style scoped>
</style>
