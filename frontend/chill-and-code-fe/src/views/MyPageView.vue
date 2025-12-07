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
  <div>
    <h2 class="page-title">마이페이지</h2>
    <div class="form-group">
      <label class="form-label">이메일 (읽기전용)</label>
      <input class="form-input" v-model="form.email" disabled />
    </div>
    <div class="form-group">
      <label class="form-label">닉네임</label>
      <input class="form-input" v-model="form.nickname" />
    </div>
    <div class="form-group">
      <label class="form-label">지역</label>
      <input class="form-input" v-model="form.region" />
    </div>
    <button class="btn" @click="onUpdate">수정</button>
  </div>
</template>
