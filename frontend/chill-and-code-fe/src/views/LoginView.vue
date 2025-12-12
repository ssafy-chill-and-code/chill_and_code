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
  <div class="row justify-content-center">
    <div class="col-12 col-md-5 col-lg-4">
      <div class="card shadow rounded-4 mt-5">
        <div class="card-body p-4">
          <div class="d-flex align-items-center gap-2 mb-3">
            <i class="bi bi-person-fill"></i>
            <h2 class="h4 m-0">로그인</h2>
          </div>
          <div class="mb-3">
            <label class="form-label">이메일</label>
            <input class="form-control" v-model="email" type="email" placeholder="you@example.com" />
          </div>
          <div class="mb-3">
            <label class="form-label">비밀번호</label>
            <input class="form-control" v-model="password" type="password" placeholder="••••••••" />
          </div>
          <button class="btn btn-primary w-100" @click="onLogin">로그인</button>
          <div class="text-center mt-3">
            <RouterLink class="small text-decoration-none" to="/signup">아직 계정이 없으신가요? 회원가입</RouterLink>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
