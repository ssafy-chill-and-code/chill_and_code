<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useScheduleStore } from '@/stores/schedule'
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
import interactionPlugin from '@fullcalendar/interaction'
import ScheduleFormModal from '@/components/ScheduleFormModal.vue'
import ScheduleDetailModal from '@/components/ScheduleDetailModal.vue'
import ScheduleAnalysisSummary from '@/components/ScheduleAnalysisSummary.vue'
import ScheduleAnalysisDetail from '@/components/ScheduleAnalysisDetail.vue'

const router = useRouter()
const scheduleStore = useScheduleStore()

// 상태 관리
const loading = ref(false)
const error = ref(null)
const currentDate = ref(new Date())
const selectedTypes = ref(['PERSONAL', 'WORK', 'WORKATION'])
const showFormModal = ref(false)
const showDetailModal = ref(false)
const selectedSchedule = ref(null)
const editingSchedule = ref(null)
const initialDate = ref(null) // 캘린더에서 클릭한 날짜
const lastFetchedMonth = ref(null) // 중복 호출 방지
const showMonthPicker = ref(false) // 월 선택 모달

// 캘린더 ref
const calendarRef = ref(null)

// 일정 유형 정의
const scheduleTypes = [
  { value: 'PERSONAL', label: '개인 일정', color: '#fde68a', bgColor: '#fffbeb' }, // 노란 계열 (더 연한 톤)
  { value: 'WORK', label: '업무 일정', color: '#fca5a5', bgColor: '#fef2f2' }, // 붉은 계열 (더 연한 톤)
  { value: 'WORKATION', label: '워케이션 일정', color: '#93c5fd', bgColor: '#eff6ff' } // 푸른 계열 (더 연한 톤)
]

// 월 포맷
const formatMonth = (date) => {
  const y = date.getFullYear()
  const m = String(date.getMonth() + 1).padStart(2, '0')
  return `${y}-${m}`
}

// 현재 연/월 표시
const currentYearMonth = computed(() => {
  const y = currentDate.value.getFullYear()
  const m = currentDate.value.getMonth() + 1
  return `${y}년 ${m}월`
})

// 작은 달력 데이터 생성
const miniCalendar = computed(() => {
  const date = currentDate.value
  const year = date.getFullYear()
  const month = date.getMonth()
  
  // 현재 월의 첫 번째 날과 마지막 날
  const firstDay = new Date(year, month, 1)
  const lastDay = new Date(year, month + 1, 0)
  const daysInMonth = lastDay.getDate()
  
  // 첫 번째 날의 요일 (0 = 일요일)
  const firstDayOfWeek = firstDay.getDay()
  
  // 작은 달력 배열 생성
  const weeks = []
  let week = []
  
  // 첫 주의 빈 칸 채우기
  for (let i = 0; i < firstDayOfWeek; i++) {
    week.push(null)
  }
  
  // 날짜 채우기
  for (let day = 1; day <= daysInMonth; day++) {
    week.push(day)
    if (week.length === 7) {
      weeks.push(week)
      week = []
    }
  }
  
  // 마지막 주의 빈 칸 채우기
  if (week.length > 0) {
    while (week.length < 7) {
      week.push(null)
    }
    weeks.push(week)
  }
  
  return weeks
})

// 오늘 날짜 확인
const isToday = (day) => {
  if (!day) return false
  const today = new Date()
  return (
    today.getFullYear() === currentDate.value.getFullYear() &&
    today.getMonth() === currentDate.value.getMonth() &&
    today.getDate() === day
  )
}

// 요일 라벨
const weekDays = ['일', '월', '화', '수', '목', '금', '토']

const months = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]

const selectedYear = ref(currentDate.value.getFullYear())
const selectedMonth = ref(currentDate.value.getMonth() + 1)

// 연도 증감 함수
const decreaseYear = () => {
  selectedYear.value--
}

const increaseYear = () => {
  selectedYear.value++
}

// 월 선택 모달 열기
const openMonthPicker = () => {
  selectedYear.value = currentDate.value.getFullYear()
  selectedMonth.value = currentDate.value.getMonth() + 1
  showMonthPicker.value = true
}

