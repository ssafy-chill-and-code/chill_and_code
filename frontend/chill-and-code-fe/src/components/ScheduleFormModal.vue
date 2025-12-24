<script setup>
import { ref, computed, watch } from 'vue'
import { useScheduleStore } from '@/stores/schedule'
import { useThemeStore } from '@/stores/theme'

const themeStore = useThemeStore()
const isDarkMode = computed(() => themeStore.isDarkMode)

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
const tagMode = ref(null) // 'auto' 또는 'manual', null이면 아직 선택 안함
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

// 태그 처리 방식 선택 옵션
const tagModeOptions = [
  { 
    value: 'auto', 
    label: '자동으로 처리하기', 
    description: '시스템이 일정 내용을 분석해 자동으로 태그를 지정합니다',
    icon: '🤖'
  },
  { 
    value: 'manual', 
    label: '직접 선택하기', 
    description: '일정의 성격을 직접 선택합니다',
    icon: '✋'
  }
]

// 직접 선택 시 태그 옵션
const manualTagOptions = [
  { 
    value: 'HIGH_PRIORITY', 
    label: '하루 종일 꼭 비워야 하는 일정', 
    description: '워케이션 추천 시 이 날짜는 제외됩니다.',
    icon: '🔴',
    color: '#ef4444',
    bgColor: '#fef2f2'
  },
  { 
    value: 'REMOTE_POSSIBLE', 
    label: '원격으로도 가능한 일정', 
    description: '원격 근무 허용 시 워케이션 기간에 포함될 수 있습니다.',
    icon: '💻',
    color: '#3b82f6',
    bgColor: '#eff6ff'
  },
  { 
    value: 'PERSONAL_FLEX', 
    label: '조정 가능한 개인 일정', 
    description: '워케이션 기간에 포함됩니다.',
    icon: '📅',
    color: '#10b981',
    bgColor: '#f0fdf4'
  },
  { 
    value: 'UNKNOWN', 
    label: '거의 신경 쓰지 않아도 되는 일정', 
    description: '워케이션 기간에 포함됩니다.',
    icon: '⚪',
    color: '#6b7280',
    bgColor: '#f9fafb'
  }
]

// 태그 옵션 (수정 모드에서 autoTag 표시용)
const tagOptions = [
  { value: null, label: '자동 태그 사용', description: '시스템이 자동으로 태그를 지정합니다' },
  ...manualTagOptions
]

// 현재 일정의 autoTag (수정 모드에서만 표시)
const autoTag = computed(() => props.schedule?.autoTag || null)

// 초기값 설정
const initForm = () => {
  if (props.schedule) {
    // 수정 모드: 기존 일정 정보로 채우기
    title.value = props.schedule.title || ''
    scheduleType.value = props.schedule.scheduleType || 'PERSONAL'
    userTag.value = props.schedule.userTag || null
    // 수정 모드에서는 userTag가 있으면 manual, 없으면 auto
    tagMode.value = userTag.value !== null ? 'manual' : 'auto'
    
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
    // initialDate가 있으면 사용, 없으면 오늘 날짜
    const dateToUse = props.initialDate || new Date().toISOString().split('T')[0]
    startDate.value = dateToUse
    endDate.value = dateToUse
    userTag.value = null
    tagMode.value = null // 새 일정은 아직 선택 안함
  }
}

