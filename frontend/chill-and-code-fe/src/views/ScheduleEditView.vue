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
  <div class="row justify-content-center">
    <div class="col-12 col-lg-8">
      <div class="card shadow-sm rounded-4">
        <div class="card-body p-4">
          <div class="d-flex align-items-center gap-2 mb-3">
            <i class="bi bi-pencil-square"></i>
            <h2 class="h5 m-0">일정 수정</h2>
          </div>
          <div class="mb-3">
            <label class="form-label">제목</label>
            <input class="form-control" v-model="form.title" />
          </div>
          <div class="row g-3">
            <div class="col-md-6">
              <label class="form-label">시작일</label>
              <input class="form-control" v-model="form.startDateTime" type="date" />
            </div>
            <div class="col-md-6">
              <label class="form-label">종료일</label>
              <input class="form-control" v-model="form.endDateTime" type="date" />
            </div>
          </div>
          <div class="mt-3">
            <label class="form-label">타입</label>
            <input class="form-control" v-model="form.scheduleType" />
          </div>
          <div class="d-flex justify-content-end gap-2 mt-4">
            <button class="btn btn-outline-secondary" type="button" @click="router.back()"><i class="bi bi-arrow-left"></i> 취소</button>
            <button class="btn btn-primary" @click="onUpdate"><i class="bi bi-check-lg"></i> 수정</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
