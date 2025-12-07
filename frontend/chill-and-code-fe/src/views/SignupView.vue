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
  <div>
    <h2 class="page-title">회원가입</h2>
    <div class="form-group">
      <label class="form-label">이메일</label>
      <input class="form-input" v-model="email" type="email" />
    </div>
    <div class="form-group">
      <label class="form-label">비밀번호</label>
      <input class="form-input" v-model="password" type="password" />
    </div>
    <div class="form-group">
      <label class="form-label">닉네임</label>
      <input class="form-input" v-model="nickname" />
    </div>
    <div class="form-group">
      <label class="form-label">지역</label>
      <input class="form-input" v-model="region" />
    </div>
    <button class="btn" @click="onSignup">회원가입</button>
  </div>
</template>
