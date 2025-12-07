<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const email = ref('')
const password = ref('')
const userStore = useUserStore()
const router = useRouter()

const onLogin = async () => {
  try {
    await userStore.login({ email: email.value, password: password.value })
    router.push('/schedule')
  } catch (e) {
    alert('로그인 실패')
  }
}
</script>

<template>
  <div style="max-width: 480px; margin: 24px auto;">
    <h2>로그인</h2>
    <div style="margin: 8px 0;">
      <label>이메일</label>
      <input v-model="email" type="email" style="display:block; width:100%; padding:6px;" />
    </div>
    <div style="margin: 8px 0;">
      <label>비밀번호</label>
      <input v-model="password" type="password" style="display:block; width:100%; padding:6px;" />
    </div>
    <button @click="onLogin" style="padding:6px 12px;">로그인</button>
  </div>
  
</template>