// 월 선택 확정
const applyMonthSelection = () => {
  const newDate = new Date(selectedYear.value, selectedMonth.value - 1, 1)
  currentDate.value = newDate
  
  if (calendarRef.value) {
    const api = calendarRef.value.getApi()
    api.gotoDate(newDate)
  }
  
  showMonthPicker.value = false
  fetchSchedules(formatMonth(newDate))
}

// 일정 데이터를 FullCalendar 이벤트로 변환
const calendarEvents = computed(() => {
  return scheduleStore.schedules
    .filter(s => selectedTypes.value.includes(s.scheduleType))
    .map(s => {
      const typeInfo = scheduleTypes.find(t => t.value === s.scheduleType)
      
      // 하루짜리 일정의 경우 end를 명시적으로 설정
      let endDate = s.endDateTime
      if (!endDate || endDate === s.startDateTime) {
        // 하루짜리 일정: 다음 날 자정으로 설정
        const start = new Date(s.startDateTime)
        const nextDay = new Date(start)
        nextDay.setDate(nextDay.getDate() + 1)
        nextDay.setHours(0, 0, 0, 0)
        endDate = nextDay.toISOString()
      }
      
      return {
        id: String(s.scheduleId),
        title: s.title,
        start: s.startDateTime,
        end: endDate,
        backgroundColor: typeInfo?.bgColor || '#e5e7eb',
        borderColor: typeInfo?.color || '#9ca3af',
        borderWidth: 2,
        textColor: '#1f2937',
        classNames: [`schedule-type-${s.scheduleType.toLowerCase()}`],
        extendedProps: {
          scheduleType: s.scheduleType,
          scheduleId: s.scheduleId
        }
      }
    })
})

// FullCalendar 옵션
const calendarOptions = computed(() => ({
  plugins: [dayGridPlugin, interactionPlugin],
  initialView: 'dayGridMonth',
  initialDate: currentDate.value, // 현재 날짜를 유지
  headerToolbar: false,
  height: 'auto',
  locale: 'ko',
  dayMaxEvents: 3,
  events: calendarEvents.value,
  eventClick: handleEventClick,
  dateClick: handleDateClick,
  datesSet: handleDatesSet,
  firstDay: 0,
  buttonText: {
    today: '오늘'
  },
  dayHeaderFormat: { weekday: 'short' },
  displayEventTime: false, // 시간 숨기기, 제목만 표시
  eventTimeFormat: {
    hour: '2-digit',
    minute: '2-digit',
    meridiem: false
  }
}))

// 월별 일정 조회
const fetchSchedules = async (month) => {
  console.log('🔍 fetchSchedules 호출:', {
    month,
    lastFetchedMonth: lastFetchedMonth.value,
    loading: loading.value,
    willSkip: loading.value && lastFetchedMonth.value === month
  })
  
  // 동일한 요청이 동시에 여러 번 발생하는 것 방지
  if (loading.value && lastFetchedMonth.value === month) {
    console.log('⏭️ 중복 호출 스킵')
    return
  }
  
  lastFetchedMonth.value = month
  loading.value = true
  error.value = null
  
  try {
    console.log('📡 API 호출 시작:', month)
    await scheduleStore.fetchSchedules(month)
    console.log('✅ API 호출 성공:', month)
    
    // API 호출 후 캘린더 이벤트 업데이트
    if (calendarRef.value) {
      const api = calendarRef.value.getApi()
      // 현재 보기 날짜를 저장
      const currentViewDate = api.getDate()
      console.log('📍 현재 캘린더 날짜:', formatMonth(currentViewDate))
      
      // 이벤트 업데이트 (캘린더 날짜는 유지됨)
      api.removeAllEvents()
      api.addEventSource(calendarEvents.value)
    }
  } catch (e) {
    error.value = '일정을 불러오는데 실패했습니다.'
    console.error('❌ API 호출 실패:', e)
  } finally {
    loading.value = false
  }
}

