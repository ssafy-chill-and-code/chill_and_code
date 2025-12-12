<script setup>
import { onMounted, ref } from 'vue'
import { useUserStore } from '../stores/user'

const userStore = useUserStore()
const form = ref({ email: '', nickname: '', region: '' })

onMounted(async () => {
  try {
    await userStore.fetchProfile()
    const u = userStore.user || {}
    form.value.email = u.email || ''
    form.value.nickname = u.nickname || ''
    form.value.region = u.region || ''
  } catch (e) {
    // 401 등은 인터셉터 처리
  }
})

const onUpdate = async () => {
  try {
    const payload = { nickname: form.value.nickname, region: form.value.region }
    await userStore.updateProfile(payload)
    alert('프로필이 수정되었습니다.')
  } catch (e) {
    alert('수정 실패')
  }
}
</script>

<template>
  <div class="row justify-content-center">
    <div class="col-12 col-lg-8 d-flex flex-column gap-4">
      <div class="card shadow-sm rounded-4">
        <div class="card-body p-4 d-flex align-items-center gap-3">
          <i class="bi bi-person-circle fs-2"></i>
          <div>
            <div class="fw-semibold">{{ form.nickname || '사용자' }}</div>
            <div class="text-muted">{{ form.email }}</div>
            <div class="text-muted">{{ form.region || '지역 정보 없음' }}</div>
          </div>
        </div>
      </div>

      <div class="card shadow-sm rounded-4">
        <div class="card-body p-4">
          <h2 class="h5 mb-3">프로필 수정</h2>
          <div class="mb-3">
            <label class="form-label">이메일 (읽기전용)</label>
            <input class="form-control" v-model="form.email" disabled />
          </div>
          <div class="mb-3">
            <label class="form-label">닉네임</label>
            <input class="form-control" v-model="form.nickname" />
          </div>
          <div class="mb-3">
            <label class="form-label">지역</label>
            <input class="form-control" v-model="form.region" />
          </div>
          <div class="d-flex justify-content-end">
            <button class="btn btn-primary" @click="onUpdate">저장</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
