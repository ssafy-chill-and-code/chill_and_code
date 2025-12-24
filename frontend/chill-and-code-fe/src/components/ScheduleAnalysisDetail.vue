<template>
  <div 
    :class="[
      'rounded-lg shadow-sm p-4 space-y-4 transition-colors',
      isDarkMode 
        ? 'bg-gray-800/50 border border-gray-700' 
        : 'bg-white border border-gray-200'
    ]"
  >
    <!-- 헤더 -->
    <div class="flex items-center gap-2">
      <svg 
        :class="[
          'w-5 h-5 transition-colors',
          isDarkMode ? 'text-indigo-400' : 'text-indigo-600'
        ]"
        fill="none" 
        stroke="currentColor" 
        viewBox="0 0 24 24"
      >
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
      </svg>
      <h3 
        :class="[
          'text-sm font-bold transition-colors',
          isDarkMode ? 'text-white' : 'text-gray-900'
        ]"
      >
        상세 분석
      </h3>
    </div>

    <!-- 로딩 상태 -->
    <div v-if="loading" class="py-6 text-center">
      <div 
        :class="[
          'inline-block animate-spin rounded-full h-6 w-6 border-3 transition-colors',
          isDarkMode 
            ? 'border-gray-700 border-t-indigo-400' 
            : 'border-gray-200 border-t-indigo-600'
        ]"
      ></div>
      <p 
        :class="[
          'mt-3 text-xs font-medium transition-colors',
          isDarkMode ? 'text-gray-300' : 'text-gray-600'
        ]"
      >
        분석 중...
      </p>
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

    <!-- 분석 결과 -->
    <div v-else-if="summary" class="space-y-4">
      <!-- 워케이션 신호 -->
      <div class="space-y-2">
        <div 
          :class="[
            'text-xs font-semibold transition-colors',
            isDarkMode ? 'text-gray-300' : 'text-gray-700'
          ]"
        >
          워케이션 신호
        </div>
        <div class="space-y-2">
          <div 
            v-if="summary.recommendWeeks && summary.recommendWeeks.length > 0" 
            :class="[
              'flex items-start gap-2 p-2.5 rounded-lg border transition-colors',
              isDarkMode
                ? 'bg-emerald-900/30 border-emerald-700'
                : 'bg-emerald-50 border-emerald-200'
            ]"
          >
            <svg 
              :class="[
                'w-4 h-4 flex-shrink-0 mt-0.5 transition-colors',
                isDarkMode ? 'text-emerald-400' : 'text-emerald-600'
              ]"
              fill="none" 
              stroke="currentColor" 
              viewBox="0 0 24 24"
            >
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <div class="flex-1 min-w-0">
              <div 
                :class="[
                  'text-xs font-semibold mb-0.5 transition-colors',
                  isDarkMode ? 'text-emerald-100' : 'text-emerald-900'
                ]"
              >
                여유로운 주
              </div>
              <div 
                :class="[
                  'text-xs transition-colors',
                  isDarkMode ? 'text-emerald-200' : 'text-emerald-700'
                ]"
              >
                {{ formatWeeks(summary.recommendWeeks) }}
              </div>
            </div>
          </div>
          
          <div 
            v-if="summary.mostBusyWeek > 0" 
            :class="[
              'flex items-start gap-2 p-2.5 rounded-lg border transition-colors',
              isDarkMode
                ? 'bg-amber-900/30 border-amber-700'
                : 'bg-amber-50 border-amber-200'
            ]"
          >
            <svg 
              :class="[
                'w-4 h-4 flex-shrink-0 mt-0.5 transition-colors',
                isDarkMode ? 'text-amber-400' : 'text-amber-600'
              ]"
              fill="none" 
              stroke="currentColor" 
              viewBox="0 0 24 24"
            >
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
            </svg>
            <div class="flex-1 min-w-0">
              <div 
                :class="[
                  'text-xs font-semibold mb-0.5 transition-colors',
                  isDarkMode ? 'text-amber-100' : 'text-amber-900'
                ]"
              >
                밀도 높은 주
              </div>
              <div 
                :class="[
                  'text-xs transition-colors',
                  isDarkMode ? 'text-amber-200' : 'text-amber-700'
                ]"
              >
                {{ summary.mostBusyWeek }}주차
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 일정 구성 비율 -->
      <div class="space-y-2">
        <div 
          :class="[
            'text-xs font-semibold transition-colors',
            isDarkMode ? 'text-gray-300' : 'text-gray-700'
          ]"
        >
          일정 유형
        </div>
        <div v-if="hasAnyScheduleType" class="space-y-1">
          <template v-for="(ratio, type) in scheduleTypeRatio" :key="type">
            <div v-if="ratio > 0" class="space-y-1">
              <div class="flex items-center justify-between text-xs">
                <span 
                  :class="[
                    'transition-colors',
                    isDarkMode ? 'text-gray-400' : 'text-gray-600'
                  ]"
                >
                  {{ getScheduleTypeLabel(type) }}
                </span>
                <span 
                  :class="[
                    'font-bold transition-colors',
                    isDarkMode ? 'text-white' : 'text-gray-900'
                  ]"
                >
                  {{ getPercentage(ratio) }}%
                </span>
              </div>
              <div 
                :class="[
                  'w-full rounded-full h-1.5 transition-colors',
                  isDarkMode ? 'bg-gray-700' : 'bg-gray-200'
                ]"
              >
                <div 
                  :class="getScheduleTypeColor(type)"
                  class="h-1.5 rounded-full transition-all duration-500"
                  :style="{ width: `${getPercentage(ratio)}%` }"
                ></div>
              </div>
            </div>
            <div 
              v-else 
              :class="[
                'text-xs italic py-1 transition-colors',
                isDarkMode ? 'text-gray-500' : 'text-gray-400'
              ]"
            >
              {{ getScheduleTypeLabel(type) }}: 등록된 일정 없음
            </div>
          </template>
        </div>
        <div 
          v-else 
          :class="[
            'text-xs italic py-2 text-center transition-colors',
            isDarkMode ? 'text-gray-400' : 'text-gray-500'
          ]"
        >
          등록된 일정이 없습니다.
        </div>
      </div>

      <!-- 일정 분포 -->
      <div class="space-y-2">
        <div 
          :class="[
            'text-xs font-semibold transition-colors',
            isDarkMode ? 'text-gray-300' : 'text-gray-700'
          ]"
        >
          일정 분포
        </div>
        
        <!-- 해석 메시지 -->
        <div 
          v-if="distributionMessage" 
          :class="[
            'p-2.5 rounded-lg border mb-2 transition-colors',
            isDarkMode
              ? 'bg-indigo-900/30 border-indigo-700'
              : 'bg-indigo-50 border-indigo-200'
          ]"
        >
          <p 
            :class="[
              'text-xs leading-relaxed transition-colors',
              isDarkMode ? 'text-indigo-200' : 'text-indigo-800'
            ]"
          >
            {{ distributionMessage }}
          </p>
        </div>
        
        <div class="space-y-1.5">
          <div v-if="blockedDays > 0">
            <div class="flex items-center justify-between text-xs mb-0.5">
              <span 
                :class="[
                  'transition-colors',
                  isDarkMode ? 'text-gray-400' : 'text-gray-600'
                ]"
              >
                차단
              </span>
              <span 
                :class="[
                  'font-bold transition-colors',
                  isDarkMode ? 'text-white' : 'text-gray-900'
                ]"
              >
                {{ blockedDays }}일
              </span>
            </div>
            <div 
              :class="[
                'w-full rounded-full h-1.5 transition-colors',
                isDarkMode ? 'bg-gray-700' : 'bg-gray-200'
              ]"
            >
              <div 
                class="h-1.5 rounded-full bg-red-500 transition-all duration-500"
                :style="{ width: `${blockedDaysPercent}%` }"
              ></div>
            </div>
          </div>
          
          <div v-if="summary.mixedDayCount > 0">
            <div class="flex items-center justify-between text-xs mb-0.5">
              <span 
                :class="[
                  'transition-colors',
                  isDarkMode ? 'text-gray-400' : 'text-gray-600'
                ]"
              >
                조율 가능
              </span>
              <span 
                :class="[
                  'font-bold transition-colors',
                  isDarkMode ? 'text-white' : 'text-gray-900'
                ]"
              >
                {{ summary.mixedDayCount }}일
              </span>
            </div>
            <div 
              :class="[
                'w-full rounded-full h-1.5 transition-colors',
                isDarkMode ? 'bg-gray-700' : 'bg-gray-200'
              ]"
            >
              <div 
                class="h-1.5 rounded-full bg-amber-500 transition-all duration-500"
                :style="{ width: `${mixedDaysPercent}%` }"
              ></div>
            </div>
          </div>
          
          <div v-if="summary.flexibleDayCount > 0">
            <div class="flex items-center justify-between text-xs mb-0.5">
              <span 
                :class="[
                  'transition-colors',
                  isDarkMode ? 'text-gray-400' : 'text-gray-600'
                ]"
              >
                여유
              </span>
              <span 
                :class="[
                  'font-bold transition-colors',
                  isDarkMode ? 'text-white' : 'text-gray-900'
                ]"
              >
                {{ summary.flexibleDayCount }}일
              </span>
            </div>
            <div 
              :class="[
                'w-full rounded-full h-1.5 transition-colors',
                isDarkMode ? 'bg-gray-700' : 'bg-gray-200'
              ]"
            >
              <div 
                class="h-1.5 rounded-full bg-emerald-500 transition-all duration-500"
                :style="{ width: `${flexibleDaysPercent}%` }"
              ></div>
            </div>
          </div>
        </div>
      </div>

      <!-- 판단 기준 안내 -->
      <button
        @click="showCriteria = !showCriteria"
        :class="[
          'flex items-center justify-between w-full p-2 rounded-lg transition-colors text-xs',
          isDarkMode
            ? 'bg-gray-700/50 hover:bg-gray-700'
            : 'bg-gray-50 hover:bg-gray-100'
        ]"
      >
        <div class="flex items-center gap-2">
          <svg 
            :class="[
              'w-3.5 h-3.5 transition-colors',
              isDarkMode ? 'text-indigo-400' : 'text-indigo-600'
            ]"
            fill="none" 
            stroke="currentColor" 
            viewBox="0 0 24 24"
          >
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          <span 
            :class="[
              'font-medium transition-colors',
              isDarkMode ? 'text-gray-300' : 'text-gray-700'
            ]"
          >
            판단 기준
          </span>
        </div>
        <svg 
          :class="[
            'w-3.5 h-3.5 transition-all',
            isDarkMode ? 'text-gray-400' : 'text-gray-500',
            showCriteria ? 'rotate-180' : ''
          ]"
          fill="none" 
          stroke="currentColor" 
          viewBox="0 0 24 24"
        >
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
        </svg>
      </button>
      
      <div 
        v-if="showCriteria" 
        :class="[
          'p-2.5 rounded-lg border text-xs transition-colors',
          isDarkMode
            ? 'bg-blue-900/30 border-blue-700'
            : 'bg-blue-50 border-blue-200'
        ]"
      >
        <div 
          :class="[
            'font-semibold mb-1 transition-colors',
            isDarkMode ? 'text-blue-100' : 'text-blue-900'
          ]"
        >
          ℹ 판단 기준
        </div>
        <ul 
          :class="[
            'space-y-0.5 list-disc list-inside transition-colors',
            isDarkMode ? 'text-blue-200' : 'text-blue-800'
          ]"
        >
          <li>종일 또는 중요 일정이 있는 날은 차단됩니다</li>
          <li>하루에 5시간 이상 연속된 일정이 있으면 부담이 큰 날로 분류됩니다</li>
          <li>그 외의 날은 이동·체류가 가능한 날로 판단합니다</li>
        </ul>
      </div>
    </div>

    <!-- 데이터 없음 -->
    <div v-else class="py-6 text-center">
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
const showCriteria = ref(false)

