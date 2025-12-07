<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useScheduleStore } from '../stores/schedule'

const route = useRoute()
const router = useRouter()
const scheduleStore = useScheduleStore()

const detail = computed(() => {
  const id = route.params.id
  return scheduleStore.schedules.find((s) => String(s.scheduleId) === String(id))
})

const onlyDate = (v) => (typeof v === 'string' ? v.substring(0, 10) : '')

const edit = () => router.push(`/schedule/${route.params.id}/edit`)
const remove = async () => {
  try {
    await scheduleStore.deleteSchedule(route.params.id)
    alert('삭제되었습니다.')
    router.push('/schedule')
  } catch (e) {
    alert('삭제 실패')
  }
}
</script>

<template>
  <div>
    <h2 class="page-title">일정 상세</h2>
    <div v-if="detail">
      <p><b>제목:</b> {{ detail.title }}</p>
      <p><b>타입:</b> {{ detail.scheduleType }}</p>
      <p><b>시작일:</b> {{ onlyDate(detail.startDateTime) }}</p>
      <p><b>종료일:</b> {{ onlyDate(detail.endDateTime) }}</p>
    </div>
    <div>
      <button class="btn" @click="edit">수정하기</button>
      <button class="btn" @click="remove">삭제하기</button>
    </div>
  </div>
</template>
