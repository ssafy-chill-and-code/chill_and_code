<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const email = ref('')
const password = ref('')
const nickname = ref('')
const region = ref('')
const userStore = useUserStore()
const router = useRouter()

const onSignup = async () => {
  try {
    await userStore.signup({ email: email.value, password: password.value, nickname: nickname.value, region: region.value })
    alert('회원가입 완료. 로그인해주세요.')
    router.push('/login')
  } catch (e) {
    alert('회원가입 실패')
  }
}
</script>

<template>
  <div style="max-width: 480px; margin: 24px auto;">
    <h2>회원가입</h2>
    <div style="margin: 8px 0;">
      <label>이메일</label>
      <input v-model="email" type="email" style="display:block; width:100%; padding:6px;" />
    </div>
    <div style="margin: 8px 0;">
      <label>비밀번호</label>
      <input v-model="password" type="password" style="display:block; width:100%; padding:6px;" />
    </div>
    <div style="margin: 8px 0;">
      <label>닉네임</label>
      <input v-model="nickname" style="display:block; width:100%; padding:6px;" />
    </div>
    <div style="margin: 8px 0;">
      <label>지역</label>
      <input v-model="region" style="display:block; width:100%; padding:6px;" />
    </div>
    <button @click="onSignup" style="padding:6px 12px;">회원가입</button>
  </div>
</template>

