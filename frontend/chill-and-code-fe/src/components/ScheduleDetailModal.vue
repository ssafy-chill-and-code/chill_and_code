<script setup>
import { ref, computed } from 'vue'
import { useScheduleStore } from '@/stores/schedule'

const props = defineProps({
  schedule: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['close', 'edit', 'deleted'])

const scheduleStore = useScheduleStore()

const deleting = ref(false)
const error = ref(null)

// 일정 유형 정보
const scheduleTypeInfo = computed(() => {
  const types = {
    PERSONAL: { label: '개인 일정', color: '#93c5fd', bgColor: '#dbeafe' },
    WORK: { label: '업무 일정', color: '#64748b', bgColor: '#e2e8f0' },
    WORKATION: { label: '워케이션 일정', color: '#0ea5e9', bgColor: '#bae6fd' }
  }
  return types[props.schedule.scheduleType] || types.PERSONAL
})

// 날짜/시간 포맷
const formatDateTime = (dateTimeStr) => {
  if (!dateTimeStr) return '-'
  const dt = new Date(dateTimeStr)
  const date = dt.toLocaleDateString('ko-KR', { year: 'numeric', month: 'long', day: 'numeric', weekday: 'short' })
  const time = dt.toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' })
  return `${date} ${time}`
}

const formatDate = (dateTimeStr) => {
  if (!dateTimeStr) return '-'
  const dt = new Date(dateTimeStr)
  return dt.toLocaleDateString('ko-KR', { year: 'numeric', month: 'long', day: 'numeric', weekday: 'short' })
}

const formatTime = (dateTimeStr) => {
  if (!dateTimeStr) return '-'
  const dt = new Date(dateTimeStr)
  return dt.toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' })
}

// 일정 기간 계산
const duration = computed(() => {
  if (!props.schedule.startDateTime || !props.schedule.endDateTime) return null
  const start = new Date(props.schedule.startDateTime)
  const end = new Date(props.schedule.endDateTime)
  const diffMs = end - start
  const diffHours = Math.floor(diffMs / (1000 * 60 * 60))
  const diffMins = Math.floor((diffMs % (1000 * 60 * 60)) / (1000 * 60))
  
  if (diffHours > 24) {
    const days = Math.floor(diffHours / 24)
    const hours = diffHours % 24
    return `${days}일 ${hours}시간`
  } else if (diffHours > 0) {
    return `${diffHours}시간 ${diffMins}분`
  } else {
    return `${diffMins}분`
  }
})

// 수정 버튼
const handleEdit = () => {
  emit('edit', props.schedule)
}

// 삭제 버튼
const handleDelete = async () => {
  if (!confirm('정말 이 일정을 삭제하시겠습니까?')) {
    return
  }
  
  deleting.value = true
  error.value = null
  
  try {
    await scheduleStore.deleteSchedule(props.schedule.scheduleId)
    emit('deleted', props.schedule.scheduleId)
  } catch (e) {
    error.value = e.response?.data?.message || '일정 삭제에 실패했습니다.'
    console.error(e)
  } finally {
    deleting.value = false
  }
}

// 닫기
const handleClose = () => {
  emit('close')
}

// ESC 키로 닫기
const handleKeydown = (e) => {
  if (e.key === 'Escape') {
    handleClose()
  }
}

// 오버레이 클릭으로 닫기
const handleOverlayClick = (e) => {
  if (e.target === e.currentTarget) {
    handleClose()
  }
}
</script>

<template>
  <div 
    class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50 p-4"
    @click="handleOverlayClick"
    @keydown="handleKeydown"
    tabindex="0"
  >
    <div 
      class="bg-white rounded-xl shadow-2xl w-full max-w-lg max-h-[90vh] overflow-y-auto"
      @click.stop
    >
      <!-- 모달 헤더 -->
      <div class="bg-white px-6 py-5 border-b border-gray-200 flex items-center justify-between">
        <div class="flex items-center gap-2">
          <div 
            class="w-2.5 h-2.5 rounded-full"
            :style="{ backgroundColor: scheduleTypeInfo.color }"
          ></div>
          <h2 class="text-lg font-bold text-gray-900">일정 상세</h2>
        </div>
        <button
          @click="handleClose"
          class="text-gray-400 hover:text-gray-600 transition-colors p-1"
          :disabled="deleting"
        >
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>

      <!-- 모달 바디 -->
      <div class="p-6">
        <!-- 에러 메시지 -->
        <div v-if="error" class="mb-6 p-3 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">
          {{ error }}
        </div>

        <!-- 일정 제목 -->
        <div class="mb-6">
          <h3 class="text-xl font-bold text-gray-900 mb-2">{{ schedule.title }}</h3>
          <div 
            class="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold"
            :style="{ backgroundColor: scheduleTypeInfo.bgColor, color: scheduleTypeInfo.color }"
          >
            <div class="w-1.5 h-1.5 rounded-full" :style="{ backgroundColor: scheduleTypeInfo.color }"></div>
            {{ scheduleTypeInfo.label }}
          </div>
        </div>

        <!-- 일정 정보 -->
        <div class="space-y-4">
          <!-- 시작 일시 -->
          <div class="flex items-start gap-3">
            <div class="flex-shrink-0 w-9 h-9 bg-indigo-50 rounded-lg flex items-center justify-center">
              <svg class="w-4 h-4 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div class="flex-1">
              <div class="text-xs font-semibold text-gray-500 mb-1">시작</div>
              <div class="text-sm font-semibold text-gray-900">{{ formatDateTime(schedule.startDateTime) }}</div>
            </div>
          </div>

          <!-- 종료 일시 -->
          <div class="flex items-start gap-3">
            <div class="flex-shrink-0 w-9 h-9 bg-slate-50 rounded-lg flex items-center justify-center">
              <svg class="w-4 h-4 text-slate-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div class="flex-1">
              <div class="text-xs font-semibold text-gray-500 mb-1">종료</div>
              <div class="text-sm font-semibold text-gray-900">{{ formatDateTime(schedule.endDateTime) }}</div>
            </div>
          </div>

          <!-- 일정 기간 -->
          <div v-if="duration" class="flex items-start gap-3">
            <div class="flex-shrink-0 w-9 h-9 bg-gray-50 rounded-lg flex items-center justify-center">
              <svg class="w-4 h-4 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
              </svg>
            </div>
            <div class="flex-1">
              <div class="text-xs font-semibold text-gray-500 mb-1">소요 시간</div>
              <div class="text-sm font-semibold text-gray-900">{{ duration }}</div>
            </div>
          </div>
        </div>

        <!-- 안내 문구 -->
        <div class="mt-6 bg-gray-50 border border-gray-200 rounded-lg p-3">
          <div class="flex items-start gap-2">
            <svg class="w-4 h-4 text-gray-500 mt-0.5 flex-shrink-0" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clip-rule="evenodd" />
            </svg>
            <div class="text-xs text-gray-600">
              장소, 메모 등의 추가 정보는 백엔드 API 확장 후 지원됩니다.
            </div>
          </div>
        </div>

        <!-- 버튼 영역 -->
        <div class="flex items-center gap-2 mt-6 pt-5 border-t border-gray-200">
          <button
            @click="handleDelete"
            :disabled="deleting"
            class="px-4 py-2 border border-gray-300 text-gray-700 font-semibold rounded-lg text-sm hover:bg-gray-50 hover:border-gray-400 disabled:opacity-50 disabled:cursor-not-allowed transition-all"
          >
            <span v-if="deleting" class="flex items-center gap-2">
              <svg class="animate-spin h-4 w-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              삭제 중...
            </span>
            <span v-else>삭제</span>
          </button>
          <button
            @click="handleEdit"
            :disabled="deleting"
            class="flex-1 px-4 py-2 bg-slate-800 text-white font-semibold rounded-lg text-sm hover:bg-slate-900 disabled:bg-slate-400 disabled:cursor-not-allowed transition-all"
          >
            수정
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 모달 애니메이션 */
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideUp {
  from {
    transform: translateY(20px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.fixed {
  animation: fadeIn 0.2s ease-out;
}

.bg-white {
  animation: slideUp 0.3s ease-out;
}
</style>