// 월 이동 핸들러
const handleDatesSet = (info) => {
  const newDate = info.view.currentStart
  const month = formatMonth(newDate)
  
  console.log('📅 handleDatesSet 호출:', {
    newMonth: month,
    lastFetchedMonth: lastFetchedMonth.value,
    needsFetch: lastFetchedMonth.value !== month,
    type: info.view.type
  })
  
  currentDate.value = newDate
  
  // 초기 로드 또는 사용자가 명시적으로 월을 변경한 경우만 API 호출
  // datesSet은 데이터 변경 시에도 발생하므로 주의!
  if (lastFetchedMonth.value !== month) {
    console.log('🔄 새로운 월 감지, fetchSchedules 호출')
    fetchSchedules(month)
  } else {
    console.log('⏭️ 같은 월, fetchSchedules 스킵')
  }
}

// 일정 클릭 동작
const handleEventClick = (info) => {
  const scheduleId = Number(info.event.id)
  const schedule = scheduleStore.schedules.find(s => s.scheduleId === scheduleId)
  if (schedule) {
    selectedSchedule.value = schedule
    showDetailModal.value = true
  }
}

// 날짜 클릭 - 새 일정 추가
const handleDateClick = (info) => {
  editingSchedule.value = null
  // 클릭한 날짜를 YYYY-MM-DD 형식으로 저장
  if (info.dateStr) {
    initialDate.value = info.dateStr
  } else {
    // dateStr이 없으면 date 객체에서 추출
    const date = info.date
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    initialDate.value = `${year}-${month}-${day}`
  }
  showFormModal.value = true
}

// 일정 추가 버튼
const openCreateModal = () => {
  editingSchedule.value = null
  initialDate.value = null // 버튼으로 열 때는 날짜 초기화
  showFormModal.value = true
}

// 오늘 버튼
const goToday = () => {
  console.log('🏠 오늘 버튼 클릭')
  if (calendarRef.value) {
    const api = calendarRef.value.getApi()
    api.today()
    // FullCalendar의 today() 호출 후 datesSet이 자동 발생
  }
}

// 이전 월
const prevMonth = () => {
  console.log('⬅️ 이전 월 버튼 클릭')
  if (calendarRef.value) {
    const api = calendarRef.value.getApi()
    api.prev()
    // FullCalendar의 prev() 호출 후 datesSet이 자동 발생
  }
}

// 다음 월
const nextMonth = () => {
  console.log('➡️ 다음 월 버튼 클릭')
  if (calendarRef.value) {
    const api = calendarRef.value.getApi()
    api.next()
    // FullCalendar의 next() 호출 후 datesSet이 자동 발생
  }
}

// 상세 모달에서 수정 요청
const handleEdit = (schedule) => {
  showDetailModal.value = false
  editingSchedule.value = schedule
  showFormModal.value = true
}

// 상세 모달에서 삭제 요청
const handleDelete = async (scheduleId) => {
  showDetailModal.value = false
  const month = formatMonth(currentDate.value)
  await Promise.all([
    fetchSchedules(month),
    scheduleStore.fetchSummary(month)
  ])
}

// 폼 모달에서 저장 완료
const handleSaved = async () => {
  showFormModal.value = false
  initialDate.value = null // 모달 닫을 때 초기화
  const month = formatMonth(currentDate.value)
  await Promise.all([
    fetchSchedules(month),
    scheduleStore.fetchSummary(month)
  ])
}


// 초기 로드
onMounted(() => {
  fetchSchedules(formatMonth(currentDate.value))
  
  // 장소 추천에서 넘어온 경우 자동으로 일정 생성 모달 열기
  if (scheduleStore.prefilledPlace) {
    editingSchedule.value = null
    showFormModal.value = true
  }
})

// 워케이션 추천 화면으로 이동
const goToRecommend = () => {
  router.push('/recommend')
}

// 모바일에서 좌측 패널 토글
const mobileMenuOpen = ref(false)
const toggleSidebar = () => {
  mobileMenuOpen.value = !mobileMenuOpen.value
}

</script>

