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
  <div class="row justify-content-center">
    <div class="col-12 col-md-5 col-lg-4">
      <div class="card shadow rounded-4 mt-5">
        <div class="card-body p-4">
          <div class="d-flex align-items-center gap-2 mb-3">
            <i class="bi bi-person-plus-fill"></i>
            <h2 class="h4 m-0">회원가입</h2>
          </div>
          <div class="mb-3">
            <label class="form-label">이메일</label>
            <input class="form-control" v-model="email" type="email" placeholder="you@example.com" />
          </div>
          <div class="mb-3">
            <label class="form-label">비밀번호</label>
            <input class="form-control" v-model="password" type="password" placeholder="••••••••" />
          </div>
          <div class="mb-3">
            <label class="form-label">닉네임</label>
            <input class="form-control" v-model="nickname" placeholder="별명" />
          </div>
          <div class="mb-3">
            <label class="form-label">지역</label>
            <input class="form-control" v-model="region" placeholder="예: 서울" />
          </div>
          <button class="btn btn-primary w-100" @click="onSignup">회원가입</button>
          <div class="text-center mt-3">
            <RouterLink class="small text-decoration-none" to="/login">이미 계정이 있으신가요? 로그인</RouterLink>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
