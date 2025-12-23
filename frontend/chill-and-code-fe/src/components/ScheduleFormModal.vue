<script setup>
import { ref, computed, watch } from 'vue'
import { useScheduleStore } from '@/stores/schedule'

const props = defineProps({
  schedule: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['close', 'saved'])

const scheduleStore = useScheduleStore()

// 폼 상태
const title = ref('')
const scheduleType = ref('PERSONAL')
const startDate = ref('')
const startTime = ref('09:00')
const endDate = ref('')
const endTime = ref('10:00')
const loading = ref(false)
const error = ref(null)

// 수정 모드인지 확인
const isEdit = computed(() => !!props.schedule)

// 모달 제목
const modalTitle = computed(() => isEdit.value ? '일정 수정' : '일정 추가')

// 일정 유형 옵션
const scheduleTypeOptions = [
  { value: 'PERSONAL', label: '개인 일정', color: '#93c5fd' },
  { value: 'WORK', label: '업무 일정', color: '#64748b' },
  { value: 'WORKATION', label: '워케이션 일정', color: '#0ea5e9' }
]

// 초기값 설정
const initForm = () => {
  if (props.schedule) {
    // 수정 모드: 기존 일정 정보로 채우기
    title.value = props.schedule.title || ''
    scheduleType.value = props.schedule.scheduleType || 'PERSONAL'
    
    // startDateTime 파싱
    if (props.schedule.startDateTime) {
      const startDT = new Date(props.schedule.startDateTime)
      startDate.value = startDT.toISOString().split('T')[0]
      startTime.value = startDT.toTimeString().slice(0, 5)
    }
    
    // endDateTime 파싱
    if (props.schedule.endDateTime) {
      const endDT = new Date(props.schedule.endDateTime)
      endDate.value = endDT.toISOString().split('T')[0]
      endTime.value = endDT.toTimeString().slice(0, 5)
    }
  } else if (scheduleStore.prefilledPlace) {
    // 장소 추천에서 넘어온 경우: 장소 정보로 자동 채우기
    const place = scheduleStore.prefilledPlace
    const period = scheduleStore.prefilledPeriod
    
    // 제목: "[장소명] - 워케이션" 형식
    title.value = `${place.name} - 워케이션`
    
    // 일정 유형: 워케이션으로 자동 설정
    scheduleType.value = 'WORKATION'
    
    // 날짜: 추천받은 기간으로 설정
    if (period && period.startDate && period.endDate) {
      startDate.value = period.startDate
      endDate.value = period.endDate
      startTime.value = '09:00'
      endTime.value = '18:00'
    } else {
      // 기간 정보가 없으면 오늘로 설정
      const today = new Date().toISOString().split('T')[0]
      startDate.value = today
      endDate.value = today
    }
    
    // 사용 후 초기화 (다음 일정 생성 시 중복 방지)
    scheduleStore.clearPrefilledPlace()
  } else {
    // 기본값: 오늘 날짜
    const today = new Date().toISOString().split('T')[0]
    startDate.value = today
    endDate.value = today
  }
}

initForm()

// 백엔드 API 형식으로 변환
const buildPayload = () => {
  return {
    title: title.value,
    scheduleType: scheduleType.value,
    startDateTime: `${startDate.value}T${startTime.value}:00`,
    endDateTime: `${endDate.value}T${endTime.value}:00`
  }
}

// 유효성 검증
const validate = () => {
  if (!title.value.trim()) {
    error.value = '일정 제목을 입력해주세요.'
    return false
  }
  
  if (!startDate.value || !startTime.value) {
    error.value = '시작 날짜와 시간을 입력해주세요.'
    return false
  }
  
  if (!endDate.value || !endTime.value) {
    error.value = '종료 날짜와 시간을 입력해주세요.'
    return false
  }
  
  const start = new Date(`${startDate.value}T${startTime.value}`)
  const end = new Date(`${endDate.value}T${endTime.value}`)
  
  if (start >= end) {
    error.value = '종료 시간은 시작 시간보다 늦어야 합니다.'
    return false
  }
  
  return true
}

// 저장
const handleSave = async () => {
  error.value = null
  
  if (!validate()) {
    return
  }
  
  loading.value = true
  
  try {
    const payload = buildPayload()
    
    if (isEdit.value) {
      await scheduleStore.updateSchedule(props.schedule.scheduleId, payload)
    } else {
      await scheduleStore.createSchedule(payload)
    }
    
    emit('saved')
  } catch (e) {
    error.value = e.response?.data?.message || '일정 저장에 실패했습니다.'
    console.error(e)
  } finally {
    loading.value = false
  }
}

// 모달 닫기
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
      class="bg-white rounded-2xl shadow-2xl w-full max-w-2xl max-h-[90vh] overflow-y-auto"
      @click.stop
    >
      <!-- 모달 헤더 -->
      <div class="bg-gradient-to-r from-slate-50 to-blue-50 px-8 py-6 border-b border-gray-200 flex items-center justify-between">
        <h2 class="text-2xl font-bold text-slate-800">{{ modalTitle }}</h2>
        <button
          @click="handleClose"
          class="text-gray-400 hover:text-gray-600 transition-colors"
          :disabled="loading"
        >
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>

      <!-- 모달 바디 -->
      <div class="p-8">
        <!-- 에러 메시지 -->
        <div v-if="error" class="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">
          {{ error }}
        </div>

        <!-- 폼 -->
        <form @submit.prevent="handleSave" class="space-y-6">
          <!-- 일정 제목 -->
          <div>
            <label class="block text-sm font-bold text-gray-900 mb-2">
              일정 제목 <span class="text-red-500">*</span>
            </label>
            <input
              v-model="title"
              type="text"
              placeholder="일정 제목을 입력하세요"
              :disabled="loading"
              class="w-full px-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-slate-500 focus:border-transparent disabled:bg-gray-50 disabled:cursor-not-allowed transition-all text-gray-900"
              maxlength="100"
            />
          </div>

          <!-- 일정 유형 -->
          <div>
            <label class="block text-sm font-bold text-gray-900 mb-3">
              일정 유형 <span class="text-red-500">*</span>
            </label>
            <div class="grid grid-cols-3 gap-3">
              <label
                v-for="option in scheduleTypeOptions"
                :key="option.value"
                class="relative flex items-center justify-center p-4 border-2 rounded-xl cursor-pointer transition-all hover:border-slate-400"
                :class="scheduleType === option.value ? 'border-slate-700 bg-slate-50' : 'border-gray-200'"
              >
                <input
                  type="radio"
                  :value="option.value"
                  v-model="scheduleType"
                  :disabled="loading"
                  class="sr-only"
                />
                <div class="flex items-center gap-2">
                  <div class="w-3 h-3 rounded-full" :style="{ backgroundColor: option.color }"></div>
                  <span class="text-sm font-semibold text-gray-800">{{ option.label }}</span>
                </div>
                <div
                  v-if="scheduleType === option.value"
                  class="absolute top-2 right-2"
                >
                  <svg class="w-5 h-5 text-slate-700" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
                  </svg>
                </div>
              </label>
            </div>
          </div>

          <!-- 시작 날짜 & 시간 -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-bold text-gray-900 mb-2">
                시작 날짜 <span class="text-red-500">*</span>
              </label>
              <input
                v-model="startDate"
                type="date"
                :disabled="loading"
                class="w-full px-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-slate-500 focus:border-transparent disabled:bg-gray-50 disabled:cursor-not-allowed transition-all text-gray-900"
              />
            </div>
            <div>
              <label class="block text-sm font-bold text-gray-900 mb-2">
                시작 시간 <span class="text-red-500">*</span>
              </label>
              <input
                v-model="startTime"
                type="time"
                :disabled="loading"
                class="w-full px-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-slate-500 focus:border-transparent disabled:bg-gray-50 disabled:cursor-not-allowed transition-all text-gray-900"
              />
            </div>
          </div>

          <!-- 종료 날짜 & 시간 -->
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-bold text-gray-900 mb-2">
                종료 날짜 <span class="text-red-500">*</span>
              </label>
              <input
                v-model="endDate"
                type="date"
                :disabled="loading"
                class="w-full px-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-slate-500 focus:border-transparent disabled:bg-gray-50 disabled:cursor-not-allowed transition-all text-gray-900"
              />
            </div>
            <div>
              <label class="block text-sm font-bold text-gray-900 mb-2">
                종료 시간 <span class="text-red-500">*</span>
              </label>
              <input
                v-model="endTime"
                type="time"
                :disabled="loading"
                class="w-full px-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-slate-500 focus:border-transparent disabled:bg-gray-50 disabled:cursor-not-allowed transition-all text-gray-900"
              />
            </div>
          </div>

          <!-- 안내 문구 -->
          <div class="bg-blue-50 border border-blue-200 rounded-lg p-4">
            <div class="flex items-start gap-3">
              <svg class="w-5 h-5 text-blue-600 mt-0.5 flex-shrink-0" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clip-rule="evenodd" />
              </svg>
              <div class="text-sm text-blue-800">
                <p class="font-semibold mb-1">참고사항</p>
                <ul class="list-disc list-inside space-y-1 text-blue-700">
                  <li>장소, 메모 등 추가 필드는 향후 백엔드 API 확장 시 지원됩니다.</li>
                  <li>연속된 일정은 캘린더에서 시각적으로 연결되어 표시됩니다.</li>
                </ul>
              </div>
            </div>
          </div>

          <!-- 버튼 영역 -->
          <div class="flex items-center gap-3 pt-4">
            <button
              type="button"
              @click="handleClose"
              :disabled="loading"
              class="flex-1 px-6 py-3 border-2 border-gray-300 text-gray-700 font-semibold rounded-xl hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed transition-all"
            >
              취소
            </button>
            <button
              type="submit"
              :disabled="loading"
              class="flex-1 px-6 py-3 bg-slate-800 text-white font-semibold rounded-xl hover:bg-slate-900 hover:shadow-lg disabled:bg-slate-400 disabled:cursor-not-allowed transition-all"
            >
              <span v-if="loading" class="flex items-center justify-center gap-2">
                <svg class="animate-spin h-5 w-5" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                저장 중...
              </span>
              <span v-else>{{ isEdit ? '수정' : '저장' }}</span>
            </button>
          </div>
        </form>
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

