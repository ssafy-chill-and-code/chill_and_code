<template>
  <div class="place-select-wrapper">
    <div class="container py-5">
      <!-- 헤더 -->
      <header class="text-center mb-5">
        <button class="back-link" @click="goBack">
          <span>←</span> 이전 단계로
        </button>
        <div class="header-content">
          <div class="step-indicator">STEP 3 of 3</div>
          <h1 class="page-title">당신의 선호도를 알려주세요</h1>
          <p class="page-subtitle">{{ periodText }}의 워케이션을 위한 옵션을 선택하세요</p>
        </div>
      </header>

      <!-- 폼 래퍼 -->
      <div class="form-container">
        <!-- 워케이션 스타일 -->
        <section class="form-section">
          <div class="section-header">
            <h3 class="section-title">워케이션 스타일 <span class="required">*</span></h3>
          </div>
          <div class="style-grid">
            <button 
              type="button" 
              class="style-option" 
              :class="{ 'selected': selectedStyle === '힐링' }" 
              @click="selectStyle('힐링')"
            >
              <div class="option-check">
                <svg v-if="selectedStyle === '힐링'" class="check-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7" />
                </svg>
              </div>
              <div class="option-emoji">🌿</div>
              <div class="option-title">힐링</div>
              <div class="option-desc">자연 속에서 마음을 편안하게</div>
            </button>
            <button 
              type="button" 
              class="style-option" 
              :class="{ 'selected': selectedStyle === '작업몰입' }" 
              @click="selectStyle('작업몰입')"
            >
              <div class="option-check">
                <svg v-if="selectedStyle === '작업몰입'" class="check-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7" />
                </svg>
              </div>
              <div class="option-emoji">💻</div>
              <div class="option-title">작업몰입</div>
              <div class="option-desc">집중력 있는 업무 환경</div>
            </button>
            <button 
              type="button" 
              class="style-option" 
              :class="{ 'selected': selectedStyle === '액티비티' }" 
              @click="selectStyle('액티비티')"
            >
              <div class="option-check">
                <svg v-if="selectedStyle === '액티비티'" class="check-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7" />
                </svg>
              </div>
              <div class="option-emoji">🏃‍♂️</div>
              <div class="option-title">액티비티</div>
              <div class="option-desc">다양한 활동과 경험</div>
            </button>
          </div>
        </section>

        <!-- 월간 예산 -->
        <section class="form-section">
          <div class="section-header">
            <h3 class="section-title">💰 월간 예산 <span class="required">*</span></h3>
          </div>
          <div class="budget-grid">
            <button 
              type="button" 
              class="budget-option" 
              :class="{ 'selected': budget === '가성비' }" 
              @click="selectBudget('가성비')"
            >
              <div class="option-check">
                <svg v-if="budget === '가성비'" class="check-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7" />
                </svg>
              </div>
              <div class="option-emoji">💰</div>
              <div class="option-title">가성비</div>
              <div class="option-desc">합리적인 가격대</div>
            </button>
            <button 
              type="button" 
              class="budget-option" 
              :class="{ 'selected': budget === '적당한' }" 
              @click="selectBudget('적당한')"
            >
              <div class="option-check">
                <svg v-if="budget === '적당한'" class="check-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7" />
                </svg>
              </div>
              <div class="option-emoji">💵</div>
              <div class="option-title">적당한</div>
              <div class="option-desc">균형잡힌 가격대</div>
            </button>
            <button 
              type="button" 
              class="budget-option" 
              :class="{ 'selected': budget === '프리미엄' }" 
              @click="selectBudget('프리미엄')"
            >
              <div class="option-check">
                <svg v-if="budget === '프리미엄'" class="check-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M5 13l4 4L19 7" />
                </svg>
              </div>
              <div class="option-emoji">💎</div>
              <div class="option-title">프리미엄</div>
              <div class="option-desc">고급스러운 경험</div>
            </button>
          </div>
        </section>

        <!-- 이동수단 -->
        <section class="form-section">
          <div class="section-header">
            <h3 class="section-title">이동수단 <span class="optional">(선택)</span></h3>
          </div>
          <select v-model="transport" class="form-select-custom">
            <option value="">선호하는 이동수단을 선택하세요</option>
            <option value="자동차">🚗 자동차</option>
            <option value="기차">🚄 기차</option>
            <option value="비행기">✈️ 비행기</option>
            <option value="버스">🚌 버스</option>
          </select>
        </section>

        <!-- 선호 지역 -->
        <section class="form-section">
          <div class="section-header">
            <h3 class="section-title">선호 지역 <span class="optional">(선택)</span></h3>
            <p class="section-hint">최대 3개까지 선택 가능합니다</p>
          </div>
          <div class="region-map-container">
            <div class="region-selected-tags" v-if="selectedRegions.length > 0">
              <span 
                v-for="region in selectedRegions" 
                :key="region" 
                class="selected-tag"
              >
                {{ region }}
                <button 
                  type="button" 
                  class="remove-tag"
                  @click="toggleRegion(region)"
                >
                  ×
                </button>
              </span>
            </div>
            <div class="map-wrapper">
              <div ref="svgContainer" class="korea-map-container"></div>
              
              <!-- 호버 시 지역명 표시 -->
              <div 
                v-if="hoveredRegion" 
                class="region-tooltip"
                :style="{ left: tooltipPosition.x + 'px', top: tooltipPosition.y + 'px' }"
              >
                {{ hoveredRegion }}
              </div>
            </div>
          </div>
        </section>

        <!-- 에러 메시지 -->
        <div v-if="errorMessage" class="error-alert">
          <svg class="error-icon-svg" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          {{ errorMessage }}
        </div>

        <!-- 제출 버튼 -->
        <div class="submit-section">
          <button 
            class="btn-submit" 
            @click="goResult" 
            :disabled="placeStore.loading"
          >
            <span v-if="placeStore.loading" class="flex items-center gap-2">
              <svg class="animate-spin h-5 w-5" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              추천 중...
            </span>
            <span v-else>장소 추천받기</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { usePlaceRecommendationStore } from '@/stores/placeRecommendation'
