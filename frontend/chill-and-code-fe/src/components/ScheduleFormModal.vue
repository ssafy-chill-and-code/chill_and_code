<script setup>
import { ref, computed, watch } from 'vue'
import { useScheduleStore } from '@/stores/schedule'

const props = defineProps({
  schedule: {
    type: Object,
    default: null
  },
  initialDate: {
    type: String,
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
const userTag = ref(null)
const loading = ref(false)
const error = ref(null)

// 수정 모드인지 확인
const isEdit = computed(() => !!props.schedule)

// 모달 제목
const modalTitle = computed(() => isEdit.value ? '일정 수정' : '일정 추가')

// 일정 유형 옵션
const scheduleTypeOptions = [
  { value: 'PERSONAL', label: '개인 일정', color: '#fde68a', bgColor: '#fffbeb' },
  { value: 'WORK', label: '업무 일정', color: '#fca5a5', bgColor: '#fef2f2' },
  { value: 'WORKATION', label: '워케이션 일정', color: '#93c5fd', bgColor: '#eff6ff' }
]

// 태그 옵션
const tagOptions = [
  { value: null, label: '자동 태그 사용', description: '시스템이 자동으로 태그를 지정합니다' },
  { value: 'HIGH_PRIORITY', label: '높은 우선순위', description: '반드시 참석해야 하는 일정 (병원, 면접, 시험 등)' },
  { value: 'REMOTE_POSSIBLE', label: '원격 가능', description: '원격으로 진행 가능한 업무 일정' },
  { value: 'PERSONAL_FLEX', label: '개인 일정 (유연)', description: '시간 조율이 가능한 개인 일정 (식사, 약속 등)' },
  { value: 'ALL_DAY_EVENT', label: '종일 일정', description: '하루 종일 진행되는 일정' },
  { value: 'UNKNOWN', label: '미분류', description: '태그를 지정하지 않음' }
]

// 현재 일정의 autoTag (수정 모드에서만 표시)
const autoTag = computed(() => props.schedule?.autoTag || null)

// 초기값 설정
const initForm = () => {
  if (props.schedule) {
    title.value = props.schedule.title || ''
    scheduleType.value = props.schedule.scheduleType || 'PERSONAL'
    userTag.value = props.schedule.userTag || null
    
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
  } else {
    // initialDate가 있으면 사용, 없으면 오늘 날짜
    const dateToUse = props.initialDate || new Date().toISOString().split('T')[0]
    startDate.value = dateToUse
    endDate.value = dateToUse
    userTag.value = null
  }
}

initForm()

// props 변경 시 폼 재초기화
watch(() => [props.schedule, props.initialDate], () => {
  initForm()
}, { deep: true })

// 백엔드 API 형식으로 변환
const buildPayload = () => {
  const payload = {
    title: title.value,
    scheduleType: scheduleType.value,
    startDateTime: `${startDate.value}T${startTime.value}:00`,
    endDateTime: `${endDate.value}T${endTime.value}:00`
  }
  
  // userTag가 선택된 경우에만 포함
  if (userTag.value !== null) {
    payload.userTag = userTag.value
  }
  
  return payload
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
                class="relative flex items-center justify-center p-4 border-2 rounded-xl cursor-pointer transition-all"
                :class="scheduleType === option.value ? 'border-opacity-100' : 'border-gray-200 border-opacity-50'"
                :style="scheduleType === option.value ? { 
                  borderColor: option.color, 
                  backgroundColor: option.bgColor 
                } : {}"
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
                  <svg class="w-5 h-5" :style="{ color: option.color }" fill="currentColor" viewBox="0 0 20 20">
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

          <!-- 태그 선택 -->
          <div>
            <label class="block text-sm font-bold text-gray-900 mb-2">
              일정 태그
            </label>
            
            <!-- 수정 모드에서 autoTag 표시 -->
            <div v-if="isEdit && autoTag" class="mb-3 p-3 bg-blue-50 border border-blue-200 rounded-lg">
              <div class="flex items-start gap-2">
                <svg class="w-5 h-5 text-blue-600 mt-0.5 flex-shrink-0" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clip-rule="evenodd" />
                </svg>
                <div class="flex-1">
                  <div class="text-xs font-semibold text-blue-800 mb-1">시스템 자동 태그</div>
                  <div class="text-sm text-blue-700">
                    {{ tagOptions.find(t => t.value === autoTag)?.label || autoTag }}
                  </div>
                  <div class="text-xs text-blue-600 mt-1">
                    {{ tagOptions.find(t => t.value === autoTag)?.description || '' }}
                  </div>
                </div>
              </div>
            </div>

            <!-- userTag 선택 드롭다운 -->
            <select
              v-model="userTag"
              :disabled="loading"
              class="w-full px-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-slate-500 focus:border-transparent disabled:bg-gray-50 disabled:cursor-not-allowed transition-all text-gray-900 bg-white"
            >
              <option 
                v-for="option in tagOptions" 
                :key="option.value" 
                :value="option.value"
              >
                {{ option.label }}
              </option>
            </select>
            
            <!-- 선택된 태그 설명 -->
            <div v-if="userTag && tagOptions.find(t => t.value === userTag)" class="mt-2 text-xs text-gray-600 px-1">
              {{ tagOptions.find(t => t.value === userTag)?.description }}
            </div>
            
            <!-- 안내 문구 -->
            <div class="mt-2 text-xs text-gray-500 px-1">
              <p>태그를 선택하면 워케이션 기간 추천 시 해당 태그가 우선적으로 사용됩니다.</p>
              <p v-if="isEdit && autoTag" class="mt-1">자동 태그가 마음에 들지 않으면 위에서 다른 태그를 선택하세요.</p>
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

