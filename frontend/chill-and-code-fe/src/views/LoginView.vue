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
  <div>
    <h2 class="page-title">로그인</h2>
    <div class="form-group">
      <label class="form-label">이메일</label>
      <input class="form-input" v-model="email" type="email" />
    </div>
    <div class="form-group">
      <label class="form-label">비밀번호</label>
      <input class="form-input" v-model="password" type="password" />
    </div>
    <button class="btn" @click="onLogin">로그인</button>
  </div>
</template>