const summary = computed(() => scheduleStore.summary)

// 일정 유형 비율
const scheduleTypeRatio = computed(() => {
  if (!summary.value || !summary.value.scheduleTypeRatio) return {}
  return summary.value.scheduleTypeRatio
})

// 일정 유형이 하나라도 있는지 확인
const hasAnyScheduleType = computed(() => {
  if (!summary.value || !summary.value.scheduleTypeRatio) return false
  return Object.values(summary.value.scheduleTypeRatio).some(ratio => ratio > 0)
})

// 백엔드에서 0.0~1.0 범위로 오므로 백분율로 변환
const getPercentage = (ratio) => {
  return Math.round((ratio || 0) * 100)
}

// 일정 분포 해석 메시지
const distributionMessage = computed(() => {
  if (!summary.value) return ''
  
  const blocked = blockedDays.value
  const mixed = summary.mixedDayCount
  const flexible = summary.flexibleDayCount
  
  // 모든 날이 여유로운 경우
  if (blocked === 0 && mixed === 0 && flexible > 0) {
    return '이번 달에는 차단되거나 조율이 필요한 일정이 없어요. 모든 날짜가 여유로운 상태예요.'
  }
  
  // 차단 일정만 없는 경우
  if (blocked === 0 && mixed > 0) {
    return '이번 달에는 차단되는 일정이 없어요. 일부 날짜는 조율이 필요하지만 워케이션 계획이 가능해요.'
  }
  
  // 조율 일정만 없는 경우
  if (blocked > 0 && mixed === 0 && flexible > 0) {
    return '이번 달에는 조율이 필요한 일정이 없어요. 차단되는 날과 여유로운 날로만 구성되어 있어요.'
  }
  
  // 차단과 조율 모두 없는 경우 (위에서 처리되지만 안전장치)
  if (blocked === 0 && mixed === 0) {
    return '모든 날짜가 여유로운 상태예요.'
  }
  
  return ''
})

