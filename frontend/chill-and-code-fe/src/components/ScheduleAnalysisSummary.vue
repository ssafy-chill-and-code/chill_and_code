<template>
  <div 
    :class="[
      'rounded-lg shadow-sm border p-4 transition-colors',
      isDarkMode 
        ? 'bg-gray-800/50 border-gray-700' 
        : 'bg-white border-gray-200'
    ]"
  >
    <!-- 로딩 상태 -->
    <div v-if="loading" class="flex items-center gap-3">
      <div 
        :class="[
          'inline-block animate-spin rounded-full h-4 w-4 border-2 transition-colors',
          isDarkMode 
            ? 'border-gray-700 border-t-indigo-400' 
            : 'border-gray-200 border-t-indigo-600'
        ]"
      ></div>
      <span 
        :class="[
          'text-xs transition-colors',
          isDarkMode ? 'text-gray-300' : 'text-gray-600'
        ]"
      >
        분석 중...
      </span>
    </div>

    <!-- 에러 상태 -->
    <div 
      v-else-if="error" 
      :class="[
        'p-3 border-l-4 rounded text-xs transition-colors',
        isDarkMode
          ? 'bg-red-900/30 border-red-500 text-red-300'
          : 'bg-red-50 border-red-500 text-red-800'
      ]"
    >
      <div class="flex items-center gap-2">
        <svg class="w-4 h-4 flex-shrink-0" fill="currentColor" viewBox="0 0 20 20">
          <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
        </svg>
        <span>{{ error }}</span>
      </div>
    </div>

    <!-- 요약 멘트 -->
    <div v-else-if="summary">
      <!-- 일정이 없는 경우 -->
      <div v-if="hasNoSchedules" class="text-center py-3">
        <div class="flex items-center justify-center gap-2 mb-2">
          <svg 
            :class="[
              'w-5 h-5 transition-colors',
              isDarkMode ? 'text-gray-500' : 'text-gray-400'
            ]"
            fill="none" 
            stroke="currentColor" 
            viewBox="0 0 24 24"
          >
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
          </svg>
        </div>
        <p 
          :class="[
            'text-sm font-medium mb-1 transition-colors',
            isDarkMode ? 'text-gray-300' : 'text-gray-700'
          ]"
        >
          이번 달에는 아직 등록된 일정이 없어요.
        </p>
        <p 
          :class="[
            'text-xs transition-colors',
            isDarkMode ? 'text-gray-400' : 'text-gray-500'
          ]"
        >
          일정을 추가하면 분석이 시작돼요.
        </p>
      </div>
      
      <!-- 일정이 있는 경우 -->
      <div v-else class="flex items-center gap-3">
        <div class="flex-shrink-0">
          <span :class="statusBadgeClass" class="px-2.5 py-1 rounded-full text-xs font-bold transition-colors">
            {{ statusLabel }} {{ statusEmoji }}
          </span>
        </div>
        <div class="flex-1 min-w-0">
          <div 
            :class="[
              'text-sm font-semibold transition-colors',
              isDarkMode ? 'text-white !important' : 'text-gray-900'
            ]"
            :style="isDarkMode ? { color: '#ffffff' } : {}"
          >
            {{ summary.summaryComment }}
          </div>
          <div 
            :class="[
              'flex items-center gap-4 mt-1.5 text-xs transition-colors',
              isDarkMode ? 'text-gray-300' : 'text-gray-600'
            ]"
          >
            <span>차단 {{ blockedDays }}일</span>
            <span>조율 {{ summary.mixedDayCount }}일</span>
            <span>여유 {{ summary.flexibleDayCount }}일</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 데이터 없음 -->
    <div v-else class="text-center py-2">
      <p 
        :class="[
          'text-xs transition-colors',
          isDarkMode ? 'text-gray-400' : 'text-gray-500'
        ]"
      >
        분석할 일정이 없습니다.
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useScheduleStore } from '@/stores/schedule'
import { useThemeStore } from '@/stores/theme'

const themeStore = useThemeStore()
const isDarkMode = computed(() => themeStore.isDarkMode)

const props = defineProps({
  month: {
    type: String,
    required: true
  }
})

const scheduleStore = useScheduleStore()
const loading = ref(false)
const error = ref(null)

const summary = computed(() => scheduleStore.summary)

// 상태 뱃지 관련
const statusLabel = computed(() => {
  if (!summary.value) return ''
  const status = summary.value.overallStatus
  if (status === 'FLEXIBLE') return '유연'
  if (status === 'MIXED') return '보통'
  if (status === 'BUSY') return '바쁨'
  return ''
})

const statusEmoji = computed(() => {
  if (!summary.value) return ''
  const status = summary.value.overallStatus
  if (status === 'FLEXIBLE') return '🟢'
  if (status === 'MIXED') return '🟡'
  if (status === 'BUSY') return '🔴'
  return ''
})

const statusBadgeClass = computed(() => {
  if (!summary.value) return ''
  const status = summary.value.overallStatus
  if (status === 'FLEXIBLE') {
    return isDarkMode.value 
      ? 'bg-emerald-900/40 text-emerald-100 border border-emerald-700'
      : 'bg-emerald-100 text-emerald-700'
  }
  if (status === 'MIXED') {
    return isDarkMode.value 
      ? 'bg-amber-900/40 text-amber-100 border border-amber-700'
      : 'bg-amber-100 text-amber-700'
  }
  if (status === 'BUSY') {
    return isDarkMode.value 
      ? 'bg-red-900/40 text-red-100 border border-red-700'
      : 'bg-red-100 text-red-700'
  }
  return ''
})

// 일정 분포 계산
const blockedDays = computed(() => {
  if (!summary.value) return 0
  return summary.value.totalDays - summary.value.flexibleDayCount - summary.value.mixedDayCount
})

// 일정이 전혀 없는지 확인 (모든 날이 여유롭고, 일정 유형 비율이 모두 0이거나 없음)
const hasNoSchedules = computed(() => {
  if (!summary.value) return false
  
  // 일정 유형 비율 확인
  const typeRatio = summary.value.scheduleTypeRatio || {}
  const hasAnyScheduleType = Object.values(typeRatio).some(ratio => ratio > 0)
  
  // 모든 날이 여유롭고 일정 유형이 없으면 일정이 없는 것으로 판단
  return !hasAnyScheduleType && 
         blockedDays.value === 0 && 
         summary.value.mixedDayCount === 0 &&
         summary.value.flexibleDayCount === summary.value.totalDays
})

// API 호출
const fetchSummary = async () => {
  loading.value = true
  error.value = null
  try {
    await scheduleStore.fetchSummary(props.month)
  } catch (e) {
    error.value = '일정 분석을 불러오는데 실패했습니다.'
    console.error('일정 분석 조회 실패:', e)
  } finally {
    loading.value = false
  }
}

// 초기 로드 및 월 변경 감지
onMounted(() => {
  fetchSummary()
})

watch(() => props.month, () => {
  fetchSummary()
})

// 일정이 변경되면 분석도 갱신
watch(() => scheduleStore.schedules, () => {
  // 일정이 변경되면 분석 요약 갱신
  if (props.month) {
    fetchSummary()
  }
}, { deep: true })
</script>