import { useRecommendationStore } from '@/stores/recommendation'

const router = useRouter()
const placeStore = usePlaceRecommendationStore()
const recommendationStore = useRecommendationStore()

const selectedStyle = ref('')
const budget = ref(150)
const transport = ref('')
const selectedRegions = ref([])
const hoveredRegion = ref(null)
const tooltipPosition = ref({ x: 0, y: 0 })
const errorMessage = ref('')

// 지역 ID를 한국어로 매핑
const regionIdToName = {
  'KR-11': '서울특별시',
  'KR-26': '부산광역시',
  'KR-27': '대구광역시',
  'KR-28': '인천광역시',
  'KR-29': '광주광역시',
  'KR-30': '대전광역시',
  'KR-31': '울산광역시',
  'KR-41': '경기도',
  'KR-42': '강원도',
  'KR-43': '충청북도',
  'KR-44': '충청남도',
  'KR-45': '전라북도',
  'KR-46': '전라남도',
  'KR-47': '경상북도',
  'KR-48': '경상남도',
  'KR-49': '제주특별자치도',
  'KR-50': '세종특별자치시'
}

// 한국어를 표시용 짧은 이름으로 변환
const getDisplayName = (fullName) => {
  const shortNames = {
    '서울특별시': '서울',
    '부산광역시': '부산',
    '대구광역시': '대구',
    '인천광역시': '인천',
    '광주광역시': '광주',
    '대전광역시': '대전',
    '울산광역시': '울산',
    '경기도': '경기',
    '강원도': '강원',
    '강원특별자치도': '강원',
    '충청북도': '충북',
    '충청남도': '충남',
    '전라북도': '전북',
    '전북특별자치도': '전북',
    '전라남도': '전남',
    '경상북도': '경북',
    '경상남도': '경남',
    '제주특별자치도': '제주',
    '세종특별자치시': '세종'
  }
  return shortNames[fullName] || fullName
}

// SVG path 클릭 핸들러
const handleRegionClick = (event, regionId) => {
  const regionName = regionIdToName[regionId]
  if (regionName) {
    toggleRegion(regionName)
  }
}

