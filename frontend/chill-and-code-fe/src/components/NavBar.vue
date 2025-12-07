<template>
  <nav class="navbar">
    <div class="navbar-left">
      <span class="navbar-logo">🌴 Chill & Code</span>
    </div>

    <ul class="navbar-menu">
      <li><RouterLink to="/schedule">스케줄 생성</RouterLink></li>
      <li><RouterLink to="/posts">커뮤니티</RouterLink></li>
      <li><RouterLink to="/mypage">마이페이지</RouterLink></li>
    </ul>

    <div class="navbar-right">
      <RouterLink to="/login" class="navbar-link">로그인</RouterLink>
      <RouterLink to="/signup" class="navbar-link">회원가입</RouterLink>
    </div>
  </nav>
</template>

<script setup>
</script>

<style scoped>
</style>

