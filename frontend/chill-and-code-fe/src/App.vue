<script setup>
import { onMounted } from 'vue'
import { RouterView } from 'vue-router'
import NavBar from '@/components/NavBar.vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
onMounted(async () => {
  try {
    await userStore.fetchProfile()
  } catch (e) {
    // 미인증 등은 무시하고 진행
  }
})
</script>

<template>
  <div>
    <NavBar />
    <main class="container mt-4">
      <RouterView />
    </main>
  </div>
</template>