// 태그 모드 변경 핸들러
const handleTagModeChange = (mode) => {
  tagMode.value = mode
  if (mode === 'auto') {
    userTag.value = null
  }
  // manual 모드로 변경해도 userTag는 그대로 유지 (사용자가 선택할 때까지)
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
              이 일정은 어떻게 처리할까요?
            </label>
            
            <!-- 안내 문구 -->
            <p class="text-xs text-gray-500 mb-4 px-1">
              이 설정은 나중에 일정 추천에 반영돼요.
            </p>
            
            <!-- 수정 모드에서 autoTag 표시 -->
            <div v-if="isEdit && autoTag && tagMode === 'auto'" class="mb-4 p-4 bg-blue-50 border border-blue-200 rounded-xl">
              <div class="flex items-start gap-3">
                <svg class="w-5 h-5 text-blue-600 mt-0.5 flex-shrink-0" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clip-rule="evenodd" />
                </svg>
                <div class="flex-1">
                  <div 
                    :class="[
                      'text-xs font-semibold mb-1',
                      isDarkMode ? 'text-gray-900' : 'text-blue-800'
                    ]"
                  >
                    시스템이 자동으로 지정한 태그
                  </div>
                  <div 
                    :class="[
                      'text-sm font-medium mb-1',
                      isDarkMode ? 'text-gray-900' : 'text-blue-900'
                    ]"
                  >
                    {{ tagOptions.find(t => t.value === autoTag)?.label || autoTag }}
                  </div>
                  <div 
                    :class="[
                      'text-xs',
                      isDarkMode ? 'text-gray-800' : 'text-blue-700'
                    ]"
                  >
                    {{ tagOptions.find(t => t.value === autoTag)?.description || '' }}
                  </div>
                </div>
              </div>
            </div>

            <!-- 1단계: 자동/직접 선택 -->
            <div v-if="tagMode === null" class="space-y-3">
              <label
                v-for="option in tagModeOptions"
                :key="option.value"
                class="relative flex items-start p-4 border-2 rounded-xl cursor-pointer transition-all hover:shadow-md"
                :class="'border-gray-200 border-opacity-50 hover:border-opacity-75'"
              >
                <input
                  type="radio"
                  :value="option.value"
                  @change="handleTagModeChange(option.value)"
                  :disabled="loading"
                  class="sr-only"
                />
                <div class="flex items-start gap-3 flex-1">
                  <!-- 아이콘 -->
                  <div class="text-2xl flex-shrink-0 mt-0.5">
                    {{ option.icon }}
                  </div>
                  <!-- 내용 -->
                  <div class="flex-1 min-w-0">
                    <div class="text-sm font-semibold text-gray-900 mb-1">
                      {{ option.label }}
                    </div>
                    <div class="text-xs text-gray-600 leading-relaxed">
                      {{ option.description }}
                    </div>
                  </div>
                </div>
              </label>
            </div>

            <!-- 2단계: 직접 선택 시 상세 옵션 -->
            <div v-if="tagMode === 'manual'" class="space-y-3">
              <!-- 뒤로가기 버튼 -->
              <button
                type="button"
                @click="tagMode = null; userTag = null"
                class="flex items-center gap-2 text-sm text-gray-600 hover:text-gray-900 mb-2 transition-colors"
              >
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
                </svg>
                <span>처리 방식 다시 선택하기</span>
              </button>

              <!-- 직접 선택 옵션들 -->
              <label
                v-for="option in manualTagOptions"
                :key="option.value"
                class="relative flex items-start p-4 border-2 rounded-xl cursor-pointer transition-all hover:shadow-md"
                :class="userTag === option.value ? 'border-opacity-100 shadow-sm' : 'border-gray-200 border-opacity-50 hover:border-opacity-75'"
                :style="userTag === option.value && option.color ? { 
                  borderColor: option.color, 
                  backgroundColor: option.bgColor || 'transparent'
                } : {}"
              >
                <input
                  type="radio"
                  :value="option.value"
                  v-model="userTag"
                  :disabled="loading"
                  class="sr-only"
                />
                <div class="flex items-start gap-3 flex-1">
                  <!-- 아이콘 -->
                  <div class="text-2xl flex-shrink-0 mt-0.5">
                    {{ option.icon }}
                  </div>
                  <!-- 내용 -->
                  <div class="flex-1 min-w-0">
                    <div class="text-sm font-semibold text-gray-900 mb-1">
                      {{ option.label }}
                    </div>
                    <div class="text-xs text-gray-600 leading-relaxed">
                      {{ option.description }}
                    </div>
                  </div>
                  <!-- 선택 표시 -->
                  <div
                    v-if="userTag === option.value"
                    class="flex-shrink-0"
                  >
                    <svg class="w-5 h-5" :style="{ color: option.color || '#6366f1' }" fill="currentColor" viewBox="0 0 20 20">
                      <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd" />
                    </svg>
                  </div>
                </div>
              </label>
            </div>

            <!-- 자동 처리 선택 시 안내 -->
            <div v-if="tagMode === 'auto'" class="mt-3 p-3 bg-gray-50 rounded-lg">
              <div class="flex items-start gap-2">
                <svg class="w-4 h-4 text-gray-500 mt-0.5 flex-shrink-0" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clip-rule="evenodd" />
                </svg>
                <div class="text-xs text-gray-600">
                  <p>시스템이 일정 내용을 분석해 자동으로 태그를 지정합니다.</p>
                  <button
                    type="button"
                    @click="tagMode = null"
                    class="mt-2 text-blue-600 hover:text-blue-800 font-medium underline"
                  >
                    직접 선택하기로 변경
                  </button>
                </div>
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