<template>
  <div class="min-h-screen bg-white">
    <!-- 메인 컨텐츠 -->
    <div class="w-full px-4 lg:px-8 py-6 lg:py-8">
      <!-- 페이지 헤더 -->
      <div class="mb-6">
        <h1 class="text-2xl lg:text-3xl font-bold text-gray-900">일정 관리</h1>
        <p class="mt-1 text-sm text-gray-600">워케이션을 위한 스마트한 일정 분석</p>
      </div>

      <div class="flex flex-col lg:flex-row gap-6">
        <!-- 좌측 패널 (데스크톱: 일반 레이아웃, 모바일: 숨김) -->
        <aside class="hidden lg:block w-64 flex-shrink-0 space-y-4">
          <!-- 현재 월 선택 -->
          <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-4">
            <div class="flex items-center justify-between mb-4">
              <div class="flex items-center gap-2">
                <svg class="w-5 h-5 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
                <h3 class="text-sm font-semibold text-gray-900">{{ currentYearMonth }}</h3>
              </div>
              <button 
                @click="openMonthPicker"
                class="p-1.5 hover:bg-gray-100 rounded-lg transition-colors"
                title="월 선택"
              >
                <svg class="w-4 h-4 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
              </button>
            </div>
            
            <!-- 작은 달력 -->
            <div class="space-y-1">
              <!-- 요일 헤더 -->
              <div class="grid grid-cols-7 gap-1 mb-1">
                <div 
                  v-for="(day, index) in weekDays" 
                  :key="index"
                  class="text-center text-xs font-medium"
                  :class="index === 0 ? 'text-red-500' : index === 6 ? 'text-blue-500' : 'text-gray-500'"
                >
                  {{ day }}
                </div>
              </div>
              
              <!-- 날짜 그리드 -->
              <div 
                v-for="(week, weekIndex) in miniCalendar" 
                :key="weekIndex"
                class="grid grid-cols-7 gap-1"
              >
                <div
                  v-for="(day, dayIndex) in week"
                  :key="dayIndex"
                  class="aspect-square flex items-center justify-center text-xs"
                  :class="[
                    !day ? 'text-transparent' : '',
                    isToday(day) 
                      ? 'bg-indigo-600 text-white rounded-full font-bold' 
                      : day 
                        ? 'text-gray-700 hover:bg-gray-100 rounded cursor-pointer' 
                        : ''
                  ]"
                >
                  {{ day }}
                </div>
              </div>
            </div>
          </div>

          <!-- CTA 버튼 -->
          <button
            @click="goToRecommend"
            class="w-full bg-slate-800 text-white rounded-lg px-4 py-3 font-semibold text-sm hover:bg-slate-900 transition-all shadow-sm"
          >
            <div class="flex items-center justify-center gap-2">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
              </svg>
              <span>워케이션 분석</span>
            </div>
          </button>

          <!-- 일정 추가 버튼 -->
          <button
            @click="openCreateModal"
            class="w-full bg-white border border-gray-300 text-gray-700 rounded-lg px-4 py-3 font-semibold text-sm hover:bg-gray-50 transition-all shadow-sm"
          >
            <div class="flex items-center justify-center gap-2">
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
              </svg>
              <span>일정 추가</span>
            </div>
          </button>

          <!-- 일정 분석 상세 리포트 -->
          <ScheduleAnalysisDetail :month="formatMonth(currentDate)" />
        </aside>

        <!-- 메인 캘린더 영역 -->
        <main class="flex-1 min-w-0 space-y-6">
          <div class="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
            <!-- 캘린더 상단 컨트롤 -->
            <div class="bg-gray-50 px-6 py-4 border-b border-gray-200">
              <div class="flex items-center justify-between">
                <button 
                  @click="openMonthPicker"
                  class="text-xl lg:text-2xl font-bold text-gray-900 hover:text-indigo-600 transition-colors flex items-center gap-2 group"
                >
                  {{ currentYearMonth }}
                  <svg class="w-5 h-5 text-gray-400 group-hover:text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                  </svg>
                </button>
                <div class="flex items-center gap-2">
                  <button
                    @click="prevMonth"
                    class="p-2 rounded-lg hover:bg-white hover:shadow-sm transition-all"
                    title="이전 달"
                  >
                    <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
                    </svg>
                  </button>
                  <button
                    @click="goToday"
                    class="px-4 py-2 text-sm font-semibold text-gray-700 bg-white rounded-lg hover:bg-gray-50 transition-all shadow-sm"
                  >
                    오늘
                  </button>
                  <button
                    @click="nextMonth"
                    class="p-2 rounded-lg hover:bg-white hover:shadow-sm transition-all"
                    title="다음 달"
                  >
                    <svg class="w-5 h-5 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                    </svg>
                  </button>
                </div>
              </div>
            </div>

            <!-- 일정 유형 필터 (캘린더 위 여백) -->
            <div class="px-6 py-3 bg-white">
              <div class="flex items-center justify-end">
                <div class="flex items-center gap-3">
                  <label 
                    v-for="type in scheduleTypes" 
                    :key="type.value"
                    class="flex items-center gap-2 cursor-pointer group"
                  >
                    <input 
                      type="checkbox" 
                      :value="type.value"
                      v-model="selectedTypes"
                      class="w-4 h-4 rounded border-gray-300 text-blue-600 focus:ring-blue-500 cursor-pointer"
                    />
                    <div class="flex items-center gap-1.5">
                      <div class="w-3 h-3 rounded-full" :style="{ backgroundColor: type.color }"></div>
                      <span class="text-sm font-medium text-gray-700 group-hover:text-gray-900">{{ type.label }}</span>
                    </div>
                  </label>
                </div>
              </div>
            </div>

            <!-- 에러 메시지 -->
            <div v-if="error" class="mx-6 mt-6 p-4 bg-red-50 border-l-4 border-red-500 rounded text-red-800 text-sm shadow-sm">
              <div class="flex items-center gap-2">
                <svg class="w-5 h-5 flex-shrink-0" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd" />
                </svg>
                <span class="font-medium">{{ error }}</span>
              </div>
            </div>

            <!-- 로딩 상태 -->
            <div v-if="loading" class="p-12 text-center">
              <div class="inline-block animate-spin rounded-full h-12 w-12 border-4 border-gray-200 border-t-blue-600"></div>
              <p class="mt-4 text-sm font-medium text-gray-600">일정을 불러오는 중...</p>
            </div>

            <!-- FullCalendar -->
            <div v-else class="p-6">
              <FullCalendar ref="calendarRef" :options="calendarOptions" />
            </div>
          </div>

          <!-- 일정 분석 요약 (캘린더 하단) -->
          <ScheduleAnalysisSummary :month="formatMonth(currentDate)" />
        </main>
      </div>
    </div>

    <!-- Mobile Menu Button -->
    <button
      @click="toggleSidebar"
      class="lg:hidden fixed bottom-8 right-8 w-16 h-16 bg-slate-800 text-white rounded-full shadow-2xl flex items-center justify-center z-30 hover:bg-slate-900 transition-all"
    >
      <svg class="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4"/>
      </svg>
    </button>

    <!-- Mobile Drawer -->
    <div 
      v-if="mobileMenuOpen" 
      class="fixed inset-0 bg-black bg-opacity-50 z-40 lg:hidden"
      @click="mobileMenuOpen = false"
    ></div>
    <div 
      :class="[
        'fixed top-0 left-0 bottom-0 w-80 bg-white shadow-2xl transform transition-transform duration-300 z-50 lg:hidden overflow-y-auto',
        mobileMenuOpen ? 'translate-x-0' : '-translate-x-full'
      ]"
    >
      <div class="p-8 border-b border-gray-200 flex items-center justify-between">
        <h2 class="text-xl font-bold text-gray-900">필터 및 분석</h2>
        <button 
          @click="mobileMenuOpen = false"
          class="p-2 rounded-lg hover:bg-gray-100 transition-colors"
        >
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
          </svg>
        </button>
      </div>
      <div class="p-6 space-y-6">
        <!-- 현재 월 선택 -->
        <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-5">
          <div class="flex items-center justify-between mb-4">
            <div class="flex items-center gap-2">
              <svg class="w-5 h-5 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
              <h3 class="text-sm font-semibold text-gray-900">{{ currentYearMonth }}</h3>
            </div>
            <button 
              @click="openMonthPicker"
              class="p-1.5 hover:bg-gray-100 rounded-lg transition-colors"
              title="월 선택"
            >
              <svg class="w-4 h-4 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
            </button>
          </div>
          
          <!-- 작은 달력 -->
          <div class="space-y-1">
            <!-- 요일 헤더 -->
            <div class="grid grid-cols-7 gap-1 mb-1">
              <div 
                v-for="(day, index) in weekDays" 
                :key="index"
                class="text-center text-xs font-medium"
                :class="index === 0 ? 'text-red-500' : index === 6 ? 'text-blue-500' : 'text-gray-500'"
              >
                {{ day }}
              </div>
            </div>
            
            <!-- 날짜 그리드 -->
            <div 
              v-for="(week, weekIndex) in miniCalendar" 
              :key="weekIndex"
              class="grid grid-cols-7 gap-1"
            >
              <div
                v-for="(day, dayIndex) in week"
                :key="dayIndex"
                class="aspect-square flex items-center justify-center text-xs"
                :class="[
                  !day ? 'text-transparent' : '',
                  isToday(day) 
                    ? 'bg-indigo-600 text-white rounded-full font-bold' 
                    : day 
                      ? 'text-gray-700 hover:bg-gray-100 rounded cursor-pointer' 
                      : ''
                ]"
              >
                {{ day }}
              </div>
            </div>
          </div>
        </div>


        <!-- CTA 버튼 -->
        <button
          @click="goToRecommend"
          class="w-full bg-slate-800 text-white rounded-lg px-5 py-4 font-semibold text-sm hover:bg-slate-900 transition-all shadow-sm"
        >
          <div class="flex items-center justify-center gap-2">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
            </svg>
            <span>워케이션 가능 기간 분석</span>
          </div>
        </button>

        <!-- 일정 추가 버튼 -->
        <button
          @click="openCreateModal"
          class="w-full bg-white border border-gray-300 text-gray-700 rounded-lg px-5 py-4 font-semibold text-sm hover:bg-gray-50 transition-all shadow-sm"
        >
          <div class="flex items-center justify-center gap-2">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
            <span>일정 추가</span>
          </div>
        </button>

        <!-- 일정 분석 요약 (모바일) -->
        <div class="lg:hidden">
          <ScheduleAnalysisSummary :month="formatMonth(currentDate)" />
        </div>
      </div>
    </div>

    <!-- 일정 등록/수정 모달 -->
    <ScheduleFormModal
      v-if="showFormModal"
      :schedule="editingSchedule"
      :initialDate="initialDate"
      @close="showFormModal = false; initialDate = null"
      @saved="handleSaved"
    />

    <!-- 일정 상세 모달 -->
    <ScheduleDetailModal
      v-if="showDetailModal"
      :schedule="selectedSchedule"
      @close="showDetailModal = false"
      @edit="handleEdit"
      @deleted="handleDelete"
    />

    <!-- 월 선택 모달 -->
    <div v-if="showMonthPicker" class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50" @click.self="showMonthPicker = false">
      <div class="bg-white rounded-2xl shadow-2xl w-full max-w-lg mx-4 overflow-hidden" @click.stop>
        <!-- 모달 헤더 -->
        <div class="bg-gradient-to-r from-slate-800 to-slate-900 px-6 py-5">
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-2">
              <svg class="w-5 h-5 text-indigo-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
              <h3 class="text-xl font-bold text-white">월 선택</h3>
            </div>
            <button @click="showMonthPicker = false" class="text-white/80 hover:text-white transition-colors p-1">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
        </div>

        <!-- 모달 바디 -->
        <div class="p-8 space-y-6">
          <!-- 년도 선택 (화살표 + 중앙 연도) -->
          <div>
            <div class="flex items-center justify-center gap-4">
              <button
                @click="decreaseYear"
                class="p-2 hover:bg-gray-100 rounded-lg transition-colors"
                title="이전 년도"
              >
                <svg class="w-6 h-6 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
                </svg>
              </button>
              <div class="text-2xl font-bold text-gray-900 min-w-[120px] text-center">
                {{ selectedYear }}년
              </div>
              <button
                @click="increaseYear"
                class="p-2 hover:bg-gray-100 rounded-lg transition-colors"
                title="다음 년도"
              >
                <svg class="w-6 h-6 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                </svg>
              </button>
            </div>
          </div>

          <!-- 월 선택 (4 * 3 그리드) -->
          <div>
            <div class="grid grid-cols-4 gap-3">
              <button
                v-for="month in months"
                :key="month"
                @click="selectedMonth = month"
                :class="[
                  'py-3 text-sm font-semibold rounded-lg transition-all',
                  selectedMonth === month
                    ? 'bg-indigo-600 text-white shadow-md'
                    : 'bg-gray-50 text-gray-700 hover:bg-gray-100 hover:text-gray-900'
                ]"
              >
                {{ month }}월
              </button>
            </div>
          </div>
        </div>

        <!-- 모달 푸터 -->
        <div class="bg-gray-50 px-6 py-4 flex items-center justify-end gap-2">
          <button
            @click="showMonthPicker = false"
            class="px-4 py-2 text-sm font-semibold text-gray-700 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 hover:border-gray-400 transition-all"
          >
            취소
          </button>
          <button
            @click="applyMonthSelection"
            class="px-4 py-2 text-sm font-semibold text-white bg-indigo-600 rounded-lg hover:bg-indigo-700 transition-all shadow-sm"
          >
            이동
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* FullCalendar 커스텀 스타일 - 깔끔한 디자인 */
:deep(.fc) {
  font-family: inherit;
}

