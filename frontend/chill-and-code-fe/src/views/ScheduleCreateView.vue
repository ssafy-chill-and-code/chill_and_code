<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useScheduleStore } from '../stores/schedule'

const form = ref({ title: '', scheduleType: '', startDateTime: '', endDateTime: '' })
const scheduleStore = useScheduleStore()
const router = useRouter()

const onCreate = async () => {
  try {
    await scheduleStore.createSchedule(form.value)
    alert('일정이 생성되었습니다.')
    router.push('/schedule')
  } catch (e) {
    alert('생성 실패')
  }
}
</script>

<template>
  <div style="max-width: 640px; margin: 24px auto;">
    <h2>일정 생성</h2>
    <div style="margin: 8px 0;">
      <label>제목</label>
      <input v-model="form.title" style="display:block; width:100%; padding:6px;" />
    </div>
    <div style="margin: 8px 0;">
      <label>시작일</label>
      <input v-model="form.startDateTime" type="date" style="display:block; padding:6px;" />
    </div>
    <div style="margin: 8px 0;">
      <label>종료일</label>
      <input v-model="form.endDateTime" type="date" style="display:block; padding:6px;" />
    </div>
    <div style="margin: 8px 0;">
      <label>타입 (PERSONAL, WORK, WORKATION)</label>
      <input v-model="form.scheduleType" style="display:block; width:100%; padding:6px;" />
    </div>
    <button @click="onCreate" style="padding:6px 12px;">생성</button>
  </div>
</template>