// SVG path 호버 핸들러
const handleRegionHover = (event, regionId, isHover) => {
  const path = event?.currentTarget || event?.target
  if (!path || path.tagName !== 'path') {
    console.warn('handleRegionHover: path element not found', { event, regionId, isHover })
    return
  }
  
  if (isHover) {
    const regionName = regionIdToName[regionId]
    hoveredRegion.value = getDisplayName(regionName)
    
    // SVG에서 z-index가 작동하지 않으므로, hover된 path를 DOM 맨 뒤로 이동
    // 이렇게 하면 실제로 다른 path 위에 렌더링됨
    const svgElement = path.closest('svg')
    const parentNode = path.parentNode
    
    if (svgElement && parentNode) {
      // hover 클래스 추가
      path.classList.add('region-hover')
      
      // SVG 인라인 속성 제거 (CSS가 우선하도록)
      if (path.hasAttribute('fill')) {
        path.removeAttribute('fill')
      }
      if (path.hasAttribute('stroke')) {
        path.removeAttribute('stroke')
      }
      if (path.hasAttribute('stroke-width')) {
        path.removeAttribute('stroke-width')
      }
      
      // JavaScript로 직접 스타일 적용 (SVG 인라인 속성보다 우선)
      // 선택된 지역인 경우 다른 색상 사용
      const isSelected = path.classList.contains('region-active')
      
      if (isSelected) {
        // 선택된 지역 + hover: 보라색 계열
        path.style.fill = '#4f46e5'
        path.style.stroke = '#4338ca'
        path.style.strokeWidth = '4'
        path.style.filter = 'drop-shadow(0 8px 24px rgba(99, 102, 241, 0.7))'
      } else {
        // 일반 hover: 포인트 컬러 (월간 예산 선택 색상과 동일)
        path.style.fill = '#6366f1'
        path.style.stroke = '#4f46e5'
        path.style.strokeWidth = '3.5'
        path.style.filter = 'drop-shadow(0 8px 24px rgba(99, 102, 241, 0.6))'
      }
      
      path.style.transform = 'scale(1.04) translateY(-3px)'
      path.style.transformOrigin = 'center'
      path.style.transformBox = 'fill-box'
      
      // DOM 순서상 맨 뒤로 이동 (나중에 그려진 것이 위에 표시됨)
      // appendChild는 이미 자식인 경우 자동으로 이동시킴
      try {
        parentNode.appendChild(path)
      } catch (e) {
        console.error('Failed to move path to end:', e)
      }
    }
    
    // 마우스 위치에 따라 툴팁 위치 설정
    if (event) {
      const mapWrapper = path.closest('.map-wrapper')
      if (mapWrapper) {
        const rect = mapWrapper.getBoundingClientRect()
        tooltipPosition.value = {
          x: event.clientX - rect.left,
          y: event.clientY - rect.top - 40
        }
      }
    }
  } else {
    hoveredRegion.value = null
    // hover 클래스 제거
    if (path) {
      path.classList.remove('region-hover')
      
      // 인라인 속성 제거
      if (path.hasAttribute('fill')) {
        path.removeAttribute('fill')
      }
      if (path.hasAttribute('stroke')) {
        path.removeAttribute('stroke')
      }
      if (path.hasAttribute('stroke-width')) {
        path.removeAttribute('stroke-width')
      }
      
      // JavaScript로 기본 스타일 복원
      path.style.fill = ''
      path.style.stroke = ''
      path.style.strokeWidth = ''
      path.style.transform = ''
      path.style.filter = ''
      
      // 선택된 지역인 경우 선택 스타일 유지
      if (path.classList.contains('region-active')) {
        path.style.fill = '#6366f1'
        path.style.stroke = '#4f46e5'
        path.style.strokeWidth = '3'
        path.style.filter = 'drop-shadow(0 4px 12px rgba(99, 102, 241, 0.4))'
      }
    }
  }
}

// SVG 컨테이너 ref
const svgContainer = ref(null)

