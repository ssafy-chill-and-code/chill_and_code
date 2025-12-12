<script setup>
import { onMounted, computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useScheduleStore } from '../stores/schedule'

const router = useRouter()
const scheduleStore = useScheduleStore()

const fmtMonth = (date) => {
  const y = date.getFullYear()
  const m = String(date.getMonth() + 1).padStart(2, '0')
  return `${y}-${m}`
}

const month = ref(fmtMonth(new Date()))

onMounted(async () => {
  try {
    await scheduleStore.fetchSchedules(month.value)
  } catch (e) {
    // 인터셉터 처리
  }
})

const schedules = computed(() => scheduleStore.schedules)

const goCreate = () => router.push('/schedule/create')
const goDetail = (id) => router.push(`/schedule/${id}`)

const onlyDate = (dateTime) => dateTime?.substring(0, 10)

const onChangeMonth = async (e) => {
  const v = typeof e === 'string' ? e : e?.target?.value
  if (!v) return
  month.value = v
  try {
    await scheduleStore.fetchSchedules(month.value)
  } catch (e) {
    // 인터셉터 처리
  }
}
</script>

<template>
  <div>
    <div class="d-flex align-items-center justify-content-between mb-3">
      <div class="d-flex align-items-center gap-3">
        <h2 class="h4 m-0">월별 일정</h2>
        <input
          type="month"
          class="form-control"
          style="max-width: 200px"
          :value="month"
          @change="onChangeMonth"
        />
      </div>
      <button class="btn btn-primary" @click="goCreate">
        <i class="bi bi-plus-lg me-1"></i> 일정 추가
      </button>
    </div>
    <ul class="list-group">
      <li
        class="list-group-item rounded-3 shadow-sm p-3 mb-2 hoverable d-flex justify-content-between align-items-center"
        v-for="s in schedules"
        :key="s.scheduleId"
        @click="goDetail(s.scheduleId)"
        role="button"
      >
        <div class="d-flex align-items-center gap-2">
          <i class="bi bi-calendar-event text-primary"></i>
          <div>
            <div class="fw-semibold">{{ s.title }}</div>
            <div class="text-muted small">{{ onlyDate(s.startDateTime) }} ~ {{ onlyDate(s.endDateTime) }}</div>
          </div>
        </div>
        <span class="badge text-bg-secondary small-badge rounded-pill">{{ s.scheduleType }}</span>
      </li>
    </ul>
    <div v-if="!schedules || schedules.length === 0" class="text-center text-muted py-4">
      표시할 일정이 없습니다.
    </div>
  </div>
  
</template>
