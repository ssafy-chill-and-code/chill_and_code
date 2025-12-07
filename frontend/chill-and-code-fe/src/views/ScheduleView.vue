<script setup>
import { onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useScheduleStore } from '../stores/schedule'

const router = useRouter()
const scheduleStore = useScheduleStore()

const fmtMonth = (date) => {
  const y = date.getFullYear()
  const m = String(date.getMonth() + 1).padStart(2, '0')
  return `${y}-${m}`
}

onMounted(async () => {
  const nowMonth = fmtMonth(new Date())
  try {
    await scheduleStore.fetchSchedules(nowMonth)
  } catch (e) {
    // 인터셉터 처리
  }
})

const schedules = computed(() => scheduleStore.schedules)

const goCreate = () => router.push('/schedule/create')
const goDetail = (id) => router.push(`/schedule/${id}`)

const onlyDate = (dateTime) => dateTime?.substring(0, 10)
</script>

<template>
  <div style="max-width: 720px; margin: 24px auto;">
    <h2>월별 일정</h2>
    <button @click="goCreate" style="margin: 8px 0; padding:6px 12px;">일정 추가</button>
    <ul>
      <li v-for="s in schedules" :key="s.scheduleId" style="margin:6px 0;">
        <a href="javascript:void(0)" @click="goDetail(s.scheduleId)">
          {{ s.title }} ({{ onlyDate(s.startDateTime) }} ~ {{ onlyDate(s.endDateTime) }})
        </a>
      </li>
    </ul>
  </div>
</template>