// SVG 로드 및 이벤트 연결
onMounted(async () => {
  try {
    const response = await fetch('/southKoreaHigh.svg')
    
    if (!response.ok) {
      throw new Error(`Failed to fetch SVG: ${response.status} ${response.statusText}`)
    }
    
    const svgText = await response.text()
    
    if (!svgContainer.value) {
      console.error('SVG container element not found')
      return
    }
    
    svgContainer.value.innerHTML = svgText
    
    // SVG 요소 찾기
    const svgElement = svgContainer.value.querySelector('svg')
    if (!svgElement) {
      console.error('SVG element not found')
      return
    }
    
    // viewBox가 없으면 경로들의 bounding box를 계산해서 설정
    if (!svgElement.getAttribute('viewBox')) {
      const paths = svgElement.querySelectorAll('path')
      if (paths.length > 0) {
        let minX = Infinity, minY = Infinity, maxX = -Infinity, maxY = -Infinity
        
        paths.forEach(path => {
          const bbox = path.getBBox()
          minX = Math.min(minX, bbox.x)
          minY = Math.min(minY, bbox.y)
          maxX = Math.max(maxX, bbox.x + bbox.width)
          maxY = Math.max(maxY, bbox.y + bbox.height)
        })
        
        // 여백 추가
        const padding = 20
        svgElement.setAttribute('viewBox', `${minX - padding} ${minY - padding} ${maxX - minX + padding * 2} ${maxY - minY + padding * 2}`)
        svgElement.setAttribute('preserveAspectRatio', 'xMidYMid meet')
        console.log(`Set viewBox: ${svgElement.getAttribute('viewBox')}`)
      } else {
        // 경로가 없으면 기본값 설정
        svgElement.setAttribute('viewBox', '0 0 500 700')
        svgElement.setAttribute('preserveAspectRatio', 'xMidYMid meet')
      }
    }
    
    // 모든 path 요소 찾기
    const paths = svgContainer.value.querySelectorAll('path[id^="KR-"]')
    
    if (paths.length === 0) {
      console.error('No region paths found in SVG')
      return
    }
    
    console.log(`Loaded ${paths.length} regions from SVG`)
    
    paths.forEach(path => {
      const regionId = path.id
      
      if (!regionId) {
        console.warn('Path element without ID found')
        return
      }
      
      // SVG path에 기본 스타일 속성 설정 (CSS가 우선하도록 인라인 속성은 제거)
      // CSS에서 fill, stroke, stroke-width를 관리하므로 인라인 속성은 설정하지 않음
      // 단, SVG 원본에 이미 속성이 있다면 제거
      if (path.hasAttribute('fill')) {
        path.removeAttribute('fill')
      }
      if (path.hasAttribute('stroke')) {
        path.removeAttribute('stroke')
      }
      if (path.hasAttribute('stroke-width')) {
        path.removeAttribute('stroke-width')
      }
      
      // 클릭 이벤트
      path.addEventListener('click', (e) => handleRegionClick(e, regionId))
      
      // 호버 이벤트
      path.addEventListener('mouseenter', (e) => handleRegionHover(e, regionId, true))
      path.addEventListener('mouseleave', (e) => handleRegionHover(e, regionId, false))
      path.addEventListener('mousemove', (e) => {
        if (hoveredRegion.value) {
          const mapWrapper = e.currentTarget.closest('.map-wrapper')
          if (mapWrapper) {
            const rect = mapWrapper.getBoundingClientRect()
            tooltipPosition.value = {
              x: e.clientX - rect.left,
              y: e.clientY - rect.top - 40
            }
          }
        }
      })
      
      // 포인터 스타일 개선
      path.style.pointerEvents = 'all'
      
      // 선택 상태에 따라 클래스 및 스타일 적용
      const regionName = regionIdToName[regionId]
      if (regionName && selectedRegions.value.includes(regionName)) {
        path.classList.add('region-active')
        // 선택된 지역 스타일 적용
        path.style.fill = '#6366f1'
        path.style.stroke = '#4f46e5'
        path.style.strokeWidth = '3'
        path.style.filter = 'drop-shadow(0 4px 12px rgba(99, 102, 241, 0.4))'
      } else {
        // 기본 스타일 (CSS가 적용되도록 인라인 스타일 제거)
        path.style.fill = ''
        path.style.stroke = ''
        path.style.strokeWidth = ''
        path.style.filter = ''
      }
    })
  } catch (error) {
    console.error('Failed to load Korea map:', error)
    errorMessage.value = '지도를 불러오는데 실패했습니다. 페이지를 새로고침해주세요.'
  }
})

