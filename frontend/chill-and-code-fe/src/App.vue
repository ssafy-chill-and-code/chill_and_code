<script setup>
import { RouterView } from 'vue-router'
import NavBar from '@/components/NavBar.vue'
</script>

<template>
  <div class="container">
    <NavBar />
    <main class="main">
      <RouterView />
    </main>
  </div>
</template>
