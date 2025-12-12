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
    <div class="card shadow-sm rounded-4">
      <div class="card-body p-4">
        <div class="d-flex align-items-center gap-2 mb-3">
          <i class="bi bi-calendar2-week"></i>
          <h2 class="h4 m-0">일정 상세</h2>
        </div>
        <div v-if="detail">
          <h3 class="h5 fw-semibold mb-2">{{ detail.title }}</h3>
          <div class="mb-2">
            <span class="fw-semibold me-2">타입:</span>
            <span class="badge text-bg-secondary">{{ detail.scheduleType }}</span>
          </div>
          <div class="mb-2"><span class="fw-semibold">시작일:</span> {{ onlyDate(detail.startDateTime) }}</div>
          <div class="mb-2"><span class="fw-semibold">종료일:</span> {{ onlyDate(detail.endDateTime) }}</div>
        </div>
        <div class="d-flex justify-content-end gap-2 mt-4">
          <button class="btn btn-secondary" @click="edit"><i class="bi bi-pencil-square me-1"></i>수정하기</button>
          <button class="btn btn-danger" @click="remove"><i class="bi bi-trash3 me-1"></i>삭제하기</button>
        </div>
      </div>
    </div>
  </div>
</template>