// 선택 상태 변경 시 SVG 업데이트
watch(selectedRegions, () => {
  if (svgContainer.value) {
    const paths = svgContainer.value.querySelectorAll('path[id^="KR-"]')
    paths.forEach(path => {
      const regionName = regionIdToName[path.id]
      // hover 상태가 아닐 때만 스타일 업데이트
      const isHovered = path.classList.contains('region-hover')
      
      if (regionName && selectedRegions.value.includes(regionName)) {
        path.classList.add('region-active')
        // hover 상태가 아니면 선택 스타일 적용
        if (!isHovered) {
          path.style.fill = '#6366f1'
          path.style.stroke = '#4f46e5'
          path.style.strokeWidth = '3'
          path.style.filter = 'drop-shadow(0 4px 12px rgba(99, 102, 241, 0.4))'
        }
      } else {
        path.classList.remove('region-active')
        // hover 상태가 아니면 기본 스타일로 복원
        if (!isHovered) {
          path.style.fill = ''
          path.style.stroke = ''
          path.style.strokeWidth = ''
          path.style.filter = ''
          path.style.transform = ''
        }
      }
    })
  }
})

const regions = [
  '경기도',
  '강원도',
  '강원특별자치도',
  '경상남도',
  '경상북도',
  '광주광역시',
  '대구광역시',
  '대전광역시',
  '부산광역시',
  '서울특별시',
  '울산광역시',
  '인천광역시',
  '전라남도',
  '전라북도',
  '전북특별자치도',
  '제주특별자치도',
  '충청남도',
  '충청북도'
]

const selectedPeriod = computed(() => recommendationStore.selection.selectedPeriod)
const periodText = computed(() => {
  if (!selectedPeriod.value) return '기간 미선택'
  return `${formatDate(selectedPeriod.value.startDate)} ~ ${formatDate(selectedPeriod.value.endDate)}`
})

function formatDate(dateString) {
  if (!dateString) return ''
  const date = new Date(dateString)
  const month = date.getMonth() + 1
  const day = date.getDate()
  return `${month}월 ${day}일`
}

function selectStyle(style) {
  selectedStyle.value = style
}

function toggleRegion(region) {
  const index = selectedRegions.value.indexOf(region)
  if (index > -1) {
    selectedRegions.value.splice(index, 1)
  } else {
    selectedRegions.value.push(region)
  }
}

function convertStyleToBackend(style) {
  const styleMap = {
    '힐링': 'NATURE',
    '작업몰입': 'CAFE',
    '액티비티': 'ACTIVITY'
  }
  return styleMap[style] || 'ACTIVITY'
}

function convertBudgetToBackend(budgetValue) {
  if (budgetValue <= 150) return 'LOW'
  if (budgetValue <= 300) return 'MID'
  return 'HIGH'
}

async function goResult() {
  if (!selectedStyle.value) {
    errorMessage.value = '워케이션 스타일을 선택해주세요.'
    return
  }
  
  if (!budget.value) {
    errorMessage.value = '월간 예산을 선택해주세요.'
    return
  }
  
  errorMessage.value = ''
  
  const backendStyle = convertStyleToBackend(selectedStyle.value)
  const backendBudget = convertBudgetToBackend(budget.value)
  
  placeStore.updateSelection({
    style: backendStyle,
    budget: backendBudget,
    region: selectedRegions.value.length > 0 ? selectedRegions.value.join(',') : '',
    transport: transport.value
  })
  
  try {
    await placeStore.fetchPlaceRecommendation()
    router.push('/recommend/place/result')
  } catch (e) {
    errorMessage.value = placeStore.error || '장소 추천을 불러오는데 실패했습니다.'
  }
}

function goBack() {
  router.back()
}
</script>

<style scoped>
.place-select-wrapper {
  background: white;
  min-height: calc(100vh - 64px);
  padding-top: 4rem;
  padding-bottom: 4rem;
  /* SVG hover 효과가 잘리지 않도록 */
  overflow: visible;
  position: relative;
}

.back-link {
  appearance: none;
  border: none;
  background: transparent;
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  color: #64748b;
  text-decoration: none;
  font-size: 0.9rem;
  font-weight: 500;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  transition: all 0.3s ease;
  margin-bottom: 2rem;
  cursor: pointer;
}

.back-link:hover {
  background: #f8fafc;
  color: #1e293b;
}

.header-content {
  margin-top: 2rem;
}

.step-indicator {
  display: inline-block;
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
  color: white;
  font-size: 0.6875rem;
  font-weight: 600;
  padding: 0.375rem 1rem;
  border-radius: 0.375rem;
  margin-bottom: 1.25rem;
  letter-spacing: 0.05em;
  text-transform: uppercase;
  box-shadow: 0 2px 8px rgba(30, 41, 59, 0.2);
}

