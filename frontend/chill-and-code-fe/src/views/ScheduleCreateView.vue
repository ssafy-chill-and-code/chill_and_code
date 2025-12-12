<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useScheduleStore } from '../stores/schedule'

const form = ref({
  title: '',
  scheduleType: '',
  startDate: '',
  startTime: '',
  endDate: '',
  endTime: '',
})
const scheduleStore = useScheduleStore()
const router = useRouter()

const toDateTime = (date, time) => {
  const t = time && time.length >= 4 ? time : '00:00'
  return `${date}T${t}:00`
}

const onCreate = async () => {
  try {
    const payload = {
      title: form.value.title,
      scheduleType: form.value.scheduleType,
      startDateTime: toDateTime(form.value.startDate, form.value.startTime),
      endDateTime: toDateTime(form.value.endDate, form.value.endTime),
    }
    await scheduleStore.createSchedule(payload)
    alert('일정이 생성되었습니다.')
    router.push('/schedule')
  } catch (e) {
    alert('생성 실패')
  }
}
</script>

<template>
  <div class="row justify-content-center">
    <div class="col-12 col-lg-8">
      <div class="card shadow-sm rounded-4">
        <div class="card-body p-4">
          <div class="d-flex align-items-center gap-2 mb-3">
            <i class="bi bi-calendar-plus"></i>
            <h2 class="h5 m-0">일정 생성</h2>
          </div>
          <div class="mb-3">
            <label class="form-label">제목</label>
            <input class="form-control" v-model="form.title" />
          </div>
          <div class="row g-3">
            <div class="col-md-6">
              <label class="form-label">시작일</label>
              <input class="form-control" v-model="form.startDate" type="date" />
            </div>
            <div class="col-md-6">
              <label class="form-label">시작 시간</label>
              <input class="form-control" v-model="form.startTime" type="time" />
            </div>
          </div>
          <div class="row g-3 mt-1">
            <div class="col-md-6">
              <label class="form-label">종료일</label>
              <input class="form-control" v-model="form.endDate" type="date" />
            </div>
            <div class="col-md-6">
              <label class="form-label">종료 시간</label>
              <input class="form-control" v-model="form.endTime" type="time" />
            </div>
          </div>
          <div class="mt-3">
            <label class="form-label">타입 (PERSONAL, WORK, WORKATION)</label>
            <input class="form-control" v-model="form.scheduleType" />
          </div>
          <div class="d-flex justify-content-end gap-2 mt-4">
            <button class="btn btn-outline-secondary" type="button" @click="router.back()"><i class="bi bi-arrow-left"></i> 취소</button>
            <button class="btn btn-primary" @click="onCreate"><i class="bi bi-check-lg"></i> 생성</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