:deep(.fc-daygrid-day) {
  cursor: pointer;
  transition: background-color 0.15s ease;
}

:deep(.fc-daygrid-day:hover) {
  background-color: #f9fafb;
}

:deep(.fc-daygrid-day-number) {
  color: #111827;
  font-weight: 600;
  padding: 8px;
  font-size: 0.875rem;
  min-width: 28px;
}

:deep(.fc-col-header-cell) {
  background-color: #f9fafb;
  border-color: #e5e7eb;
  padding: 14px 8px;
  font-weight: 700;
  text-transform: uppercase;
  font-size: 0.7rem;
  letter-spacing: 0.05em;
  color: #6b7280;
}

:deep(.fc-daygrid-day-frame) {
  min-height: 100px;
}

:deep(.fc-event) {
  cursor: pointer;
  border-radius: 6px;
  padding: 5px 8px;
  margin: 2px;
  font-size: 0.85rem;
  font-weight: 600;
  border-width: 2px !important;
  border-left-width: 4px !important;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  transition: all 0.15s ease;
}

/* 일정 유형별 색상 강제 적용 */
:deep(.fc-event.schedule-type-personal) {
  background-color: #fffbeb !important;
  border-color: #fde68a !important;
  border-left-color: #fde68a !important;
}

:deep(.fc-event.schedule-type-work) {
  background-color: #fef2f2 !important;
  border-color: #fca5a5 !important;
  border-left-color: #fca5a5 !important;
}