.page-title {
  font-size: clamp(1.75rem, 5vw, 2.5rem);
  font-weight: 800;
  background: linear-gradient(135deg, #1e293b 0%, #475569 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 1rem;
  letter-spacing: -0.02em;
}

.page-subtitle {
  font-size: 1.0625rem;
  color: #64748b;
  margin-bottom: 0;
  font-weight: 400;
}

.form-container {
  max-width: 900px;
  margin: 0 auto;
}

.form-section {
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 1rem;
  padding: 2rem;
  margin-bottom: 1.5rem;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  transition: all 0.2s ease;
  /* SVG hover 효과가 잘리지 않도록 overflow visible */
  overflow: visible;
  position: relative;
}

.form-section:hover {
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  border-color: #d1d5db;
}

.section-header {
  margin-bottom: 1.25rem;
  padding-bottom: 0.75rem;
  border-bottom: 1px solid #f1f5f9;
  position: relative;
}

.section-header::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  width: 48px;
  height: 2px;
  background: linear-gradient(90deg, #6366f1 0%, #1e293b 100%);
}

.section-title {
  font-size: 0.875rem;
  font-weight: 700;
  color: #111827;
  margin: 0;
  letter-spacing: 0.025em;
  text-transform: uppercase;
}

.section-hint {
  font-size: 0.75rem;
  color: #64748b;
  margin: 0.5rem 0 0 0;
  font-weight: 400;
  line-height: 1.5;
}

.required {
  color: #ef4444;
  font-size: 0.875rem;
}

.optional {
  color: #9ca3af;
  font-size: 0.75rem;
  font-weight: 500;
  text-transform: none;
}

/* Style Grid */
.style-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1rem;
}

