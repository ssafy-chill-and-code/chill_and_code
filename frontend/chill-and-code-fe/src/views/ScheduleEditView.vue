<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useScheduleStore } from '../stores/schedule'

const route = useRoute()
const router = useRouter()
const scheduleStore = useScheduleStore()
const form = ref({ title: '', startDateTime: '', endDateTime: '', type: '' })

onMounted(() => {
  const id = route.params.id
  const data = scheduleStore.schedules.find((s) => String(s.scheduleId) === String(id)) || {}
  const onlyDate = (v) => (typeof v === 'string' ? v.substring(0, 10) : '')
  form.value = {
    title: data.title || '',
    startDateTime: onlyDate(data.startDateTime),
    endDateTime: onlyDate(data.endDateTime),
    scheduleType: data.scheduleType || '',
  }
})

const onUpdate = async () => {
  try {
    await scheduleStore.updateSchedule(route.params.id, form.value)
    alert('수정되었습니다.')
    router.push('/schedule')
  } catch (e) {
    alert('수정 실패')
  }
}
</script>

<template>
  <div>
    <h2 class="page-title">일정 수정</h2>
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
      <label class="form-label">타입</label>
      <input class="form-input" v-model="form.scheduleType" />
    </div>
    <button class="btn" @click="onUpdate">수정</button>
  </div>
</template>