// 일정 분포 계산
const blockedDays = computed(() => {
  if (!summary.value) return 0
  return summary.value.totalDays - summary.value.flexibleDayCount - summary.value.mixedDayCount
})

const blockedDaysPercent = computed(() => {
  if (!summary.value || summary.value.totalDays === 0) return 0
  return (blockedDays.value / summary.value.totalDays) * 100
})

const mixedDaysPercent = computed(() => {
  if (!summary.value || summary.value.totalDays === 0) return 0
  return (summary.value.mixedDayCount / summary.value.totalDays) * 100
})

const flexibleDaysPercent = computed(() => {
  if (!summary.value || summary.value.totalDays === 0) return 0
  return (summary.value.flexibleDayCount / summary.value.totalDays) * 100
})

// 헬퍼 함수
const formatWeeks = (weeks) => {
  if (!weeks || weeks.length === 0) return ''
  return weeks.map(w => `${w}주차`).join(', ')
}

const getScheduleTypeLabel = (type) => {
  const labels = {
    'WORK': '업무 일정',
    'PERSONAL': '개인 일정',
    'WORKATION': '워케이션 일정'
  }
  return labels[type] || type
}

const getScheduleTypeColor = (type) => {
  const colors = {
    'WORK': 'bg-red-500',
    'PERSONAL': 'bg-yellow-500',
    'WORKATION': 'bg-blue-500'
  }
  return colors[type] || 'bg-gray-500'
}

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