/* Style Options */
.style-option {
  appearance: none;
  border: 1px solid #d1d5db;
  background: white;
  border-radius: 0.75rem;
  padding: 1.25rem;
  width: 100%;
  cursor: pointer;
  transition: all 0.2s ease;
  text-align: center;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.style-option:hover {
  border-color: #1e293b;
  background: #f9fafb;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.style-option.selected {
  border-color: #6366f1;
  background: linear-gradient(135deg, #eef2ff 0%, #ffffff 100%);
  box-shadow: 0 0 0 2px rgba(99, 102, 241, 0.15), 0 4px 12px rgba(99, 102, 241, 0.1);
}

.option-emoji {
  font-size: 1.75rem;
  margin-bottom: 0.5rem;
  filter: grayscale(0.3);
  transition: filter 0.2s ease;
}

.style-option.selected .option-emoji {
  filter: grayscale(0);
}

.option-check {
  position: absolute;
  top: 1rem;
  right: 1rem;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: linear-gradient(135deg, #6366f1 0%, #4f46e5 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.2s ease;
  box-shadow: 0 2px 8px rgba(99, 102, 241, 0.3);
}

.style-option.selected .option-check {
  opacity: 1;
}

.budget-option.selected .option-check {
  opacity: 1;
}

.check-icon {
  width: 16px;
  height: 16px;
  color: white;
}

.option-title {
  font-size: 1rem;
  font-weight: 600;
  color: #111827;
  margin-bottom: 0.375rem;
  letter-spacing: -0.01em;
}

.option-desc {
  font-size: 0.8125rem;
  color: #6b7280;
  line-height: 1.5;
  margin-bottom: 0.25rem;
}

.option-hint {
  font-size: 0.6875rem;
  color: #94a3b8;
  line-height: 1.4;
  margin-top: 0.25rem;
  font-weight: 500;
}

/* Budget Grid */
.budget-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1rem;
}

/* Budget Options */
.budget-option {
  appearance: none;
  border: 1px solid #d1d5db;
  background: white;
  border-radius: 0.75rem;
  padding: 1.25rem;
  width: 100%;
  cursor: pointer;
  transition: all 0.2s ease;
  text-align: center;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.budget-option:hover {
  border-color: #1e293b;
  background: #f9fafb;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.budget-option.selected {
  border-color: #6366f1;
  background: linear-gradient(135deg, #eef2ff 0%, #ffffff 100%);
  box-shadow: 0 0 0 2px rgba(99, 102, 241, 0.15), 0 4px 12px rgba(99, 102, 241, 0.1);
}

/* Select */
.form-select-custom {
  width: 100%;
  padding: 1rem 1.25rem;
  font-size: 0.9375rem;
  border: 1px solid #d1d5db;
  border-radius: 0.75rem;
  background: white;
  cursor: pointer;
  transition: all 0.2s ease;
  color: #111827;
  font-weight: 400;
}

.form-select-custom:focus {
  outline: none;
  border-color: #1e293b;
  box-shadow: 0 0 0 3px rgba(30, 41, 59, 0.1);
}

/* Region Map */
.region-map-container {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
  /* SVG hover 효과가 잘리지 않도록 */
  overflow: visible;
  position: relative;
}

.region-selected-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  padding: 1rem;
  background: linear-gradient(135deg, #eef2ff 0%, #ffffff 100%);
  border-radius: 0.75rem;
  border: 1px solid #c7d2fe;
}

.selected-tag {
  display: inline-flex;
  align-items: center;
  gap: 0.375rem;
  background: linear-gradient(135deg, #6366f1 0%, #4f46e5 100%);
  color: white;
  padding: 0.375rem 0.75rem;
  border-radius: 0.5rem;
  font-size: 0.8125rem;
  font-weight: 500;
  box-shadow: 0 2px 8px rgba(99, 102, 241, 0.25);
}

.remove-tag {
  appearance: none;
  border: none;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.125rem;
  line-height: 1;
  transition: all 0.2s ease;
  padding: 0;
}

.remove-tag:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: scale(1.1);
}

.map-wrapper {
  position: relative;
  background: linear-gradient(135deg, #f9fafb 0%, #ffffff 100%);
  border-radius: 0.75rem;
  padding: 1.5rem;
  border: 1px solid #e5e7eb;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: visible;
  /* pointer-events가 자식에 전달되도록 */
  pointer-events: auto;
  z-index: 1;
}

.korea-map-container {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0 auto;
  overflow: visible;
  /* pointer-events가 path에 전달되도록 */
  pointer-events: auto;
  position: relative;
  z-index: 1;
}

.korea-map-container svg {
  max-width: 500px;
  min-width: 280px;
  width: 100%;
  height: auto;
  min-height: 350px;
  display: block;
  margin: 0 auto;
  overflow: visible;
  /* SVG가 pointer-events를 받을 수 있도록 */
  pointer-events: auto;
  position: relative;
}

.korea-map-container path {
  fill: #f1f5f9;
  stroke: #cbd5e1;
  stroke-width: 1.5;
  cursor: pointer;
  transition: fill 0.25s cubic-bezier(0.4, 0, 0.2, 1),
              stroke 0.25s cubic-bezier(0.4, 0, 0.2, 1),
              stroke-width 0.25s cubic-bezier(0.4, 0, 0.2, 1),
              transform 0.25s cubic-bezier(0.4, 0, 0.2, 1),
              filter 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  /* pointer-events 명시적으로 설정 */
  pointer-events: all !important;
  /* SVG path의 fill 영역 중심을 기준으로 transform */
  transform-origin: center;
  transform-box: fill-box;
  transform: scale(1) translateY(0);
  /* hover 시 다른 path 위에 표시되도록 */
  position: relative;
}

/* 호버 상태 - DOM 순서상 맨 뒤로 이동되어 다른 path 위에 렌더링됨 */
.korea-map-container path.region-hover,
.korea-map-container path:hover {
  fill: #6366f1 !important;
  stroke: #4f46e5 !important;
  stroke-width: 3.5 !important;
  /* path의 중심을 기준으로 살짝 확대 + 위로 이동 */
  transform: scale(1.04) translateY(-3px) !important;
  /* 강한 그림자 효과로 떠 보이는 느낌 명확히 */
  filter: drop-shadow(0 8px 24px rgba(99, 102, 241, 0.6)) !important;
}

/* SVG 인라인 속성보다 CSS가 우선하도록 더 구체적인 선택자 */
.korea-map-container svg path.region-hover,
.korea-map-container svg path:hover {
  fill: #6366f1 !important;
  stroke: #4f46e5 !important;
  stroke-width: 3.5 !important;
  transform: scale(1.04) translateY(-3px) !important;
  filter: drop-shadow(0 8px 24px rgba(99, 102, 241, 0.6)) !important;
}

/* 클릭 시 피드백 */
.korea-map-container path:active {
  fill: #4f46e5;
  stroke: #4338ca;
  stroke-width: 2.5;
  filter: drop-shadow(0 1px 4px rgba(99, 102, 241, 0.5));
}

/* 선택된 지역 - hover와 구분되는 선택 상태 */
.korea-map-container path.region-active {
  fill: #6366f1 !important;
  stroke: #4f46e5 !important;
  stroke-width: 3 !important;
  filter: drop-shadow(0 4px 12px rgba(99, 102, 241, 0.4)) !important;
  transform: scale(1) translateY(0) !important;
}

/* 선택된 지역에 호버 시 - 선택 상태 + hover 효과 결합 */
.korea-map-container path.region-active.region-hover,
.korea-map-container path.region-active:hover {
  fill: #4f46e5 !important;
  stroke: #4338ca !important;
  stroke-width: 4 !important;
  /* 선택된 지역도 동일하게 transform-origin 중심 기준 확대 */
  transform: scale(1.04) translateY(-3px) !important;
  filter: drop-shadow(0 8px 24px rgba(99, 102, 241, 0.7)) !important;
}

.region-tooltip {
  position: absolute;
  transform: translateX(-50%) translateY(-100%);
  margin-top: -8px;
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  font-weight: 600;
  pointer-events: none;
  box-shadow: 0 4px 12px rgba(30, 41, 59, 0.3);
  z-index: 100;
  animation: tooltipFadeIn 0.15s ease-out;
  white-space: nowrap;
  border: none;
}

.region-tooltip::after {
  content: '';
  position: absolute;
  top: 100%;
  left: 50%;
  transform: translateX(-50%);
  border: 6px solid transparent;
  border-top-color: #1e293b;
}

@keyframes tooltipFadeIn {
  from {
    opacity: 0;
    transform: translateX(-50%) translateY(-100%) translateY(-4px);
  }
  to {
    opacity: 1;
    transform: translateX(-50%) translateY(-100%) translateY(0);
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateX(-50%) translateY(-5px);
  }
  to {
    opacity: 1;
    transform: translateX(-50%) translateY(0);
  }
}

/* Error Alert */
.error-alert {
  background: #fef2f2;
  border: 1px solid #fecaca;
  color: #991b1b;
  padding: 0.75rem 1rem;
  border-radius: 0.5rem;
  text-align: left;
  margin-bottom: 1.5rem;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
}

.error-icon-svg {
  width: 18px;
  height: 18px;
  flex-shrink: 0;
}

/* Submit */
.submit-section {
  text-align: center;
  padding-top: 1rem;
}

.btn-submit {
  appearance: none;
  border: none;
  background: linear-gradient(135deg, #1e293b 0%, #334155 100%);
  color: white;
  font-size: 1rem;
  font-weight: 600;
  padding: 1rem 2rem;
  border-radius: 0.75rem;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 4px 12px rgba(30, 41, 59, 0.25);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 200px;
  position: relative;
  overflow: hidden;
}

.btn-submit::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.15), transparent);
  transition: left 0.5s ease;
}

.btn-submit:hover:not(:disabled)::before {
  left: 100%;
}

.btn-submit:hover:not(:disabled) {
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
  box-shadow: 0 8px 20px rgba(30, 41, 59, 0.35);
  transform: translateY(-2px);
}

.btn-submit:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.animate-spin {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

@media (max-width: 992px) {
  .place-select-wrapper {
    padding-top: 3rem;
    padding-bottom: 3rem;
  }
  
  .style-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .budget-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 576px) {
  .place-select-wrapper {
    padding-top: 2rem;
    padding-bottom: 2rem;
  }
  
  .form-section {
    padding: 1.25rem;
  }
  
  .style-grid {
    grid-template-columns: 1fr;
  }
  
  .budget-grid {
    grid-template-columns: 1fr;
  }
  
  .map-wrapper {
    padding: 1rem;
  }
  
  .korea-map-container svg {
    max-width: 280px;
    min-height: 300px;
  }
  
  .btn-submit {
    width: 100%;
    min-width: unset;
  }
}
</style>