:deep(.fc-event.schedule-type-workation) {
  background-color: #eff6ff !important;
  border-color: #93c5fd !important;
  border-left-color: #93c5fd !important;
}

:deep(.fc-event:hover) {
  transform: translateY(-1px);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.12);
}

:deep(.fc-daygrid-event-dot) {
  display: none;
}

:deep(.fc-daygrid-day-top) {
  flex-direction: row;
  justify-content: center;
  padding-top: 4px;
}

:deep(.fc-day-today) {
  background-color: #eef2ff !important;
}

:deep(.fc-day-today .fc-daygrid-day-number) {
  background: linear-gradient(135deg, #4f46e5 0%, #4338ca 100%);
  color: white;
  border-radius: 50%;
  width: 32px;
  height: 32px;
  min-width: 32px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 6px rgba(79, 70, 229, 0.3);
  font-weight: 700;
  padding: 0;
}

:deep(.fc-scrollgrid) {
  border-color: #e5e7eb !important;
  border-radius: 8px;
  overflow: hidden;
}

:deep(.fc-scrollgrid td),
:deep(.fc-scrollgrid th) {
  border-color: #e5e7eb;
}

:deep(.fc-theme-standard td),
:deep(.fc-theme-standard th) {
  border-color: #e5e7eb;
}

/* 주말 스타일 */
:deep(.fc-day-sat .fc-daygrid-day-number) {
  color: #4f46e5;
}

:deep(.fc-day-sun .fc-daygrid-day-number) {
  color: #dc2626;
}

/* 다른 달 날짜 */
:deep(.fc-day-other .fc-daygrid-day-number) {
  color: #d1d5db;
  font-weight: 500;
}
</style>
