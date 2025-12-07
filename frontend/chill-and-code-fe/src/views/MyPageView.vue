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
  <div style="max-width: 640px; margin: 24px auto;">
    <h2>마이페이지</h2>
    <div style="margin: 8px 0;">
      <label>이메일 (읽기전용)</label>
      <input v-model="form.email" disabled style="display:block; width:100%; padding:6px;" />
    </div>
    <div style="margin: 8px 0;">
      <label>닉네임</label>
      <input v-model="form.nickname" style="display:block; width:100%; padding:6px;" />
    </div>
    <div style="margin: 8px 0;">
      <label>지역</label>
      <input v-model="form.region" style="display:block; width:100%; padding:6px;" />
    </div>
    <button @click="onUpdate" style="padding:6px 12px;">수정</button>
  </div>
</template>

