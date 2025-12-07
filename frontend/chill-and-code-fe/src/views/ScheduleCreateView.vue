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
  <div>
    <h2 class="page-title">일정 생성</h2>
    <div class="form-group">
      <label class="form-label">제목</label>
      <input class="form-input" v-model="form.title" />
    </div>
    <div class="form-group">
      <label class="form-label">시작일</label>
      <input class="form-input" v-model="form.startDateTime" type="date" />
    </div>
    <div class="form-group">
      <label class="form-label">종료일</label>
      <input class="form-input" v-model="form.endDateTime" type="date" />
    </div>
    <div class="form-group">
      <label class="form-label">타입 (PERSONAL, WORK, WORKATION)</label>
      <input class="form-input" v-model="form.scheduleType" />
    </div>
    <button class="btn" @click="onCreate">생성</button>
  </div>
</template>
